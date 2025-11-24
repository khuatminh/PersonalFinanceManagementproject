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
@Table(name = "goals")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @NotBlank(message = "Goal name is required")
    @Size(max = 100, message = "Goal name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    String name;

    @NotNull(message = "Target amount is required")
    @DecimalMin(value = "1", message = "Target amount must be greater than 0")
    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    BigDecimal targetAmount;

    @Column(name = "current_amount", precision = 19, scale = 2)
    BigDecimal currentAmount = BigDecimal.ZERO;

    @NotNull(message = "Target date is required")
    @Column(name = "target_date", nullable = false)
    LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    GoalStatus status = GoalStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @Column(length = 500)
    String description;

    @Column(name = "last_notification_percentage")
    Integer lastNotificationPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Goal(String name, BigDecimal targetAmount, LocalDate targetDate, User user) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
        this.user = user;
    }

    // Business logic methods
    public BigDecimal getRemainingAmount() {
        return targetAmount.subtract(currentAmount);
    }

    public double getProgressPercentage() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return currentAmount.doubleValue() / targetAmount.doubleValue() * 100;
    }

    public boolean isCompleted() {
        return currentAmount.compareTo(targetAmount) >= 0;
    }

    public long getDaysRemaining() {
        if (targetDate == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        if (today.isAfter(targetDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(today, targetDate);
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(targetDate) && !isCompleted();
    }

    public void markAsCompleted() {
        this.status = GoalStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void addProgress(BigDecimal amount) {
        this.currentAmount = this.currentAmount.add(amount);
        if (isCompleted()) {
            markAsCompleted();
        }
    }

    @Override
    public String toString() {
        return name + " - " + currentAmount + "/" + targetAmount + " (" +
                String.format("%.1f", getProgressPercentage()) + "%)";
    }

    public enum GoalStatus {
        ACTIVE("Active"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        GoalStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}