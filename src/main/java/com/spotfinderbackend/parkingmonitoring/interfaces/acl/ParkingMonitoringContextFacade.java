package com.spotfinderbackend.parkingmonitoring.interfaces.acl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Anti-Corruption Layer facade for Parking Monitoring BC.
 * <p>
 * Provides stable, coarse-grained methods to other BCs (Access Control,
 * Emergency, Analytics) without exposing internal aggregates.
 */
public interface ParkingMonitoringContextFacade {

    /** Notify Parking Monitoring to free a slot. */
    void releaseSlot(Long slotId);

    /** Set every LED/slot to evacuation mode (emergency protocol). */
    void setAllLedsToEvacuationMode();

    /** Restore LEDs to their normal status after an emergency is resolved. */
    void restoreLedsToNormalMode();

    /** Snapshot a parking slot's status. */
    Optional<SlotSnapshot> findSlot(Long slotId);

    /** Get total slots in a facility. */
    int countTotalSlots(Long facilityId);

    /** Historical snapshots in a date range (used by Analytics). */
    List<SlotSnapshot> getSnapshotsBetween(LocalDateTime start, LocalDateTime end, Long facilityId);

    record SlotSnapshot(Long slotId, String slotCode, String status, LocalDateTime timestamp) { }
}
