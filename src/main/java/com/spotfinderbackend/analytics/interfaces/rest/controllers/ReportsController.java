package com.spotfinderbackend.analytics.interfaces.rest.controllers;

import com.spotfinderbackend.analytics.domain.model.queries.GetReportByIdQuery;
import com.spotfinderbackend.analytics.domain.model.queries.GetReportsByFacilityQuery;
import com.spotfinderbackend.analytics.domain.services.ReportCommandService;
import com.spotfinderbackend.analytics.domain.services.ReportQueryService;
import com.spotfinderbackend.analytics.interfaces.rest.resources.GenerateReportResource;
import com.spotfinderbackend.analytics.interfaces.rest.resources.ReportResource;
import com.spotfinderbackend.analytics.interfaces.rest.transform.AnalyticsResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/reports", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Generate and download analytics reports")
public class ReportsController {

    private final ReportCommandService commandService;
    private final ReportQueryService queryService;

    public ReportsController(ReportCommandService commandService, ReportQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Generate a new report")
    @PostMapping
    public ResponseEntity<ReportResource> generate(@RequestBody GenerateReportResource resource) {
        var report = commandService.handle(AnalyticsResourceAssembler.toCommand(resource));
        return report
                .map(r -> new ResponseEntity<>(AnalyticsResourceAssembler.toResource(r), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get a report by id")
    @GetMapping("/{id}")
    public ResponseEntity<ReportResource> getById(@PathVariable Long id) {
        return queryService.handle(new GetReportByIdQuery(id))
                .map(r -> ResponseEntity.ok(AnalyticsResourceAssembler.toResource(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List reports")
    @GetMapping
    public ResponseEntity<List<ReportResource>> list(@RequestParam(required = false) Long facilityId) {
        var reports = queryService.handle(new GetReportsByFacilityQuery(facilityId));
        return ResponseEntity.ok(AnalyticsResourceAssembler.toReportResources(reports));
    }

    @Operation(summary = "Download a report PDF")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        var reportOpt = queryService.handle(new GetReportByIdQuery(id));
        if (reportOpt.isEmpty() || reportOpt.get().getFileUrl() == null)
            return ResponseEntity.notFound().build();
        File file = new File(reportOpt.get().getFileUrl());
        if (!file.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .body(new FileSystemResource(file));
    }
}
