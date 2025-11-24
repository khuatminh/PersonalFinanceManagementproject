package com.finance.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"user"})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, length = 500)
    String message;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(length = 200)
    private String title;

    @Column(length = 500)
    private String actionUrl;

    private LocalDateTime readAt;

    // Constructor for simple notifications
    public Notification(User user, String message) {
        this(user, message, NotificationType.GENERAL, null, null);
    }

    // Enhanced constructor
    public Notification(User user, String message, NotificationType type, String title, String actionUrl) {
        this.user = user;
        this.message = message;
        this.type = type;
        this.title = title;
        this.actionUrl = actionUrl;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public void markAsUnread() {
        this.isRead = false;
        this.readAt = null;
    }

    // Enum for notification types
    public enum NotificationType {
        GENERAL("general"),
        GOAL("goal"),
        BUDGET("budget"),
        TRANSACTION("transaction"),
        SYSTEM("system"),
        SUCCESS("success"),
        WARNING("warning"),
        ERROR("error");

        private final String value;

        NotificationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}