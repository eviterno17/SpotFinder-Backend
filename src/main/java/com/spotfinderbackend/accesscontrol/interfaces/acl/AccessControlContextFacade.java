package com.spotfinderbackend.accesscontrol.interfaces.acl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ACL facade exposed by Access Control BC.
 * <p>
 * Consumed by Payment Processing, Emergency, Analytics.
 */
public interface AccessControlContextFacade {

    Optional<SessionDetails> findSessionDetails(Long sessionId);

    /** Used by Payment Processing on successful payment. */
    void markSessionAsPaid(Long sessionId);

    /** Used by Emergency BC to broadcast users with active sessions. */
    List<Long> getActiveSessionUserIds();

    /** Used by Emergency BC. */
    void openAllBarriers(String reason);

    /** Used by Analytics BC. */
    long countCompletedSessionsBetween(LocalDateTime start, LocalDateTime end);

    record SessionDetails(Long sessionId, String licensePlate, Long userId,
                          LocalDateTime entryTimestamp, LocalDateTime exitTimestamp,
                          String paymentStatus, String sessionStatus, Long slotId) { }
}
