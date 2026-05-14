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
     * Looks up a user by license plate. Vehicle-plate registration would normally
     * live in IAM (or a dedicated Vehicles BC). Until that is wired up we return
     * empty so the entry flow falls back to an "unidentified" session.
     */
    public Optional<Long> findUserIdByLicensePlate(String licensePlate) {
        // Placeholder: PDF mentions plate registration via IAM. To be wired
        // once user-vehicle profile endpoints are added to IAM.
        return Optional.empty();
    }

    public Optional<String> findEmailByUserId(Long userId) {
        return iamContextFacade.findEmailByUserId(userId);
    }
}
