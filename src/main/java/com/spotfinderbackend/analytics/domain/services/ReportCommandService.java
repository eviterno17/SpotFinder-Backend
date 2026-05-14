package com.spotfinderbackend.analytics.domain.services;

import com.spotfinderbackend.analytics.domain.model.aggregates.Report;
import com.spotfinderbackend.analytics.domain.model.commands.GenerateReportCommand;

import java.util.Optional;

public interface ReportCommandService {
    Optional<Report> handle(GenerateReportCommand command);
}
