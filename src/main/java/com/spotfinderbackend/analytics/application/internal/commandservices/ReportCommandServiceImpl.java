package com.spotfinderbackend.analytics.application.internal.commandservices;

import com.spotfinderbackend.analytics.domain.model.aggregates.Report;
import com.spotfinderbackend.analytics.domain.model.commands.GenerateReportCommand;
import com.spotfinderbackend.analytics.domain.model.queries.GetOccupancyMetricsQuery;
import com.spotfinderbackend.analytics.domain.model.queries.GetRevenueMetricsQuery;
import com.spotfinderbackend.analytics.domain.model.valueobjects.OccupancyMetrics;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportPeriod;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportType;
import com.spotfinderbackend.analytics.domain.model.valueobjects.RevenueMetrics;
import com.spotfinderbackend.analytics.domain.services.AnalyticsQueryService;
import com.spotfinderbackend.analytics.domain.services.PdfGenerationService;
import com.spotfinderbackend.analytics.domain.services.ReportCommandService;
import com.spotfinderbackend.analytics.infrastructure.persistence.jpa.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Transactional
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final AnalyticsQueryService analyticsQueryService;
    private final PdfGenerationService pdfGenerationService;
    private final String storagePath;

    public ReportCommandServiceImpl(ReportRepository reportRepository,
                                    AnalyticsQueryService analyticsQueryService,
                                    PdfGenerationService pdfGenerationService,
                                    @Value("${spotfinder.reports.storage-path:./reports}") String storagePath) {
        this.reportRepository = reportRepository;
        this.analyticsQueryService = analyticsQueryService;
        this.pdfGenerationService = pdfGenerationService;
        this.storagePath = storagePath;
    }

    @Override
    public Optional<Report> handle(GenerateReportCommand command) {
        Report report = new Report(command);
        report = reportRepository.save(report);

        try {
            ReportPeriod period = new ReportPeriod(command.startDate(), command.endDate());
            byte[] pdf;
            switch (command.reportType()) {
                case OCCUPANCY -> {
                    OccupancyMetrics o = analyticsQueryService.handle(
                            new GetOccupancyMetricsQuery(command.startDate(), command.endDate(), command.facilityId()));
                    pdf = pdfGenerationService.generateOccupancyReport(o, period);
                }
                case REVENUE -> {
                    RevenueMetrics r = analyticsQueryService.handle(
                            new GetRevenueMetricsQuery(command.startDate(), command.endDate(), command.facilityId()));
                    pdf = pdfGenerationService.generateRevenueReport(r, period);
                }
                case COMBINED -> {
                    OccupancyMetrics o = analyticsQueryService.handle(
                            new GetOccupancyMetricsQuery(command.startDate(), command.endDate(), command.facilityId()));
                    RevenueMetrics r = analyticsQueryService.handle(
                            new GetRevenueMetricsQuery(command.startDate(), command.endDate(), command.facilityId()));
                    pdf = pdfGenerationService.generateCombinedReport(o, r, period);
                }
                default -> throw new IllegalStateException("Unknown report type: " + command.reportType());
            }

            Path dir = Paths.get(storagePath);
            Files.createDirectories(dir);
            String filename = "report-" + report.getId() + "-" + command.reportType().name().toLowerCase() + ".pdf";
            Path file = dir.resolve(filename);
            Files.write(file, pdf);
            report.markAsCompleted(file.toAbsolutePath().toString());
        } catch (IOException e) {
            report.markAsFailed("Storage error: " + e.getMessage());
        }
        return Optional.of(reportRepository.save(report));
    }
}
