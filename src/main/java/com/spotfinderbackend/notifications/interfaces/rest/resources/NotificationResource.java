package com.spotfinderbackend.notifications.interfaces.rest.resources;

import java.time.LocalDateTime;

public record NotificationResource(Long id, Long userId, String type, String title, String body,
                                   String status, String channel, LocalDateTime createdAt,
                                   LocalDateTime readAt) { }
