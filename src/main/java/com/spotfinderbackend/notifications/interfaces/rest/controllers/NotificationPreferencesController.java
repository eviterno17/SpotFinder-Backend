package com.spotfinderbackend.notifications.interfaces.rest.controllers;

import com.spotfinderbackend.notifications.domain.model.queries.GetPreferencesByUserQuery;
import com.spotfinderbackend.notifications.domain.services.NotificationCommandService;
import com.spotfinderbackend.notifications.domain.services.NotificationQueryService;
import com.spotfinderbackend.notifications.interfaces.rest.resources.NotificationPreferenceResource;
import com.spotfinderbackend.notifications.interfaces.rest.resources.UpdatePreferencesResource;
import com.spotfinderbackend.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/notification-preferences", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notification Preferences", description = "Per-user notification preferences")
public class NotificationPreferencesController {

    private final NotificationCommandService commandService;
    private final NotificationQueryService queryService;

    public NotificationPreferencesController(NotificationCommandService commandService,
                                             NotificationQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Get user's preferences (defaults if none configured)")
    @GetMapping
    public ResponseEntity<List<NotificationPreferenceResource>> get(@PathVariable Long userId) {
        var prefs = queryService.handle(new GetPreferencesByUserQuery(userId));
        return ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourcesFromPreferences(prefs));
    }

    @Operation(summary = "Update user's preferences")
    @PutMapping
    public ResponseEntity<Void> update(@PathVariable Long userId, @RequestBody UpdatePreferencesResource resource) {
        commandService.handle(NotificationResourceFromEntityAssembler.toCommand(userId, resource));
        return ResponseEntity.noContent().build();
    }
}
