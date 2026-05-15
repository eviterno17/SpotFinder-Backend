package com.spotfinderbackend.iam.interfaces.rest.resources;

/** Body of `POST /api/v1/users/{userId}/change-password`. */
public record ChangePasswordResource(String currentPassword, String newPassword) { }
