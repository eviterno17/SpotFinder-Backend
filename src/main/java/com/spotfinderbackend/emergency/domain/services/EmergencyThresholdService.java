package com.spotfinderbackend.emergency.domain.services;

import com.spotfinderbackend.emergency.domain.model.valueobjects.EmergencyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Domain service that evaluates danger thresholds for gas/smoke sensors.
 * Default threshold (PDF §4.2.4): 900 PPM.
 */
@Service
public class EmergencyThresholdService {

    private final int gasThreshold;

    public EmergencyThresholdService(@Value("${spotfinder.emergency.gas-threshold-ppm:900}") int gasThreshold) {
        this.gasThreshold = gasThreshold;
    }

    public boolean isGasLevelDangerous(int gasPPM) {
        return gasPPM >= gasThreshold;
    }

    public boolean isDangerousCondition(int gasPPM, EmergencyType type) {
        if (type == null) return false;
        return isGasLevelDangerous(gasPPM);
    }

    public int getGasThreshold() {
        return gasThreshold;
    }
}
