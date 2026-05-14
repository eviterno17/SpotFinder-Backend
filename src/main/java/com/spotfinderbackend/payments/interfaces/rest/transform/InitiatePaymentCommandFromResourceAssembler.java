package com.spotfinderbackend.payments.interfaces.rest.transform;

import com.spotfinderbackend.payments.domain.model.commands.InitiatePaymentCommand;
import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentMethod;
import com.spotfinderbackend.payments.interfaces.rest.resources.InitiatePaymentResource;

public class InitiatePaymentCommandFromResourceAssembler {
    public static InitiatePaymentCommand toCommandFromResource(InitiatePaymentResource r) {
        return new InitiatePaymentCommand(r.sessionId(),
                PaymentMethod.valueOf(r.paymentMethod().toUpperCase()), r.token());
    }
}
