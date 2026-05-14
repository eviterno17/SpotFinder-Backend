package com.spotfinderbackend.notifications.infrastructure.fcm;

import com.spotfinderbackend.notifications.domain.services.PushMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Firebase Cloud Messaging client.
 * <p>
 * Production wiring should use the Firebase Admin SDK to send a {@code Message}
 * (single) or {@code MulticastMessage} (batch). Until the SDK + service account
 * are provisioned, this client logs the payload and returns success.
 */
@Service
public class FirebaseMessagingClient implements PushMessagingService {

    private static final Logger LOG = LoggerFactory.getLogger(FirebaseMessagingClient.class);

    private final String serviceAccountPath;

    public FirebaseMessagingClient(@Value("${firebase.service-account-path:}") String serviceAccountPath) {
        this.serviceAccountPath = serviceAccountPath;
    }

    @Override
    public boolean sendPushNotification(String fcmToken, String title, String body, Map<String, String> data) {
        if (fcmToken == null || fcmToken.isBlank()) return false;
        if (serviceAccountPath == null || serviceAccountPath.isBlank()) {
            LOG.warn("Firebase service account not configured; simulating push to {}: '{}' / '{}'", fcmToken, title, body);
            return true;
        }
        // TODO: real wiring using firebase-admin (Message.builder().setToken(fcmToken)…).
        LOG.info("FCM push placeholder to={}, title={}", fcmToken, title);
        return true;
    }

    @Override
    public int sendBulkPushNotifications(List<String> fcmTokens, String title, String body, Map<String, String> data) {
        if (fcmTokens == null || fcmTokens.isEmpty()) return 0;
        int success = 0;
        for (var token : fcmTokens) {
            if (sendPushNotification(token, title, body, data)) success++;
        }
        return success;
    }
}
