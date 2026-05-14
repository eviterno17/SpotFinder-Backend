package com.spotfinderbackend.analytics.interfaces.rest.controllers;

import com.spotfinderbackend.analytics.domain.model.queries.*;
import com.spotfinderbackend.analytics.domain.services.AnalyticsQueryService;
import com.spotfinderbackend.analytics.interfaces.rest.resources.*;
import com.spotfinderbackend.analytics.interfaces.rest.transform.AnalyticsResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/analytics", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Analytics", description = "Aggregated metrics for parking operations")
public class AnalyticsController {

    private final AnalyticsQueryService queryService;

    public AnalyticsController(AnalyticsQueryService queryService) {
        this.queryService = queryService;
    }

    @Operation(summary = "Get occupancy metrics for a period")
    @GetMapping("/occupancy")
    public ResponseEntity<OccupancyMetricsResource> occupancy(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long facilityId) {
        var metrics = queryService.handle(new GetOccupancyMetricsQuery(startDate, endDate, facilityId));
        return ResponseEntity.ok(AnalyticsResourceAssembler.toResource(metrics));
    }

    @Operation(summary = "Get revenue metrics for a period")
    @GetMapping("/revenue")
    public ResponseEntity<RevenueMetricsResource> revenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long facilityId) {
        var metrics = queryService.handle(new GetRevenueMetricsQuery(startDate, endDate, facilityId));
        return ResponseEntity.ok(AnalyticsResourceAssembler.toResource(metrics));
    }

    @Operation(summary = "Get heatmap data")
    @GetMapping("/heatmap")
    public ResponseEntity<List<HeatmapEntryResource>> heatmap(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long facilityId) {
        var entries = queryService.handle(new GetHeatmapDataQuery(startDate, endDate, facilityId));
        return ResponseEntity.ok(AnalyticsResourceAssembler.toHeatmapResources(entries));
    }

    @Operation(summary = "Get peak hours data")
    @GetMapping("/peak-hours")
    public ResponseEntity<PeakHoursResource> peakHours(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long facilityId) {
        var data = queryService.handle(new GetPeakHoursQuery(startDate, endDate, facilityId));
        return ResponseEntity.ok(AnalyticsResourceAssembler.toResource(data));
    }
}
