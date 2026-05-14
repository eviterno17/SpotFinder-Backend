package com.spotfinderbackend.parkingmonitoring.interfaces.rest.controllers;

import com.spotfinderbackend.parkingmonitoring.domain.model.queries.*;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotCommandService;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotQueryService;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.*;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform.ParkingSlotResourceFromEntityAssembler;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform.RegisterParkingSlotCommandFromResourceAssembler;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform.UpdateSlotStatusCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/parking-slots", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Parking Slots", description = "Parking slot monitoring endpoints")
public class ParkingSlotsController {

    private final ParkingSlotCommandService commandService;
    private final ParkingSlotQueryService queryService;

    public ParkingSlotsController(ParkingSlotCommandService commandService, ParkingSlotQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Register a new parking slot")
    @PostMapping
    public ResponseEntity<ParkingSlotResource> register(@RequestBody RegisterParkingSlotResource resource) {
        var command = RegisterParkingSlotCommandFromResourceAssembler.toCommandFromResource(resource);
        var slot = commandService.handle(command);
        if (slot.isEmpty()) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(ParkingSlotResourceFromEntityAssembler.toResourceFromEntity(slot.get()), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all slots, optionally filtered by facility")
    @GetMapping
    public ResponseEntity<List<ParkingSlotResource>> getAll(@RequestParam(required = false) Long facilityId) {
        var slots = queryService.handle(new GetAllSlotsQuery(facilityId));
        return ResponseEntity.ok(ParkingSlotResourceFromEntityAssembler.toResourcesFromEntities(slots));
    }

    @Operation(summary = "Get available slots")
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSlotResource>> getAvailable(@RequestParam(required = false) Long facilityId) {
        var slots = queryService.handle(new GetAvailableSlotsQuery(facilityId));
        return ResponseEntity.ok(ParkingSlotResourceFromEntityAssembler.toResourcesFromEntities(slots));
    }

    @Operation(summary = "Get a slot by id")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSlotResource> getById(@PathVariable Long id) {
        return queryService.handle(new GetSlotByIdQuery(id))
                .map(s -> ResponseEntity.ok(ParkingSlotResourceFromEntityAssembler.toResourceFromEntity(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get recommended (closest available) slots")
    @GetMapping("/recommendations")
    public ResponseEntity<List<ParkingSlotResource>> getRecommendations(@RequestParam(required = false) Long facilityId) {
        var slots = queryService.handle(new GetSlotRecommendationsQuery(facilityId));
        return ResponseEntity.ok(ParkingSlotResourceFromEntityAssembler.toResourcesFromEntities(slots));
    }

    @Operation(summary = "Get occupancy summary")
    @GetMapping("/occupancy")
    public ResponseEntity<OccupancySummaryResource> getOccupancySummary(@RequestParam(required = false) Long facilityId) {
        var summary = queryService.handle(new GetOccupancySummaryQuery(facilityId));
        return ResponseEntity.ok(new OccupancySummaryResource(summary.total(), summary.available(),
                summary.occupied(), summary.occupancyRate()));
    }

    @Operation(summary = "Update slot status (admin)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody SlotStatusUpdateResource resource) {
        var command = UpdateSlotStatusCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
