package com.spotfinderbackend.analytics.domain.model.events;

import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReportGeneratedEvent extends ApplicationEvent {
    private final Long reportId;
    private final ReportType reportType;
    private final String fileUrl;
    private final Long generatedBy;

    public ReportGeneratedEvent(Object source, Long reportId, ReportType reportType, String fileUrl, Long generatedBy) {
        super(source);
        this.reportId = reportId;
        this.reportType = reportType;
        this.fileUrl = fileUrl;
        this.generatedBy = generatedBy;
    }
}
