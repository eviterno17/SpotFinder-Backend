package com.spotfinderbackend.accesscontrol.interfaces.rest.controllers;

import com.spotfinderbackend.accesscontrol.domain.model.commands.CreateVehicleSessionCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.EndVehicleSessionCommand;
import com.spotfinderbackend.accesscontrol.domain.model.queries.*;
import com.spotfinderbackend.accesscontrol.domain.services.VehicleSessionCommandService;
import com.spotfinderbackend.accesscontrol.domain.services.VehicleSessionQueryService;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.CreateSessionResource;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.VehicleSessionResource;
import com.spotfinderbackend.accesscontrol.interfaces.rest.transform.VehicleSessionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/parking-sessions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Parking Sessions", description = "Vehicle sessions (active, history, end)")
public class ParkingSessionsController {

    private final VehicleSessionCommandService commandService;
    private final VehicleSessionQueryService queryService;

    public ParkingSessionsController(VehicleSessionCommandService commandService, VehicleSessionQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a vehicle session manually")
    @PostMapping
    public ResponseEntity<VehicleSessionResource> create(@RequestBody CreateSessionResource resource) {
        var session = commandService.handle(new CreateVehicleSessionCommand(resource.licensePlate(), LocalDateTime.now(), resource.userId()));
        if (session.isEmpty()) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(VehicleSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()), HttpStatus.CREATED);
    }

    @Operation(summary = "Get authenticated user's active session")
    @GetMapping("/active")
    public ResponseEntity<VehicleSessionResource> getActive(@RequestParam Long userId) {
        return queryService.handle(new GetActiveSessionQuery(userId))
                .map(s -> ResponseEntity.ok(VehicleSessionResourceFromEntityAssembler.toResourceFromEntity(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get session by id")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleSessionResource> getById(@PathVariable Long id) {
        return queryService.handle(new GetSessionByIdQuery(id))
                .map(s -> ResponseEntity.ok(VehicleSessionResourceFromEntityAssembler.toResourceFromEntity(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "End a vehicle session (payment required)")
    @PatchMapping("/{id}/end")
    public ResponseEntity<Void> end(@PathVariable Long id) {
        commandService.handle(new EndVehicleSessionCommand(id, LocalDateTime.now()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user's session history")
    @GetMapping("/history")
    public ResponseEntity<List<VehicleSessionResource>> history(@RequestParam Long userId) {
        var sessions = queryService.handle(new GetSessionHistoryQuery(userId));
        return ResponseEntity.ok(VehicleSessionResourceFromEntityAssembler.toResourcesFromEntities(sessions));
    }
}
