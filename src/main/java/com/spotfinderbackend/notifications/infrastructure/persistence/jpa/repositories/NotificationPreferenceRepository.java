package com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.notifications.domain.model.entities.NotificationPreference;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    List<NotificationPreference> findByUserId_Value(Long userId);

    Optional<NotificationPreference> findByUserId_ValueAndNotificationType(Long userId, NotificationType type);

    boolean existsByUserId_Value(Long userId);
}
