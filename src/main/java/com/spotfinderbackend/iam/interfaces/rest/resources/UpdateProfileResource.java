package com.spotfinderbackend.iam.interfaces.rest.resources;

/** Body of `PUT /api/v1/users/{userId}`. */
public record UpdateProfileResource(String firstName, String lastName) { }
