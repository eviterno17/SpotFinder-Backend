package com.spotfinderbackend.payments.domain.services;

import com.spotfinderbackend.payments.domain.model.aggregates.Payment;
import com.spotfinderbackend.payments.domain.model.commands.CalculateFeeCommand;
import com.spotfinderbackend.payments.domain.model.commands.InitiatePaymentCommand;
import com.spotfinderbackend.payments.domain.model.valueobjects.ParkingFee;

import java.util.Optional;

public interface PaymentCommandService {
    Optional<Payment> handle(InitiatePaymentCommand command);
    ParkingFee handle(CalculateFeeCommand command);
}
