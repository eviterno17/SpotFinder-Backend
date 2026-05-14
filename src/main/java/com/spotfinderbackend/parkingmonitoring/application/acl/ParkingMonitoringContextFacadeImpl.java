package com.spotfinderbackend.parkingmonitoring.application.acl;

import com.spotfinderbackend.parkingmonitoring.domain.model.commands.ReleaseSlotCommand;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.RestoreLedsToNormalModeCommand;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.SetAllLedsToEvacuationModeCommand;
import com.spotfinderbackend.parkingmonitoring.domain.model.queries.GetSlotByIdQuery;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.FacilityId;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotCommandService;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotQueryService;
import com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories.ParkingSlotRepository;
import com.spotfinderbackend.parkingmonitoring.interfaces.acl.ParkingMonitoringContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingMonitoringContextFacadeImpl implements ParkingMonitoringContextFacade {

    private final ParkingSlotCommandService commandService;
    private final ParkingSlotQueryService queryService;
    private final ParkingSlotRepository slotRepository;

    public ParkingMonitoringContextFacadeImpl(ParkingSlotCommandService commandService,
                                              ParkingSlotQueryService queryService,
                                              ParkingSlotRepository slotRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.slotRepository = slotRepository;
    }

    @Override
    public void releaseSlot(Long slotId) {
        if (slotId == null) return;
        commandService.handle(new ReleaseSlotCommand(slotId));
    }

    @Override
    public void setAllLedsToEvacuationMode() {
        commandService.handle(new SetAllLedsToEvacuationModeCommand());
    }

    @Override
    public void restoreLedsToNormalMode() {
        commandService.handle(new RestoreLedsToNormalModeCommand());
    }

    @Override
    public Optional<SlotSnapshot> findSlot(Long slotId) {
        return queryService.handle(new GetSlotByIdQuery(slotId))
                .map(s -> new SlotSnapshot(s.getId(), s.getSlotCode().code(),
                        s.getStatus().name(), s.getLastUpdated()));
    }

    @Override
    public int countTotalSlots(Long facilityId) {
        if (facilityId == null) return (int) slotRepository.count();
        return (int) slotRepository.countByFacilityId(new FacilityId(facilityId));
    }

    @Override
    public List<SlotSnapshot> getSnapshotsBetween(LocalDateTime start, LocalDateTime end, Long facilityId) {
        // Minimal projection: use current state with lastUpdated as a coarse proxy.
        // A real implementation would query a dedicated snapshot table.
        var slots = (facilityId == null)
                ? slotRepository.findAll()
                : slotRepository.findByFacilityId(new FacilityId(facilityId));
        return slots.stream()
                .filter(s -> s.getLastUpdated() != null)
                .filter(s -> (start == null || !s.getLastUpdated().isBefore(start))
                        && (end == null || !s.getLastUpdated().isAfter(end)))
                .map(s -> new SlotSnapshot(s.getId(), s.getSlotCode().code(),
                        s.getStatus().name(), s.getLastUpdated()))
                .toList();
    }
}
