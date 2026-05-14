package com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.notifications.domain.model.aggregates.Notification;
import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId_ValueOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserId_ValueAndStatusNot(Long userId, NotificationStatus status);

    int countByUserId_ValueAndStatusNot(Long userId, NotificationStatus status);
}
