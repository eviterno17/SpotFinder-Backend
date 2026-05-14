package com.spotfinderbackend.analytics.application.internal.outboundservices.acl;

import com.spotfinderbackend.accesscontrol.interfaces.acl.AccessControlContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("analyticsExternalSessionDataService")
public class ExternalSessionDataService {

    private final AccessControlContextFacade facade;

    public ExternalSessionDataService(AccessControlContextFacade facade) {
        this.facade = facade;
    }

    public long getCompletedSessionCount(LocalDateTime start, LocalDateTime end) {
        return facade.countCompletedSessionsBetween(start, end);
    }
}
