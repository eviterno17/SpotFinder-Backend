package com.spotfinderbackend.analytics.domain.services;

import com.spotfinderbackend.analytics.domain.model.valueobjects.SlotStatusSnapshot;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OccupancyAnalyticsService {

    public double calculateAverageOccupancyRate(List<SlotStatusSnapshot> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) return 0.0;
        long occupied = snapshots.stream().filter(s -> "OCCUPIED".equals(s.status())).count();
        return (double) occupied / snapshots.size();
    }

    public double calculateTurnoverRate(long totalSessions, long totalSlots, int days) {
        if (totalSlots <= 0 || days <= 0) return 0.0;
        return (double) totalSessions / (double) (totalSlots * days);
    }

    public List<Integer> identifyPeakHours(List<SlotStatusSnapshot> snapshots) {
        if (snapshots == null || snapshots.isEmpty()) return List.of();
        Map<Integer, long[]> byHour = new HashMap<>();
        for (var s : snapshots) {
            int hour = s.timestamp().getHour();
            byHour.computeIfAbsent(hour, k -> new long[2]);
            byHour.get(hour)[0]++; // total
            if ("OCCUPIED".equals(s.status())) byHour.get(hour)[1]++;
        }
        return byHour.entrySet().stream()
                .filter(e -> e.getValue()[0] > 0)
                .sorted((a, b) -> Double.compare(
                        (double) b.getValue()[1] / b.getValue()[0],
                        (double) a.getValue()[1] / a.getValue()[0]))
                .limit(4)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    public Map<Integer, Double> calculateOccupancyByHour(List<SlotStatusSnapshot> snapshots) {
        if (snapshots == null) return Map.of();
        Map<Integer, long[]> byHour = new HashMap<>();
        for (var s : snapshots) {
            int hour = s.timestamp().getHour();
            byHour.computeIfAbsent(hour, k -> new long[2]);
            byHour.get(hour)[0]++;
            if ("OCCUPIED".equals(s.status())) byHour.get(hour)[1]++;
        }
        Map<Integer, Double> result = new TreeMap<>();
        byHour.forEach((h, counts) -> result.put(h, counts[0] == 0 ? 0.0 : (double) counts[1] / counts[0]));
        return result;
    }
}
