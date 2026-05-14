package com.spotfinderbackend.wallet.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.wallet.domain.model.aggregates.DigitalPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalPassRepository extends JpaRepository<DigitalPass, Long> {
}