package com.spotfinderbackend.notifications.interfaces.acl;

import java.util.List;
import java.util.Map;

/**
 * Anti-Corruption Layer facade exposed by Notification Management BC.
 * <p>
 * Other BCs (Access Control, Payment Processing, Emergency, Analytics) push
 * notifications through this stable, coarse-grained interface.
 */
public interface NotificationContextFacade {

    /** Send a notification to a single user. */
    void sendNotification(Long userId, String type, String title, String body, Map<String, String> data);

    /** Broadcast a notification to many users (used by Emergency). */
    void broadcastNotification(List<Long> userIds, String type, String title, String body, Map<String, String> data);

    /** Bootstrap default preferences for a newly registered user. */
    void initializeDefaultPreferencesFor(Long userId);
}
