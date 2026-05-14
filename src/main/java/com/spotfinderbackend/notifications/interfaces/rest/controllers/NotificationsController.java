package com.spotfinderbackend.notifications.interfaces.rest.controllers;

import com.spotfinderbackend.notifications.domain.model.commands.MarkNotificationAsReadCommand;
import com.spotfinderbackend.notifications.domain.model.queries.*;
import com.spotfinderbackend.notifications.domain.services.NotificationCommandService;
import com.spotfinderbackend.notifications.domain.services.NotificationQueryService;
import com.spotfinderbackend.notifications.interfaces.rest.resources.*;
import com.spotfinderbackend.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "User notifications")
public class NotificationsController {

    private final NotificationCommandService commandService;
    private final NotificationQueryService queryService;

    public NotificationsController(NotificationCommandService commandService, NotificationQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Send a notification to a user")
    @PostMapping
    public ResponseEntity<NotificationResource> send(@RequestBody SendNotificationResource resource) {
        var notification = commandService.handle(NotificationResourceFromEntityAssembler.toCommand(resource));
        return notification
                .map(n -> new ResponseEntity<>(NotificationResourceFromEntityAssembler.toResourceFromEntity(n), HttpStatus.CREATED))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @Operation(summary = "List notifications of a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResource>> getByUser(@PathVariable Long userId) {
        var list = queryService.handle(new GetNotificationsByUserQuery(userId));
        return ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourcesFromEntities(list));
    }

    @Operation(summary = "List unread notifications of a user")
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResource>> getUnread(@PathVariable Long userId) {
        var list = queryService.handle(new GetUnreadNotificationsQuery(userId));
        return ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourcesFromEntities(list));
    }

    @Operation(summary = "Mark a notification as read")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        commandService.handle(new MarkNotificationAsReadCommand(id));
        return ResponseEntity.noContent().build();
    }
}
