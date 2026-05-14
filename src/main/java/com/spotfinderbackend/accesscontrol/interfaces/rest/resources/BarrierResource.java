package com.spotfinderbackend.accesscontrol.interfaces.rest.resources;

public record BarrierResource(Long id, String barrierCode, String position, String status, Long facilityId) { }
