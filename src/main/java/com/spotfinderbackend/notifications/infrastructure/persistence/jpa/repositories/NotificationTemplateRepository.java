package com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.notifications.domain.model.entities.NotificationTemplate;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByType(NotificationType type);

    Optional<NotificationTemplate> findByTypeAndLanguage(NotificationType type, String language);
}
