package com.spotfinderbackend.analytics.domain.model.valueobjects;

import java.util.List;
import java.util.Map;

public record PeakHoursData(List<Integer> peakHours,
                            Map<Integer, Double> occupancyByHour,
                            String busiestDay) { }
