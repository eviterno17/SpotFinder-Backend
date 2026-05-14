package com.spotfinderbackend.payments.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.payments.domain.model.aggregates.Payment;
import com.spotfinderbackend.payments.domain.model.valueobjects.PaymentTransactionStatus;
import com.spotfinderbackend.payments.domain.model.valueobjects.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findBySessionId(SessionId sessionId);

    Optional<Payment> findBySessionId_ValueAndStatus(Long sessionId, PaymentTransactionStatus status);

    List<Payment> findByUserIdOrderByPaidAtDesc(Long userId);

    boolean existsBySessionId(SessionId sessionId);
}
