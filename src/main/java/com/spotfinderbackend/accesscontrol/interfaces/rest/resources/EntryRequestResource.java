package com.spotfinderbackend.accesscontrol.interfaces.rest.resources;

/**
 * @param imageData base64-encoded image of the license plate.
 */
public record EntryRequestResource(String imageData, String barrierCode) { }
