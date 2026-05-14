package com.spotfinderbackend.payments.interfaces.acl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ACL facade exposed by Payment Processing BC.
 * <p>Consumed by Analytics BC to compute revenue metrics.</p>
 */
public interface PaymentContextFacade {

    record PaymentSummary(Long paymentId, BigDecimal amount, String paymentMethod, LocalDateTime paidAt) { }

    /** Returns completed payments in the given time window. */
    List<PaymentSummary> getCompletedPaymentSummaries(LocalDateTime start, LocalDateTime end);
}
