package com.spotfinderbackend.accesscontrol.application.internal.outboundservices.acl;

import com.spotfinderbackend.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("accessControlExternalIamService")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Looks up the user that owns the given plate. Returns empty if the plate
     * has not been registered by any user (vehicle entry will create an
     * "unidentified" session in that case).
     */
    public Optional<Long> findUserIdByLicensePlate(String licensePlate) {
        return iamContextFacade.findUserIdByLicensePlate(licensePlate);
    }

    public Optional<String> findEmailByUserId(Long userId) {
        return iamContextFacade.findEmailByUserId(userId);
    }
}
