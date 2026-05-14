package com.spotfinderbackend.emergency.application.internal.outboundservices.acl;

import com.spotfinderbackend.parkingmonitoring.interfaces.acl.ParkingMonitoringContextFacade;
import org.springframework.stereotype.Service;

@Service("emergencyExternalParkingMonitoringService")
public class ExternalParkingMonitoringService {

    private final ParkingMonitoringContextFacade facade;

    public ExternalParkingMonitoringService(ParkingMonitoringContextFacade facade) {
        this.facade = facade;
    }

    public void setAllLedsToEvacuationMode() {
        facade.setAllLedsToEvacuationMode();
    }

    public void restoreLedsToNormalMode() {
        facade.restoreLedsToNormalMode();
    }
}
