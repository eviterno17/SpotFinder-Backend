package com.spotfinderbackend.emergency.interfaces.rest.resources;

public record TriggerAlertResource(String sensorId, int gasLevel, String type, String sensorLocation) { }
