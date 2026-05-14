package com.spotfinderbackend.accesscontrol.domain.services;

import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.LicensePlate;

import java.util.Optional;

/**
 * Domain service interface for ALPR. Implementation lives in infrastructure
 * (Plate Recognizer API client).
 */
public interface PlateRecognitionService {
    Optional<LicensePlate> recognizePlate(byte[] imageData);
}
