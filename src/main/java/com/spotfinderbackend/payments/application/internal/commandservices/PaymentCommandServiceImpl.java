package com.spotfinderbackend.payments.application.internal.commandservices;

import com.spotfinderbackend.payments.application.internal.outboundservices.acl.ExternalAccessControlService;
import com.spotfinderbackend.payments.domain.model.aggregates.Payment;
import com.spotfinderbackend.payments.domain.model.commands.CalculateFeeCommand;
import com.spotfinderbackend.payments.domain.model.commands.InitiatePaymentCommand;
import com.spotfinderbackend.payments.domain.model.valueobjects.ParkingFee;
import com.spotfinderbackend.payments.domain.services.FeeCalculationService;
import com.spotfinderbackend.payments.domain.services.PaymentCommandService;
import com.spotfinderbackend.payments.domain.services.PaymentGatewayService;
import com.spotfinderbackend.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.spotfinderbackend.shared.domain.model.exceptions.ConflictException;
import com.spotfinderbackend.shared.domain.model.exceptions.NotFoundException;
import com.spotfinderbackend.shared.domain.model.valueobjects.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final FeeCalculationService feeCalculationService;
    private final ExternalAccessControlService externalAccessControlService;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository,
                                     PaymentGatewayService paymentGatewayService,
                                     FeeCalculationService feeCalculationService,
                                     ExternalAccessControlService externalAccessControlService) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayService = paymentGatewayService;
        this.feeCalculationService = feeCalculationService;
        this.externalAccessControlService = externalAccessControlService;
    }

    @Override
    public Optional<Payment> handle(InitiatePaymentCommand command) {
        var sessionDetails = externalAccessControlService.getSessionDetails(command.sessionId())
                .orElseThrow(() -> new NotFoundException("Session not found: " + command.sessionId()));

        var existing = paymentRepository.findBySessionId_ValueAndStatus(command.sessionId(),
                com.spotfinderbackend.payments.domain.model.valueobjects.PaymentTransactionStatus.COMPLETED);
        if (existing.isPresent())
            throw new ConflictException("Session already paid");

        ParkingFee fee = feeCalculationService.calculateFee(
                sessionDetails.entryTimestamp(),
                LocalDateTime.now(),
                feeCalculationService.getDefaultRatePerHour()
        );

        var payment = new Payment(command.sessionId(), new Money(fee.amount()),
                command.paymentMethod(), fee, sessionDetails.userId());
        payment = paymentRepository.save(payment);

        var response = paymentGatewayService.processPayment(
                fee.amount(), Money.DEFAULT_CURRENCY, command.token(), command.paymentMethod());

        if (response.success()) {
            payment.markAsCompleted(response.transactionId(), response.receiptUrl());
        } else {
            payment.markAsFailed(response.errorDetail());
        }
        return Optional.of(paymentRepository.save(payment));
    }

    @Override
    public ParkingFee handle(CalculateFeeCommand command) {
        LocalDateTime entry = command.entryTimestamp();
        LocalDateTime exit = command.exitTimestamp();

        if (entry == null) {
            var session = externalAccessControlService.getSessionDetails(command.sessionId())
                    .orElseThrow(() -> new NotFoundException("Session not found: " + command.sessionId()));
            entry = session.entryTimestamp();
        }
        return feeCalculationService.calculateFee(entry,
                exit == null ? LocalDateTime.now() : exit,
                feeCalculationService.getDefaultRatePerHour());
    }
}
