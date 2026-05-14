package com.spotfinderbackend.analytics.application.internal.queryservices;

import com.spotfinderbackend.analytics.domain.model.aggregates.Report;
import com.spotfinderbackend.analytics.domain.model.queries.GetReportByIdQuery;
import com.spotfinderbackend.analytics.domain.model.queries.GetReportsByFacilityQuery;
import com.spotfinderbackend.analytics.domain.services.ReportQueryService;
import com.spotfinderbackend.analytics.infrastructure.persistence.jpa.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportRepository reportRepository;

    public ReportQueryServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Optional<Report> handle(GetReportByIdQuery query) {
        return reportRepository.findById(query.reportId());
    }

    @Override
    public List<Report> handle(GetReportsByFacilityQuery query) {
        if (query.facilityId() == null) return reportRepository.findAll();
        return reportRepository.findByFacilityIdOrderByGeneratedAtDesc(query.facilityId());
    }
}
