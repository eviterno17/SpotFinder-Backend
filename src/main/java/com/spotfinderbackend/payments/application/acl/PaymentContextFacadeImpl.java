package com.spotfinderbackend.payments.application.acl;

import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentTransactionStatus;
import com.spotfinderbackend.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.spotfinderbackend.payments.interfaces.acl.PaymentContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentContextFacadeImpl implements PaymentContextFacade {

    private final PaymentRepository paymentRepository;

    public PaymentContextFacadeImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentSummary> getCompletedPaymentSummaries(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.COMPLETED)
                .filter(p -> p.getPaidAt() != null)
                .filter(p -> (start == null || !p.getPaidAt().isBefore(start))
                        && (end == null || !p.getPaidAt().isAfter(end)))
                .map(p -> new PaymentSummary(p.getId(),
                        p.getAmount() == null ? null : p.getAmount().amount(),
                        p.getPaymentMethod() == null ? null : p.getPaymentMethod().name(),
                        p.getPaidAt()))
                .toList();
    }
}
