package com.spotfinderbackend.analytics.application.internal.queryservices;

import com.spotfinderbackend.analytics.application.internal.outboundservices.acl.ExternalParkingDataService;
import com.spotfinderbackend.analytics.application.internal.outboundservices.acl.ExternalPaymentDataService;
import com.spotfinderbackend.analytics.application.internal.outboundservices.acl.ExternalSessionDataService;
import com.spotfinderbackend.analytics.domain.model.queries.*;
import com.spotfinderbackend.analytics.domain.model.valueobjects.*;
import com.spotfinderbackend.analytics.domain.services.AnalyticsQueryService;
import com.spotfinderbackend.analytics.domain.services.OccupancyAnalyticsService;
import com.spotfinderbackend.analytics.domain.services.RevenueAnalyticsService;
import com.spotfinderbackend.shared.domain.model.valueobjects.Money;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsQueryServiceImpl implements AnalyticsQueryService {

    private final OccupancyAnalyticsService occupancyAnalyticsService;
    private final RevenueAnalyticsService revenueAnalyticsService;
    private final ExternalParkingDataService externalParkingDataService;
    private final ExternalPaymentDataService externalPaymentDataService;
    private final ExternalSessionDataService externalSessionDataService;

    public AnalyticsQueryServiceImpl(OccupancyAnalyticsService occupancyAnalyticsService,
                                     RevenueAnalyticsService revenueAnalyticsService,
                                     ExternalParkingDataService externalParkingDataService,
                                     ExternalPaymentDataService externalPaymentDataService,
                                     ExternalSessionDataService externalSessionDataService) {
        this.occupancyAnalyticsService = occupancyAnalyticsService;
        this.revenueAnalyticsService = revenueAnalyticsService;
        this.externalParkingDataService = externalParkingDataService;
        this.externalPaymentDataService = externalPaymentDataService;
        this.externalSessionDataService = externalSessionDataService;
    }

    @Override
    public OccupancyMetrics handle(GetOccupancyMetricsQuery query) {
        LocalDate start = query.startDate() == null ? LocalDate.now().minusDays(7) : query.startDate();
        LocalDate end = query.endDate() == null ? LocalDate.now() : query.endDate();
        var snapshots = externalParkingDataService.getSlotStatusSnapshots(
                start.atStartOfDay(), end.atTime(LocalTime.MAX), query.facilityId());
        long sessions = externalSessionDataService.getCompletedSessionCount(
                start.atStartOfDay(), end.atTime(LocalTime.MAX));
        int totalSlots = externalParkingDataService.getTotalSlots(query.facilityId());
        int days = (int) (end.toEpochDay() - start.toEpochDay() + 1);

        return new OccupancyMetrics(
                occupancyAnalyticsService.calculateAverageOccupancyRate(snapshots),
                occupancyAnalyticsService.identifyPeakHours(snapshots),
                occupancyAnalyticsService.calculateTurnoverRate(sessions, totalSlots, days),
                occupancyAnalyticsService.calculateOccupancyByHour(snapshots),
                totalSlots, start, end
        );
    }

    @Override
    public RevenueMetrics handle(GetRevenueMetricsQuery query) {
        LocalDate start = query.startDate() == null ? LocalDate.now().minusDays(7) : query.startDate();
        LocalDate end = query.endDate() == null ? LocalDate.now() : query.endDate();
        var payments = externalPaymentDataService.getPaymentSummaries(start.atStartOfDay(), end.atTime(LocalTime.MAX));

        return new RevenueMetrics(
                revenueAnalyticsService.calculateTotalRevenue(payments),
                revenueAnalyticsService.calculateAverageTicket(payments),
                payments.size(),
                revenueAnalyticsService.groupByPaymentMethod(payments),
                revenueAnalyticsService.groupByDay(payments),
                Money.DEFAULT_CURRENCY
        );
    }

    @Override
    public List<HeatmapEntry> handle(GetHeatmapDataQuery query) {
        LocalDate start = query.startDate() == null ? LocalDate.now().minusDays(30) : query.startDate();
        LocalDate end = query.endDate() == null ? LocalDate.now() : query.endDate();
        var snapshots = externalParkingDataService.getSlotStatusSnapshots(
                start.atStartOfDay(), end.atTime(LocalTime.MAX), query.facilityId());

        // Group by slotId.
        Map<Long, List<SlotStatusSnapshot>> bySlot = snapshots.stream()
                .filter(s -> s.slotId() != null)
                .collect(Collectors.groupingBy(SlotStatusSnapshot::slotId));

        List<HeatmapEntry> entries = new ArrayList<>();
        bySlot.forEach((slotId, snaps) -> {
            int occupiedCount = (int) snaps.stream().filter(s -> "OCCUPIED".equals(s.status())).count();
            entries.add(new HeatmapEntry(slotId, "S-" + slotId,
                    occupiedCount,
                    snaps.isEmpty() ? 0.0 : (double) occupiedCount / snaps.size() * 100,
                    occupiedCount * 10L));
        });
        return entries;
    }

    @Override
    public PeakHoursData handle(GetPeakHoursQuery query) {
        LocalDate start = query.startDate() == null ? LocalDate.now().minusDays(7) : query.startDate();
        LocalDate end = query.endDate() == null ? LocalDate.now() : query.endDate();
        var snapshots = externalParkingDataService.getSlotStatusSnapshots(
                start.atStartOfDay(), end.atTime(LocalTime.MAX), query.facilityId());

        Map<Integer, Double> occByHour = occupancyAnalyticsService.calculateOccupancyByHour(snapshots);
        List<Integer> peakHours = occupancyAnalyticsService.identifyPeakHours(snapshots);

        Map<DayOfWeek, long[]> byDay = new EnumMap<>(DayOfWeek.class);
        for (var s : snapshots) {
            byDay.computeIfAbsent(s.timestamp().getDayOfWeek(), k -> new long[2]);
            byDay.get(s.timestamp().getDayOfWeek())[0]++;
            if ("OCCUPIED".equals(s.status())) byDay.get(s.timestamp().getDayOfWeek())[1]++;
        }
        String busiestDay = byDay.entrySet().stream()
                .max(Comparator.comparingDouble(e -> e.getValue()[0] == 0 ? 0 : (double) e.getValue()[1] / e.getValue()[0]))
                .map(e -> e.getKey().name())
                .orElse("UNKNOWN");

        return new PeakHoursData(peakHours, occByHour, busiestDay);
    }

    @SuppressWarnings("unused")
    private LocalDateTime ignored() { return null; }
}
