package com.spotfinderbackend.iam.domain.model.exceptions;

import com.spotfinderbackend.shared.domain.model.exceptions.ConflictException;

public class VehicleAlreadyRegisteredException extends ConflictException {
    public VehicleAlreadyRegisteredException(String plate) {
        super("Plate already registered: " + plate);
    }
}
