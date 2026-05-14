package com.spotfinderbackend.parkingmonitoring.application.internal.eventhandlers;

import com.spotfinderbackend.parkingmonitoring.domain.model.events.SlotStatusChangedEvent;
import com.spotfinderbackend.parkingmonitoring.domain.model.queries.GetOccupancySummaryQuery;
import com.spotfinderbackend.parkingmonitoring.domain.services.ParkingSlotQueryService;
import com.spotfinderbackend.parkingmonitoring.infrastructure.realtime.WebSocketBroadcaster;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SlotStatusChangedEventHandler {

    private final WebSocketBroadcaster broadcaster;
    private final ParkingSlotQueryService parkingSlotQueryService;

    public SlotStatusChangedEventHandler(WebSocketBroadcaster broadcaster,
                                         ParkingSlotQueryService parkingSlotQueryService) {
        this.broadcaster = broadcaster;
        this.parkingSlotQueryService = parkingSlotQueryService;
    }

    @EventListener
    public void on(SlotStatusChangedEvent event) {
        broadcaster.broadcastSlotUpdate(event.getSlotId(), event.getNewStatus(), event.getSlotCode());

        var summary = parkingSlotQueryService.handle(new GetOccupancySummaryQuery(null));
        broadcaster.broadcastOccupancySummary(summary.total(), summary.available(), summary.occupied());
    }
}
