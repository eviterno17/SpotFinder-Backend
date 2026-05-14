package com.spotfinderbackend.analytics.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ReportResource(Long id, String reportType, String periodStart, String periodEnd,
                             LocalDateTime generatedAt, String status, String fileUrl, Long facilityId) { }
