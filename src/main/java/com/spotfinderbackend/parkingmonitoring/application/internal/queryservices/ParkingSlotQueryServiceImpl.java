package com.spotfinderbackend.parkingmonitoring.application.internal.queryservices;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingSlot;
import com.spotfinderbackend.parkingmonitoring.domain.model.queries.*;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.FacilityId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import com.spotfinderbackend.parkingmonitoring.domain.services.OccupancyCalculationService;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotQueryService;
import com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories.ParkingSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingSlotQueryServiceImpl implements ParkingSlotQueryService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final OccupancyCalculationService occupancyCalculationService;

    public ParkingSlotQueryServiceImpl(ParkingSlotRepository parkingSlotRepository,
                                       OccupancyCalculationService occupancyCalculationService) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.occupancyCalculationService = occupancyCalculationService;
    }

    @Override
    public List<ParkingSlot> handle(GetAllSlotsQuery query) {
        if (query.facilityId() == null) return parkingSlotRepository.findAll();
        return parkingSlotRepository.findByFacilityId(new FacilityId(query.facilityId()));
    }

    @Override
    public List<ParkingSlot> handle(GetAvailableSlotsQuery query) {
        if (query.facilityId() == null) return parkingSlotRepository.findByStatus(SlotStatus.AVAILABLE);
        return parkingSlotRepository.findByFacilityIdAndStatus(new FacilityId(query.facilityId()), SlotStatus.AVAILABLE);
    }

    @Override
    public Optional<ParkingSlot> handle(GetSlotByIdQuery query) {
        return parkingSlotRepository.findById(query.slotId());
    }

    @Override
    public List<ParkingSlot> handle(GetSlotRecommendationsQuery query) {
        // Simple recommendation: return available slots in arrival order (id ascending).
        var available = query.facilityId() == null
                ? parkingSlotRepository.findByStatus(SlotStatus.AVAILABLE)
                : parkingSlotRepository.findByFacilityIdAndStatus(new FacilityId(query.facilityId()), SlotStatus.AVAILABLE);
        return available.stream().sorted((a, b) -> Long.compare(a.getId(), b.getId())).toList();
    }

    @Override
    public OccupancySummary handle(GetOccupancySummaryQuery query) {
        long total;
        long occupied;
        long available;
        if (query.facilityId() == null) {
            total = parkingSlotRepository.count();
            occupied = parkingSlotRepository.findByStatus(SlotStatus.OCCUPIED).size();
            available = parkingSlotRepository.findByStatus(SlotStatus.AVAILABLE).size();
        } else {
            var fid = new FacilityId(query.facilityId());
            total = parkingSlotRepository.countByFacilityId(fid);
            occupied = parkingSlotRepository.countByFacilityIdAndStatus(fid, SlotStatus.OCCUPIED);
            available = parkingSlotRepository.countByFacilityIdAndStatus(fid, SlotStatus.AVAILABLE);
        }
        return new OccupancySummary(total, available, occupied,
                occupancyCalculationService.calculateOccupancyRate(total, occupied));
    }
}
