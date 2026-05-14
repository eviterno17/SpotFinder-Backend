package com.spotfinderbackend.notifications.domain.services;

import java.util.List;
import java.util.Map;

public interface PushMessagingService {
    boolean sendPushNotification(String fcmToken, String title, String body, Map<String, String> data);
    int sendBulkPushNotifications(List<String> fcmTokens, String title, String body, Map<String, String> data);
}
