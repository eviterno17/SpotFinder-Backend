package com.spotfinderbackend.wallet.interfaces.rest;

import com.spotfinderbackend.wallet.domain.model.commands.*;
import com.spotfinderbackend.wallet.domain.model.queries.GetPassByIdQuery;
import com.spotfinderbackend.wallet.domain.services.WalletCommandService;
import com.spotfinderbackend.wallet.domain.services.WalletQueryService;
import com.spotfinderbackend.wallet.interfaces.rest.resources.*;
import com.spotfinderbackend.wallet.interfaces.rest.transform.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletCommandService commandService;
    private final WalletQueryService queryService;

    public WalletController(
            WalletCommandService commandService,
            WalletQueryService queryService
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // TS58 → CREATE PASS
    @PostMapping("/passes")
    public ResponseEntity<DigitalPassResource> createPass(
            @RequestBody CreatePassResource resource
    ) {

        var command = CreatePassCommandFromResourceAssembler.toCommand(resource);

        Long passId = commandService.handle(command);

        var pass = queryService.handle(new GetPassByIdQuery(passId));

        return ResponseEntity.status(201)
                .body(DigitalPassResourceFromEntityAssembler.toResource(pass));
    }

    // TS59 → GET PASS
    @GetMapping("/passes/{id}")
    public ResponseEntity<DigitalPassResource> getPass(
            @PathVariable Long id
    ) {

        var pass = queryService.handle(new GetPassByIdQuery(id));

        return ResponseEntity.ok(
                DigitalPassResourceFromEntityAssembler.toResource(pass)
        );
    }

    // TS60 → APPLE WALLET
    @GetMapping("/apple/{id}")
    public ResponseEntity<DigitalPassResource> generateApple(
            @PathVariable Long id
    ) {

        commandService.handle(
                new GenerateApplePassCommand(id)
        );

        var pass = queryService.handle(new GetPassByIdQuery(id));

        return ResponseEntity.ok(
                DigitalPassResourceFromEntityAssembler.toResource(pass)
        );
    }

    // TS61 → GOOGLE WALLET
    @PostMapping("/google")
    public ResponseEntity<DigitalPassResource> generateGoogle(
            @RequestBody GenerateGooglePassResource resource
    ) {

        commandService.handle(
                GenerateGooglePassCommandFromResourceAssembler.toCommand(resource)
        );

        var pass = queryService.handle(new GetPassByIdQuery(resource.passId()));

        return ResponseEntity.status(201)
                .body(DigitalPassResourceFromEntityAssembler.toResource(pass));
    }
}