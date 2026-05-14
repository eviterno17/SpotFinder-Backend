package com.spotfinderbackend.payments.application.internal.queryservices;

import com.spotfinderbackend.payments.domain.model.aggregates.Payment;
import com.spotfinderbackend.payments.domain.model.queries.*;
import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentTransactionStatus;
import com.spotfinderbackend.payments.domain.model.valueobjects.SessionId;
import com.spotfinderbackend.payments.domain.services.PaymentQueryService;
import com.spotfinderbackend.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.paymentId());
    }

    @Override
    public Optional<Payment> handle(GetPaymentBySessionIdQuery query) {
        return paymentRepository.findBySessionId(new SessionId(query.sessionId()));
    }

    @Override
    public List<Payment> handle(GetPaymentHistoryQuery query) {
        return paymentRepository.findByUserIdOrderByPaidAtDesc(query.userId());
    }

    @Override
    public Optional<PaymentTransactionStatus> handle(GetPaymentStatusQuery query) {
        return paymentRepository.findBySessionId(new SessionId(query.sessionId())).map(Payment::getStatus);
    }
}
