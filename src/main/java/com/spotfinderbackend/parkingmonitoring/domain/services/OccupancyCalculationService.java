package com.spotfinderbackend.parkingmonitoring.domain.services;

import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Domain service encapsulating occupancy and debounce rules.
 * <p>
 * Rule (PDF §4.2.1):
 * <ul>
 *   <li>distance &lt; 15cm → OCCUPIED</li>
 *   <li>distance &gt; 50cm → AVAILABLE</li>
 *   <li>between → no change (hysteresis)</li>
 * </ul>
 */
@Service
public class OccupancyCalculationService {

    private static final double OCCUPIED_THRESHOLD_CM = 15.0;
    private static final double AVAILABLE_THRESHOLD_CM = 50.0;
    private static final double OCCUPANCY_ALERT_THRESHOLD = 0.90;

    public Optional<SlotStatus> evaluateSensorReading(double distance, SlotStatus currentStatus) {
        if (currentStatus == SlotStatus.OUT_OF_SERVICE || currentStatus == SlotStatus.EVACUATION) {
            return Optional.empty();
        }
        if (distance < OCCUPIED_THRESHOLD_CM && currentStatus != SlotStatus.OCCUPIED) {
            return Optional.of(SlotStatus.OCCUPIED);
        }
        if (distance > AVAILABLE_THRESHOLD_CM && currentStatus != SlotStatus.AVAILABLE) {
            return Optional.of(SlotStatus.AVAILABLE);
        }
        return Optional.empty();
    }

    public double calculateOccupancyRate(long totalSlots, long occupiedSlots) {
        if (totalSlots <= 0) return 0.0;
        return (double) occupiedSlots / (double) totalSlots;
    }

    public boolean shouldTriggerAlert(double occupancyRate) {
        return occupancyRate >= OCCUPANCY_ALERT_THRESHOLD;
    }
}
