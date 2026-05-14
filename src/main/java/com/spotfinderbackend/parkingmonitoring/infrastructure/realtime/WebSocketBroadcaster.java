package com.spotfinderbackend.parkingmonitoring.infrastructure.realtime;

import com.spotfinderbackend.parkingmonitoring.domain.model.valueobjects.SlotStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Sends real-time updates to subscribed clients (mobile app & dashboard) via WebSocket/STOMP.
 * <p>
 * Channel design:
 * <ul>
 *   <li>{@code /topic/slots} — per-slot status changes</li>
 *   <li>{@code /topic/occupancy} — aggregated occupancy summary</li>
 * </ul>
 */
@Component
public class WebSocketBroadcaster {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketBroadcaster.class);

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired(required = false)
    public WebSocketBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastSlotUpdate(Long slotId, SlotStatus status, String slotCode) {
        var payload = Map.of(
                "slotId", slotId,
                "slotCode", slotCode,
                "status", status.name(),
                "updatedAt", LocalDateTime.now().toString()
        );
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/slots", payload);
        }
        LOG.debug("Broadcast slot update {}", payload);
    }

    public void broadcastOccupancySummary(long total, long available, long occupied) {
        var payload = Map.of(
                "total", total,
                "available", available,
                "occupied", occupied,
                "occupancyRate", total == 0 ? 0.0 : (double) occupied / total,
                "updatedAt", LocalDateTime.now().toString()
        );
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/occupancy", payload);
        }
        LOG.debug("Broadcast occupancy {}", payload);
    }
}
