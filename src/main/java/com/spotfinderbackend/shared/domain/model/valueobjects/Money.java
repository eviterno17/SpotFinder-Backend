package com.spotfinderbackend.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Money Value Object with PEN as default currency.
 * Used by Payment Processing and Analytics Bounded Contexts.
 */
@Embeddable
public record Money(
        @Column(nullable = false, precision = 15, scale = 2)
        BigDecimal amount,

        @Column(nullable = false, length = 3)
        String currencyCode
) {
    public static final String DEFAULT_CURRENCY = "PEN";

    public Money() {
        this(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), DEFAULT_CURRENCY);
    }

    public Money {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(currencyCode, "Currency must not be null");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Money(BigDecimal amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    public Money(double amount) {
        this(BigDecimal.valueOf(amount), DEFAULT_CURRENCY);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currencyCode);
    }

    public boolean isGreaterThan(Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isZero() {
        return this.amount.signum() == 0;
    }

    public static Money zero() {
        return new Money();
    }

    private void requireSameCurrency(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currencyCode + " vs " + other.currencyCode);
        }
    }
}
