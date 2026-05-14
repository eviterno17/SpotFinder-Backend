package com.spotfinderbackend.iam.interfaces.acl;

import java.util.Optional;

/**
 * Anti-Corruption Layer facade exposed by IAM Bounded Context.
 * <p>
 * Other BCs (Access Control, Payment Processing, Notification Management, Emergency, Analytics)
 * consume identity information through this stable interface, decoupling them from the
 * internal IAM model.
 * </p>
 * <p>
 * Matches the pattern described in the PDF §4.1.2 (OHS pattern from IAM upstream).
 * </p>
 */
public interface IamContextFacade {

    /**
     * @return userId if a user with that email exists, empty otherwise.
     */
    Optional<Long> findUserIdByEmail(String email);

    /**
     * @return userId if a user with that id exists and is active, empty otherwise.
     */
    Optional<Long> findActiveUserId(Long userId);

    /**
     * @return email address of the user with the given id, empty if not found.
     */
    Optional<String> findEmailByUserId(Long userId);

    /**
     * @return list of role names the user has (e.g. ["ADMIN"], ["CAR_OWNER"]).
     */
    java.util.List<String> findRolesByUserId(Long userId);

    /**
     * Returns the FCM token registered for the given user, if any.
     * Used by Notification Management BC to send push notifications.
     */
    Optional<String> findFcmTokenByUserId(Long userId);
}
