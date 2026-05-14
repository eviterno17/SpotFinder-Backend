package com.spotfinderbackend.parkingmonitoring.interfaces.rest.controllers;

import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingFacilityCommandService;
import com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories.ParkingFacilityRepository;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.ParkingFacilityResource;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.RegisterFacilityResource;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform.ParkingFacilityResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/parking-facilities", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Parking Facilities", description = "Manages parking facilities (malls)")
public class ParkingFacilitiesController {

    private final ParkingFacilityCommandService commandService;
    private final ParkingFacilityRepository facilityRepository;

    public ParkingFacilitiesController(ParkingFacilityCommandService commandService,
                                       ParkingFacilityRepository facilityRepository) {
        this.commandService = commandService;
        this.facilityRepository = facilityRepository;
    }

    @Operation(summary = "Register a parking facility")
    @PostMapping
    public ResponseEntity<ParkingFacilityResource> register(@RequestBody RegisterFacilityResource resource) {
        var command = ParkingFacilityResourceFromEntityAssembler.toCommandFromResource(resource);
        var facility = commandService.handle(command);
        if (facility.isEmpty()) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(ParkingFacilityResourceFromEntityAssembler.toResourceFromEntity(facility.get()), HttpStatus.CREATED);
    }

    @Operation(summary = "List all parking facilities")
    @GetMapping
    public ResponseEntity<List<ParkingFacilityResource>> getAll() {
        var resources = facilityRepository.findAll().stream()
                .map(ParkingFacilityResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get facility by id")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingFacilityResource> getById(@PathVariable Long id) {
        return facilityRepository.findById(id)
                .map(f -> ResponseEntity.ok(ParkingFacilityResourceFromEntityAssembler.toResourceFromEntity(f)))
                .orElse(ResponseEntity.notFound().build());
    }
}
