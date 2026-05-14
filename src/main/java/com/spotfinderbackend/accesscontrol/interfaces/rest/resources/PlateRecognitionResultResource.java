package com.spotfinderbackend.accesscontrol.interfaces.rest.resources;

public record PlateRecognitionResultResource(String licensePlate, double confidence, boolean isHighConfidence) { }
