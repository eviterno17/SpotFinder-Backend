package com.spotfinderbackend.analytics.infrastructure.pdf;

import com.spotfinderbackend.analytics.domain.model.valueobjects.OccupancyMetrics;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportPeriod;
import com.spotfinderbackend.analytics.domain.model.valueobjects.RevenueMetrics;
import com.spotfinderbackend.analytics.domain.services.PdfGenerationService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Minimal PDF-shaped report generator.
 * <p>
 * Produces a deterministic byte stream with a {@code %PDF-1.4} header followed
 * by a plain-text body. This keeps the pipeline functional without taking on a
 * heavyweight dependency (iText, JasperReports, PDFBox). Swap with a real
 * implementation when the PDF dependency is added.
 */
@Service
public class SimplePdfGenerationService implements PdfGenerationService {

    @Override
    public byte[] generateOccupancyReport(OccupancyMetrics metrics, ReportPeriod period) {
        StringBuilder sb = pdfHeader("OCCUPANCY REPORT");
        sb.append("Period: ").append(period.startDate()).append(" - ").append(period.endDate()).append("\n");
        sb.append("Average occupancy rate: ").append(metrics.averageOccupancyRate()).append("\n");
        sb.append("Turnover rate: ").append(metrics.turnoverRate()).append("\n");
        sb.append("Peak hours: ").append(metrics.peakHours()).append("\n");
        sb.append("Total slots: ").append(metrics.totalSlots()).append("\n");
        sb.append("Data by hour: ").append(metrics.dataByHour()).append("\n");
        return finalize(sb);
    }

    @Override
    public byte[] generateRevenueReport(RevenueMetrics metrics, ReportPeriod period) {
        StringBuilder sb = pdfHeader("REVENUE REPORT");
        sb.append("Period: ").append(period.startDate()).append(" - ").append(period.endDate()).append("\n");
        sb.append("Total revenue: ").append(metrics.currency()).append(" ").append(metrics.totalRevenue()).append("\n");
        sb.append("Average ticket: ").append(metrics.averageTicket()).append("\n");
        sb.append("Total transactions: ").append(metrics.totalTransactions()).append("\n");
        sb.append("By method: ").append(metrics.paymentsByMethod()).append("\n");
        sb.append("By day: ").append(metrics.dataByDay()).append("\n");
        return finalize(sb);
    }

    @Override
    public byte[] generateCombinedReport(OccupancyMetrics occupancy, RevenueMetrics revenue, ReportPeriod period) {
        StringBuilder sb = pdfHeader("COMBINED REPORT");
        sb.append("=== OCCUPANCY ===\n");
        sb.append("avgRate=").append(occupancy.averageOccupancyRate())
                .append(" turnover=").append(occupancy.turnoverRate()).append("\n");
        sb.append("=== REVENUE ===\n");
        sb.append("total=").append(revenue.totalRevenue())
                .append(" avgTicket=").append(revenue.averageTicket()).append("\n");
        return finalize(sb);
    }

    private StringBuilder pdfHeader(String title) {
        return new StringBuilder()
                .append("%PDF-1.4\n")
                .append("% SpotFinder ").append(title).append("\n");
    }

    private byte[] finalize(StringBuilder sb) {
        sb.append("%%EOF\n");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
