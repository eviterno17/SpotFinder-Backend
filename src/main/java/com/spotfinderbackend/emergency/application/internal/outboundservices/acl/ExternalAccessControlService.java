package com.spotfinderbackend.emergency.application.internal.outboundservices.acl;

import com.spotfinderbackend.accesscontrol.interfaces.acl.AccessControlContextFacade;
import org.springframework.stereotype.Service;

@Service("emergencyExternalAccessControlService")
public class ExternalAccessControlService {

    private final AccessControlContextFacade accessControlFacade;

    public ExternalAccessControlService(AccessControlContextFacade accessControlFacade) {
        this.accessControlFacade = accessControlFacade;
    }

    public void openAllBarriers(String reason) {
        accessControlFacade.openAllBarriers(reason);
    }

    public java.util.List<Long> getActiveSessionUserIds() {
        return accessControlFacade.getActiveSessionUserIds();
    }
}
