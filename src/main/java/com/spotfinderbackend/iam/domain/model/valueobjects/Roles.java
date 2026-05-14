package com.spotfinderbackend.iam.domain.model.valueobjects;

/**
 * Roles available in the SpotFinder system.
 * <ul>
 *   <li>{@code ADMIN} - Parking administrator (web dashboard, slot/emergency management).</li>
 *   <li>{@code CAR_OWNER} - Driver / vehicle owner (mobile app, payments, find-my-car).</li>
 * </ul>
 */
public enum Roles {
    ADMIN,
    CAR_OWNER
}
