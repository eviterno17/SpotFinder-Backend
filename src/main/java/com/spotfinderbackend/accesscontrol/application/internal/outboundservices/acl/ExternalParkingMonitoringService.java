package com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl;

import com.spotfinderbackend.parkingmonitoring.interfaces.acl.ParkingMonitoringContextFacade;
import org.springframework.stereotype.Service;

@Service("accessControlExternalParkingMonitoringService")
public class ExternalParkingMonitoringService {

    private final ParkingMonitoringContextFacade facade;

    public ExternalParkingMonitoringService(ParkingMonitoringContextFacade facade) {
        this.facade = facade;
    }

    public void releaseSlot(Long slotId) {
        facade.releaseSlot(slotId);
    }
}
