package com.spotfinderbackend.accesscontrol.interfaces.rest.controllers;

import com.spotfinderbackend.accesscontrol.domain.services.AccessCommandService;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.*;
import com.spotfinderbackend.accesscontrol.interfaces.rest.transform.AccessCommandFromResourceAssembler;
import com.spotfinderbackend.accesscontrol.interfaces.rest.transform.VehicleSessionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/access", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Access Control", description = "Vehicle entry / exit and ALPR endpoints")
public class AccessController {

    private final AccessCommandService accessCommandService;

    public AccessController(AccessCommandService accessCommandService) {
        this.accessCommandService = accessCommandService;
    }

    @Operation(summary = "Register a vehicle entry (ALPR + barrier open + session creation)")
    @PostMapping("/entries")
    public ResponseEntity<VehicleSessionResource> registerEntry(@RequestBody EntryRequestResource resource) {
        var sessionOpt = accessCommandService.handle(AccessCommandFromResourceAssembler.toCommand(resource));
        return sessionOpt
                .map(s -> new ResponseEntity<>(VehicleSessionResourceFromEntityAssembler.toResourceFromEntity(s), HttpStatus.CREATED))
                .orElse(ResponseEntity.unprocessableEntity().build());
    }

    @Operation(summary = "Register a vehicle exit (payment verification + barrier open + session close)")
    @PostMapping("/exits")
    public ResponseEntity<Void> registerExit(@RequestBody ExitRequestResource resource) {
        accessCommandService.handle(AccessCommandFromResourceAssembler.toCommand(resource));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Run ALPR on a plate image (auxiliary endpoint)")
    @PostMapping("/alpr")
    public ResponseEntity<PlateRecognitionResultResource> recognize(@RequestBody PlateRecognitionResource resource) {
        var plateOpt = accessCommandService.handle(AccessCommandFromResourceAssembler.toCommand(resource));
        return plateOpt
                .map(p -> ResponseEntity.ok(new PlateRecognitionResultResource(p.plateText(), p.confidence(), p.isHighConfidence())))
                .orElse(ResponseEntity.unprocessableEntity().build());
    }
}
