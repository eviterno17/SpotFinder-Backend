package com.spotfinderbackend.wallet.domain.services;

import com.spotfinderbackend.wallet.domain.model.commands.*;
import com.spotfinderbackend.wallet.domain.model.aggregates.DigitalPass;
import com.spotfinderbackend.wallet.domain.model.queries.GetPassByIdQuery;

public interface WalletCommandService {

    Long handle(CreatePassCommand command);

    void handle(GenerateApplePassCommand command);

    void handle(GenerateGooglePassCommand command);
}