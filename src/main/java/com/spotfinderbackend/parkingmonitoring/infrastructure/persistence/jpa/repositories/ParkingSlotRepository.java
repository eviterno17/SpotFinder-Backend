package com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingSlot;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.FacilityId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SensorId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotCode;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByStatus(SlotStatus status);

    List<ParkingSlot> findByFacilityId(FacilityId facilityId);

    List<ParkingSlot> findByFacilityIdAndStatus(FacilityId facilityId, SlotStatus status);

    long countByFacilityIdAndStatus(FacilityId facilityId, SlotStatus status);

    long countByFacilityId(FacilityId facilityId);

    boolean existsBySlotCode(SlotCode slotCode);

    boolean existsBySensorId(SensorId sensorId);

    Optional<ParkingSlot> findBySensorId(SensorId sensorId);
}
