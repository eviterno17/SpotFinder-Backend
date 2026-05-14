package com.spotfinderbackend.analytics.application.internal.outboundservices.acl;

import com.spotfinderbackend.analytics.domain.model.valueobjects.PaymentSummary;
import com.spotfinderbackend.payments.interfaces.acl.PaymentContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("analyticsExternalPaymentDataService")
public class ExternalPaymentDataService {

    private final PaymentContextFacade facade;

    public ExternalPaymentDataService(PaymentContextFacade facade) {
        this.facade = facade;
    }

    public List<PaymentSummary> getPaymentSummaries(LocalDateTime start, LocalDateTime end) {
        return facade.getCompletedPaymentSummaries(start, end).stream()
                .map(s -> new PaymentSummary(s.paymentId(), s.amount(), s.paymentMethod(), s.paidAt()))
                .toList();
    }
}
