package com.spotfinderbackend.wallet.application.internal.queryservices;

import com.spotfinderbackend.wallet.domain.model.aggregates.DigitalPass;
import com.spotfinderbackend.wallet.domain.model.queries.GetPassByIdQuery;
import com.spotfinderbackend.wallet.domain.services.WalletQueryService;
import com.spotfinderbackend.wallet.infrastructure.persistence.jpa.repositories.DigitalPassRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletQueryServiceImpl implements WalletQueryService {

    private final DigitalPassRepository repository;

    public WalletQueryServiceImpl(DigitalPassRepository repository) {
        this.repository = repository;
    }

    @Override
    public DigitalPass handle(GetPassByIdQuery query) {

        return repository.findById(query.id())
                .orElseThrow(() ->
                        new RuntimeException("Digital pass not found")
                );
    }
}