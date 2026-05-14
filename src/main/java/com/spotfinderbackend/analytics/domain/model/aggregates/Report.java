package com.spotfinderbackend.analytics.domain.model.aggregates;

import com.spotfinderbackend.analytics.domain.model.commands.GenerateReportCommand;
import com.spotfinderbackend.analytics.domain.model.events.ReportGeneratedEvent;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportPeriod;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportStatus;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportType;
import com.spotfinderbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Report extends AuditableAbstractAggregateRoot<Report> {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportType reportType;

    @Embedded
    private ReportPeriod reportPeriod;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column
    private Long generatedBy;

    @Column(length = 500)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ReportStatus status;

    @Column
    private Long facilityId;

    protected Report() {}

    public Report(GenerateReportCommand command) {
        this.reportType = command.reportType();
        this.reportPeriod = new ReportPeriod(command.startDate(), command.endDate());
        this.generatedAt = LocalDateTime.now();
        this.generatedBy = command.generatedBy();
        this.facilityId = command.facilityId();
        this.status = ReportStatus.GENERATING;
        this.fileUrl = null;
    }

    public void markAsCompleted(String fileUrl) {
        this.status = ReportStatus.COMPLETED;
        this.fileUrl = fileUrl;
        registerEvent(new ReportGeneratedEvent(this, getId(), reportType, fileUrl, generatedBy));
    }

    public void markAsFailed(String errorDetail) {
        this.status = ReportStatus.FAILED;
    }

    public boolean isCompleted() { return this.status == ReportStatus.COMPLETED; }
}
