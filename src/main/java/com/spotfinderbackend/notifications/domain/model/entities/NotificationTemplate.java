package com.spotfinderbackend.notifications.domain.model.entities;

import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Map;

@Entity
@Table(name = "notification_templates")
@Getter
public class NotificationTemplate extends AuditableModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false, length = 200)
    private String titleTemplate;

    @Column(nullable = false, length = 1000)
    private String bodyTemplate;

    @Column(nullable = false, length = 10)
    private String language;

    protected NotificationTemplate() {}

    public NotificationTemplate(NotificationType type, String titleTemplate,
                                String bodyTemplate, String language) {
        this.type = type;
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.language = language;
    }

    public String resolveTitle(Map<String, String> data) {
        return resolve(titleTemplate, data);
    }

    public String resolveBody(Map<String, String> data) {
        return resolve(bodyTemplate, data);
    }

    private String resolve(String template, Map<String, String> data) {
        if (template == null) return "";
        if (data == null) return template;
        String out = template;
        for (var e : data.entrySet()) {
            out = out.replace("{{" + e.getKey() + "}}", e.getValue() == null ? "" : e.getValue());
        }
        return out;
    }
}
