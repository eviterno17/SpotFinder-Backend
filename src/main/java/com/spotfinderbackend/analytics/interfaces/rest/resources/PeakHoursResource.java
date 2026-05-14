package com.spotfinderbackend.analytics.interfaces.rest.resources;

import java.util.List;
import java.util.Map;

public record PeakHoursResource(List<Integer> peakHours, Map<Integer, Double> occupancyByHour, String busiestDay) { }
