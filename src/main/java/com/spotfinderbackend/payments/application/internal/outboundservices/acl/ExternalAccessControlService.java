package com.spotfinderbackend.payments.application.internal.outboundservices.acl;

import com.spotfinderbackend.accesscontrol.interfaces.acl.AccessControlContextFacade;
import com.spotfinderbackend.accesscontrol.interfaces.acl.AccessControlContextFacade.SessionDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("paymentsExternalAccessControlService")
public class ExternalAccessControlService {

    private final AccessControlContextFacade accessControlFacade;

    public ExternalAccessControlService(AccessControlContextFacade accessControlFacade) {
        this.accessControlFacade = accessControlFacade;
    }

    public Optional<SessionDetails> getSessionDetails(Long sessionId) {
        return accessControlFacade.findSessionDetails(sessionId);
    }

    public void markSessionAsPaid(Long sessionId) {
        accessControlFacade.markSessionAsPaid(sessionId);
    }
}
