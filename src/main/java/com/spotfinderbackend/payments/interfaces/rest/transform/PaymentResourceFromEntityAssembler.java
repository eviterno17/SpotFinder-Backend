package com.spotfinderbackend.payments.interfaces.rest.transform;

import com.spotfinderbackend.payments.domain.model.aggregates.Payment;
import com.spotfinderbackend.payments.interfaces.rest.resources.PaymentResource;
import com.spotfinderbackend.payments.interfaces.rest.resources.ParkingFeeResource;
import com.spotfinderbackend.payments.domain.model.valueobjects.ParkingFee;
import com.spotfinderbackend.shared.domain.model.valueobjects.Money;

import java.util.Collection;
import java.util.List;

public class PaymentResourceFromEntityAssembler {

    public static PaymentResource toResourceFromEntity(Payment p) {
        var fee = p.getParkingFee();
        return new PaymentResource(
                p.getId(),
                p.getSessionId() == null ? null : p.getSessionId().value(),
                p.getAmount() == null ? null : p.getAmount().amount(),
                p.getAmount() == null ? Money.DEFAULT_CURRENCY : p.getAmount().currencyCode(),
                p.getPaymentMethod() == null ? null : p.getPaymentMethod().name(),
                p.getStatus().name(),
                p.getTransactionId(),
                p.getReceiptUrl(),
                p.getPaidAt(),
                fee == null ? null : fee.getFormattedDuration(),
                fee == null ? 0 : fee.hoursCharged()
        );
    }

    public static List<PaymentResource> toResourcesFromEntities(Collection<Payment> entities) {
        return entities.stream().map(PaymentResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    public static ParkingFeeResource toResourceFromFee(ParkingFee fee) {
        return new ParkingFeeResource(fee.amount(), fee.getFormattedDuration(),
                fee.ratePerHour(), fee.hoursCharged(), Money.DEFAULT_CURRENCY);
    }
}
