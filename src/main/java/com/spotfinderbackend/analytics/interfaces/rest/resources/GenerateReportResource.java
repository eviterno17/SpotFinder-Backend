package com.spotfinderbackend.analytics.interfaces.rest.resources;

public record GenerateReportResource(String reportType, String startDate, String endDate,
                                     Long generatedBy, Long facilityId) { }
