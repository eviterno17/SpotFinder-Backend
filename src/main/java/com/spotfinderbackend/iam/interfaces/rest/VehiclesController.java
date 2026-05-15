package com.spotfinderbackend.iam.interfaces.rest;

import com.spotfinderbackend.iam.domain.model.commands.DeleteVehicleCommand;
import com.spotfinderbackend.iam.domain.model.queries.GetVehiclesByUserIdQuery;
import com.spotfinderbackend.iam.domain.services.VehicleCommandService;
import com.spotfinderbackend.iam.domain.services.VehicleQueryService;
import com.spotfinderbackend.iam.interfaces.rest.resources.RegisterVehicleResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.VehicleResource;
import com.spotfinderbackend.iam.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/vehicles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicles registered by a user (used by ALPR flow)")
public class VehiclesController {

    private final VehicleCommandService commandService;
    private final VehicleQueryService queryService;

    public VehiclesController(VehicleCommandService commandService, VehicleQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Register a vehicle for a user (TS36)")
    @PostMapping
    public ResponseEntity<VehicleResource> register(@PathVariable Long userId,
                                                    @RequestBody RegisterVehicleResource resource) {
        var command = VehicleResourceFromEntityAssembler.toCommandFromResource(userId, resource);
        var vehicle = commandService.handle(command);
        return vehicle
                .map(v -> new ResponseEntity<>(VehicleResourceFromEntityAssembler.toResourceFromEntity(v), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "List vehicles of a user (TS37)")
    @GetMapping
    public ResponseEntity<List<VehicleResource>> list(@PathVariable Long userId) {
        var vehicles = queryService.handle(new GetVehiclesByUserIdQuery(userId));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourcesFromEntities(vehicles));
    }

    @Operation(summary = "Delete a vehicle (TS38)")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long vehicleId) {
        commandService.handle(new DeleteVehicleCommand(vehicleId, userId));
        return ResponseEntity.noContent().build();
    }
}
