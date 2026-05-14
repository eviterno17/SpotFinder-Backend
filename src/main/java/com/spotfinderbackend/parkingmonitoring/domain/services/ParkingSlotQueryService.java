package com.spotfinderbackend.parkingmonitoring.domain.services;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingSlot;
import com.spotfinderbackend.parkingmonitoring.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotQueryService {
    List<ParkingSlot> handle(GetAllSlotsQuery query);
    List<ParkingSlot> handle(GetAvailableSlotsQuery query);
    Optional<ParkingSlot> handle(GetSlotByIdQuery query);
    List<ParkingSlot> handle(GetSlotRecommendationsQuery query);
    OccupancySummary handle(GetOccupancySummaryQuery query);

    record OccupancySummary(long total, long available, long occupied, double occupancyRate) { }
}
