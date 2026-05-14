package com.spotfinderbackend.payments.interfaces.rest.resources;

import java.math.BigDecimal;

public record ParkingFeeResource(BigDecimal amount, String duration, BigDecimal ratePerHour,
                                 int hoursCharged, String currency) { }
