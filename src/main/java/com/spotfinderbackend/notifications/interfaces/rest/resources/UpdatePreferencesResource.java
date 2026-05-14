package com.spotfinderbackend.notifications.interfaces.rest.resources;

import java.util.List;

public record UpdatePreferencesResource(List<NotificationPreferenceResource> preferences) { }
