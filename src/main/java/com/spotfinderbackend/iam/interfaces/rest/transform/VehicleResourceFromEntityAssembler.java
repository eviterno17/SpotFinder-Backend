package com.spotfinderbackend.iam.interfaces.rest.transform;

import com.spotfinderbackend.iam.domain.model.aggregates.Vehicle;
import com.spotfinderbackend.iam.domain.model.commands.RegisterVehicleCommand;
import com.spotfinderbackend.iam.interfaces.rest.resources.RegisterVehicleResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.VehicleResource;

import java.util.Collection;
import java.util.List;

public class VehicleResourceFromEntityAssembler {

    public static VehicleResource toResourceFromEntity(Vehicle v) {
        return new VehicleResource(
                v.getId(),
                v.getUserId(),
                v.getPlate() == null ? null : v.getPlate().value(),
                v.getBrand(),
                v.getModel(),
                v.getColor(),
                v.getCreatedAt()
        );
    }

    public static List<VehicleResource> toResourcesFromEntities(Collection<Vehicle> entities) {
        return entities.stream().map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    public static RegisterVehicleCommand toCommandFromResource(Long userId, RegisterVehicleResource r) {
        return new RegisterVehicleCommand(userId, r.plate(), r.brand(), r.model(), r.color());
    }
}
