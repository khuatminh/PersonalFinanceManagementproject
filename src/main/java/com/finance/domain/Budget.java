package com.finance.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @NotBlank(message = "Budget name is required")
    @Size(max = 100, message = "Budget name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    String name;

    @NotNull(message = "Budget amount is required")
    @DecimalMin(value = "1", message = "Budget amount must be greater than 0")
    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal amount;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Column(name = "created_at",nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(length = 500)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "last_notification_percentage")
    Integer lastNotificationPercentage;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Budget(String name, BigDecimal amount, LocalDate startDate, LocalDate endDate, User user) {
        this.name = name;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public long getDaysRemaining() {
        if (endDate == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        if (today.isAfter(endDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(today, endDate);
    }

    public long getTotalDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public long getDurationInDays() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        return name + " - " + amount + " (" + startDate + " to " + endDate + ")";
    }
}