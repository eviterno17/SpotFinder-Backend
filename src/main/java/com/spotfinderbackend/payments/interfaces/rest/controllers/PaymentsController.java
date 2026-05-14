package com.spotfinderbackend.payments.interfaces.rest.controllers;

import com.spotfinderbackend.payments.domain.model.commands.CalculateFeeCommand;
import com.spotfinderbackend.payments.domain.model.queries.*;
import com.spotfinderbackend.payments.domain.services.PaymentCommandService;
import com.spotfinderbackend.payments.domain.services.PaymentQueryService;
import com.spotfinderbackend.payments.interfaces.rest.resources.*;
import com.spotfinderbackend.payments.interfaces.rest.transform.InitiatePaymentCommandFromResourceAssembler;
import com.spotfinderbackend.payments.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/payments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Parking payment endpoints (Culqi)")
public class PaymentsController {

    private final PaymentCommandService commandService;
    private final PaymentQueryService queryService;

    public PaymentsController(PaymentCommandService commandService, PaymentQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Initiate a payment for a session")
    @PostMapping
    public ResponseEntity<PaymentResource> initiate(@RequestBody InitiatePaymentResource resource) {
        var payment = commandService.handle(InitiatePaymentCommandFromResourceAssembler.toCommandFromResource(resource));
        return payment
                .map(p -> new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(p), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get a payment by id")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResource> getById(@PathVariable Long id) {
        return queryService.handle(new GetPaymentByIdQuery(id))
                .map(p -> ResponseEntity.ok(PaymentResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get the payment associated with a session")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<PaymentResource> getBySession(@PathVariable Long sessionId) {
        return queryService.handle(new GetPaymentBySessionIdQuery(sessionId))
                .map(p -> ResponseEntity.ok(PaymentResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List payment history for a user")
    @GetMapping("/history")
    public ResponseEntity<List<PaymentResource>> history(@RequestParam Long userId) {
        var payments = queryService.handle(new GetPaymentHistoryQuery(userId));
        return ResponseEntity.ok(PaymentResourceFromEntityAssembler.toResourcesFromEntities(payments));
    }

    @Operation(summary = "Calculate fee for an active session")
    @GetMapping("/calculate-fee/{sessionId}")
    public ResponseEntity<ParkingFeeResource> calculateFee(@PathVariable Long sessionId) {
        var fee = commandService.handle(new CalculateFeeCommand(sessionId, null, null));
        return ResponseEntity.ok(PaymentResourceFromEntityAssembler.toResourceFromFee(fee));
    }
}
