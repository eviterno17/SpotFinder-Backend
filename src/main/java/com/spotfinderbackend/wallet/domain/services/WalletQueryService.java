package com.spotfinderbackend.wallet.domain.services;

import com.spotfinderbackend.wallet.domain.model.aggregates.DigitalPass;
import com.spotfinderbackend.wallet.domain.model.queries.GetPassByIdQuery;

public interface WalletQueryService {

    DigitalPass handle(GetPassByIdQuery query);
}