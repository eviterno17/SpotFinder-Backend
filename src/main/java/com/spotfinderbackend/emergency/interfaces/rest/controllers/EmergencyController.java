package com.spotfinderbackend.emergency.interfaces.rest.controllers;

import com.spotfinderbackend.emergency.domain.model.commands.ActivateEvacuationCommand;
import com.spotfinderbackend.emergency.domain.model.commands.ResolveEmergencyCommand;
import com.spotfinderbackend.emergency.domain.model.queries.*;
import com.spotfinderbackend.emergency.domain.services.EmergencyCommandService;
import com.spotfinderbackend.emergency.domain.services.EmergencyQueryService;
import com.spotfinderbackend.emergency.interfaces.rest.resources.*;
import com.spotfinderbackend.emergency.interfaces.rest.transform.EmergencyAlertResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/emergency", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Emergency", description = "Emergency alert endpoints")
public class EmergencyController {

    private final EmergencyCommandService commandService;
    private final EmergencyQueryService queryService;

    public EmergencyController(EmergencyCommandService commandService, EmergencyQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Trigger an emergency alert (called by edge server)")
    @PostMapping("/alerts")
    public ResponseEntity<EmergencyAlertResource> trigger(@RequestBody TriggerAlertResource resource) {
        var alert = commandService.handle(EmergencyAlertResourceFromEntityAssembler.toCommand(resource));
        return alert
                .map(a -> new ResponseEntity<>(EmergencyAlertResourceFromEntityAssembler.toResourceFromEntity(a), HttpStatus.CREATED))
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Get current emergency status (NORMAL or EMERGENCY)")
    @GetMapping("/status")
    public ResponseEntity<EmergencyStatusResource> status() {
        var response = queryService.handle(new GetEmergencyStatusQuery());
        return ResponseEntity.ok(EmergencyAlertResourceFromEntityAssembler.toResourceFromResponse(response));
    }

    @Operation(summary = "Activate evacuation manually (admin)")
    @PostMapping("/evacuate")
    public ResponseEntity<Void> evacuate() {
        commandService.handle(new ActivateEvacuationCommand(null));
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Resolve an emergency (admin)")
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Long id, @RequestBody ResolveEmergencyResource resource) {
        commandService.handle(new ResolveEmergencyCommand(id, resource.adminUserId()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get emergency by id")
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyAlertResource> getById(@PathVariable Long id) {
        return queryService.handle(new GetEmergencyByIdQuery(id))
                .map(a -> ResponseEntity.ok(EmergencyAlertResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get emergency history")
    @GetMapping("/history")
    public ResponseEntity<List<EmergencyAlertResource>> history(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var alerts = queryService.handle(new GetEmergencyHistoryQuery(startDate, endDate));
        return ResponseEntity.ok(EmergencyAlertResourceFromEntityAssembler.toResourcesFromEntities(alerts));
    }
}
