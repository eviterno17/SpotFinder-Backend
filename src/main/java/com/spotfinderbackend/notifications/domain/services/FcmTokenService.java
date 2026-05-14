package com.spotfinderbackend.notifications.domain.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FcmTokenService {
    Optional<String> getFcmToken(Long userId);
    Map<Long, String> getFcmTokens(List<Long> userIds);
}
