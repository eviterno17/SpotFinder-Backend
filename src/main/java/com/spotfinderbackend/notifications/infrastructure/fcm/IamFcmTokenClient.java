package com.spotfinderbackend.notifications.infrastructure.fcm;

import com.spotfinderbackend.iam.interfaces.acl.IamContextFacade;
import com.spotfinderbackend.notifications.domain.services.FcmTokenService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IamFcmTokenClient implements FcmTokenService {

    private final IamContextFacade iamContextFacade;

    public IamFcmTokenClient(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    @Override
    public Optional<String> getFcmToken(Long userId) {
        return iamContextFacade.findFcmTokenByUserId(userId);
    }

    @Override
    public Map<Long, String> getFcmTokens(List<Long> userIds) {
        Map<Long, String> result = new HashMap<>();
        if (userIds == null) return result;
        for (Long id : userIds) {
            iamContextFacade.findFcmTokenByUserId(id).ifPresent(token -> result.put(id, token));
        }
        return result;
    }
}
