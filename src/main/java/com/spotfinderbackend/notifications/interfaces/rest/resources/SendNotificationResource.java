package com.spotfinderbackend.notifications.interfaces.rest.resources;

import java.util.Map;

public record SendNotificationResource(Long userId, String type, String title, String body, Map<String, String> data) { }
