package com.spotfinderbackend.parkingmonitoring.domain.model.aggregates;

import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RegisterFacilityCommand;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.Address;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.FacilityName;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class ParkingFacility extends AuditableAbstractAggregateRoot<ParkingFacility> {

    @Embedded
    private FacilityName name;

    private int totalSlots;

    @Embedded
    private Address address;

    protected ParkingFacility() {}

    public ParkingFacility(RegisterFacilityCommand command) {
        this.name = new FacilityName(command.name());
        this.totalSlots = command.totalSlots();
        this.address = new Address(command.address() == null ? "" : command.address());
    }

    public void updateInfo(String name, String address) {
        if (name != null && !name.isBlank()) this.name = new FacilityName(name);
        if (address != null) this.address = new Address(address);
    }
}
