package com.spotfinderbackend.emergency.domain.model.queries;

import java.time.LocalDate;

public record GetEmergencyHistoryQuery(LocalDate startDate, LocalDate endDate) { }
