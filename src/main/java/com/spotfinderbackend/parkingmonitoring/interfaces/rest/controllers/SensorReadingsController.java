package com.spotfinderbackend.parkingmonitoring.interfaces.rest.controllers;

import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotCommandService;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.resources.SensorReadingResource;
import com.spotfinderbackend.parkingmonitoring.interfaces.rest.transform.ProcessSensorReadingCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/sensor-readings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Sensor Readings", description = "Receives ultrasonic sensor readings from the edge server")
public class SensorReadingsController {

    private final ParkingSlotCommandService commandService;

    public SensorReadingsController(ParkingSlotCommandService commandService) {
        this.commandService = commandService;
    }

    @Operation(summary = "Register a sensor reading")
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody SensorReadingResource resource) {
        var command = ProcessSensorReadingCommandFromResourceAssembler.toCommandFromResource(resource);
        commandService.handle(command);
        return ResponseEntity.accepted().build();
    }
}
