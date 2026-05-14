package com.spotfinderbackend.analytics.domain.services;

import com.spotfinderbackend.analytics.domain.model.aggregates.Report;
import com.spotfinderbackend.analytics.domain.model.queries.GetReportByIdQuery;
import com.spotfinderbackend.analytics.domain.model.queries.GetReportsByFacilityQuery;

import java.util.List;
import java.util.Optional;

public interface ReportQueryService {
    Optional<Report> handle(GetReportByIdQuery query);
    List<Report> handle(GetReportsByFacilityQuery query);
}
