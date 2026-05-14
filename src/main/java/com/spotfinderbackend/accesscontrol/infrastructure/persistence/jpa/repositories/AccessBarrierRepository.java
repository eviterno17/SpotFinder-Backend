package com.spotfinderbackend.accesscontrol.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.accesscontrol.domain.model.aggregates.AccessBarrier;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierCode;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessBarrierRepository extends JpaRepository<AccessBarrier, Long> {

    Optional<AccessBarrier> findByBarrierCode(BarrierCode code);

    List<AccessBarrier> findByPosition(BarrierPosition position);
}
