package com.spotfinderbackend.payments.domain.services;

import com.spotfinderbackend.payments.domain.model.aggregates.Payment;
import com.spotfinderbackend.payments.domain.model.queries.*;
import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentTransactionStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByIdQuery query);
    Optional<Payment> handle(GetPaymentBySessionIdQuery query);
    List<Payment> handle(GetPaymentHistoryQuery query);
    Optional<PaymentTransactionStatus> handle(GetPaymentStatusQuery query);
}
