package com.spotfinderbackend.parkingmonitoring.application.internal.commandservices;

import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.ParkingSlot;
import com.spotfinderbackend.parkingmonitoring.domain.model.aggregates.SensorReading;
import com.spotfinderbackend.parkingmonitoring.domain.model.commands.*;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.FacilityId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SensorId;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotCode;
import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import com.spotfinderbackend.parkingmonitoring.domain.services.OccupancyCalculationService;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotCommandService;
import com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories.ParkingSlotRepository;
import com.spotfinderbackend.parkingmonitoring.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.BadRequestException;
import com.spotfinderbackend.shared.domain.model.exceptions.ConflictException;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ParkingSlotCommandServiceImpl implements ParkingSlotCommandService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final OccupancyCalculationService occupancyCalculationService;

    public ParkingSlotCommandServiceImpl(ParkingSlotRepository parkingSlotRepository,
                                         SensorReadingRepository sensorReadingRepository,
                                         OccupancyCalculationService occupancyCalculationService) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.occupancyCalculationService = occupancyCalculationService;
    }

    @Override
    public Optional<ParkingSlot> handle(RegisterParkingSlotCommand command) {
        var slotCode = new SlotCode(command.slotCode());
        if (parkingSlotRepository.existsBySlotCode(slotCode))
            throw new ConflictException("Slot code already exists: " + command.slotCode());

        if (command.sensorId() != null && !command.sensorId().isBlank()) {
            var sensorId = new SensorId(command.sensorId());
            if (parkingSlotRepository.existsBySensorId(sensorId))
                throw new ConflictException("Sensor already assigned: " + command.sensorId());
        }

        var slot = new ParkingSlot(command);
        return Optional.of(parkingSlotRepository.save(slot));
    }

    @Override
    public void handle(UpdateSlotStatusCommand command) {
        var slot = parkingSlotRepository.findById(command.slotId())
                .orElseThrow(() -> new NotFoundException("Slot not found: " + command.slotId()));

        switch (command.newStatus()) {
            case AVAILABLE -> slot.markActive();
            case OUT_OF_SERVICE -> slot.markOutOfService();
            case OCCUPIED -> slot.occupy();
            case EVACUATION -> slot.setEvacuationMode();
        }
        parkingSlotRepository.save(slot);
    }

    @Override
    public void handle(ProcessSensorReadingCommand command) {
        var sensorId = new SensorId(command.sensorId());
        var slotOpt = parkingSlotRepository.findBySensorId(sensorId);
        if (slotOpt.isEmpty()) {
            // Store reading anyway for traceability.
            sensorReadingRepository.save(new SensorReading(command.sensorId(), command.slotId(),
                    command.distance(), command.timestamp()));
            return;
        }
        var slot = slotOpt.get();
        sensorReadingRepository.save(new SensorReading(command.sensorId(), slot.getId(),
                command.distance(), command.timestamp()));

        var decision = occupancyCalculationService.evaluateSensorReading(command.distance(), slot.getStatus());
        if (decision.isEmpty()) return;

        slot.changeStatusFromSensor(decision.get());
        parkingSlotRepository.save(slot);
    }

    @Override
    public void handle(SetAllLedsToEvacuationModeCommand command) {
        parkingSlotRepository.findAll().forEach(slot -> {
            slot.setEvacuationMode();
            parkingSlotRepository.save(slot);
        });
    }

    @Override
    public void handle(RestoreLedsToNormalModeCommand command) {
        parkingSlotRepository.findAll().forEach(slot -> {
            slot.restoreFromEvacuation(SlotStatus.AVAILABLE);
            parkingSlotRepository.save(slot);
        });
    }

    @Override
    public void handle(ReleaseSlotCommand command) {
        var slot = parkingSlotRepository.findById(command.slotId())
                .orElseThrow(() -> new NotFoundException("Slot not found: " + command.slotId()));
        if (!slot.isOccupied()) {
            throw new BadRequestException("Slot is not currently occupied");
        }
        slot.release();
        parkingSlotRepository.save(slot);
    }
}
