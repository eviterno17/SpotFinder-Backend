package com.spotfinderbackend.iam.domain.model.aggregates;

import com.spotfinderbackend.iam.domain.model.valueobjects.Plate;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Vehicle registered by a user. Used by Access Control's ALPR flow to associate
 * a recognized plate to a user (and therefore to push notifications / payments).
 */
@Entity
@Getter
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    @Column(nullable = false)
    private Long userId;

    @Embedded
    private Plate plate;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String model;

    @Column(length = 30)
    private String color;

    protected Vehicle() {}

    public Vehicle(Long userId, String plate, String brand, String model, String color) {
        if (userId == null || userId <= 0)
            throw new BadRequestException("userId is required");
        this.userId = userId;
        this.plate = new Plate(plate);
        this.brand = brand;
        this.model = model;
        this.color = color;
    }

    public void updateDetails(String brand, String model, String color) {
        if (brand != null) this.brand = brand;
        if (model != null) this.model = model;
        if (color != null) this.color = color;
    }
}
