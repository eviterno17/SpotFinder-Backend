package com.spotfinderbackend.wallet.application.internal.commandservices;

import com.spotfinderbackend.wallet.domain.model.aggregates.DigitalPass;
import com.spotfinderbackend.wallet.domain.model.commands.CreatePassCommand;
import com.spotfinderbackend.wallet.domain.model.commands.GenerateApplePassCommand;
import com.spotfinderbackend.wallet.domain.model.commands.GenerateGooglePassCommand;
import com.spotfinderbackend.wallet.domain.services.WalletCommandService;
import com.spotfinderbackend.wallet.infrastructure.persistence.jpa.repositories.DigitalPassRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletCommandServiceImpl implements WalletCommandService {

    private final DigitalPassRepository repository;

    public WalletCommandServiceImpl(DigitalPassRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long handle(CreatePassCommand command) {

        var pass = new DigitalPass(command.sessionId());

        repository.save(pass);

        return pass.getId();
    }

    @Override
    public void handle(GenerateApplePassCommand command) {

        var pass = repository.findById(command.passId())
                .orElseThrow();

        pass.generateApplePass();

        repository.save(pass);
    }

    @Override
    public void handle(GenerateGooglePassCommand command) {

        var pass = repository.findById(command.passId())
                .orElseThrow();

        pass.generateGooglePass();

        repository.save(pass);
    }
}