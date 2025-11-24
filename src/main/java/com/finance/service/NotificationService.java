package com.finance.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.repository.NotificationRepository;
import com.finance.domain.Notification;
import com.finance.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // Basic CRUD operations
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Page<Notification> findByUser(User user, Pageable pageable) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public List<Notification> findUnreadByUser(User user) {
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
    }

    public Page<Notification> findUnreadByUser(User user, Pageable pageable) {
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user, pageable);
    }

    public Page<Notification> findReadByUser(User user, Pageable pageable) {
        return notificationRepository.findByUserAndIsReadTrueOrderByCreatedAtDesc(user, pageable);
    }

    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    // Create notification methods
    public Notification createNotification(User user, String message) {
        return createNotification(user, message, Notification.NotificationType.GENERAL, null, null);
    }

    public Notification createNotification(User user, String message, Notification.NotificationType type) {
        return createNotification(user, message, type, null, null);
    }

    public Notification createNotification(User user, String message, Notification.NotificationType type, String title) {
        return createNotification(user, message, type, title, null);
    }

    public Notification createNotification(User user, String message, Notification.NotificationType type, String title, String actionUrl) {
        Notification notification = new Notification(user, message, type, title, actionUrl);
        return notificationRepository.save(notification);
    }

    // Bulk operations
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void markAllAsReadForUser(User user) {
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        unreadNotifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(unreadNotifications);
    }

    @Transactional
    public void markAsUnread(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.markAsUnread();
        notificationRepository.save(notification);
    }

    // Delete operations
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    public void deleteReadNotifications(User user) {
        notificationRepository.deleteByUserAndIsReadTrue(user);
    }

    @Transactional
    public void deleteOldNotifications(User user, LocalDateTime beforeDate) {
        notificationRepository.deleteByUserAndCreatedAtBefore(user, beforeDate);
    }

    // Statistics
    public long countUnreadByUser(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    public long countByUser(User user) {
        return notificationRepository.countByUser(user);
    }

    // Business logic methods
    public void createGoalNotification(User user, String goalName, String message) {
        createNotification(user, message, Notification.NotificationType.GOAL, "Mục tiêu: " + goalName, "/goals");
    }

    public void createBudgetNotification(User user, String message) {
        createNotification(user, message, Notification.NotificationType.BUDGET, "Thông báo Ngân sách", "/budgets");
    }

    public void createTransactionNotification(User user, String message, String transactionType) {
        Notification.NotificationType type = "expense".equalsIgnoreCase(transactionType) ?
            Notification.NotificationType.WARNING : Notification.NotificationType.SUCCESS;
        createNotification(user, message, type, "Giao dịch", "/transactions");
    }

    public void createSystemNotification(User user, String message) {
        createNotification(user, message, Notification.NotificationType.SYSTEM, "Hệ thống", null);
    }

    public void createSuccessNotification(User user, String message) {
        createNotification(user, message, Notification.NotificationType.SUCCESS, "Thành công", null);
    }

    public void createErrorNotification(User user, String message) {
        createNotification(user, message, Notification.NotificationType.ERROR, "Lỗi", null);
    }

    public void createWarningNotification(User user, String message) {
        createNotification(user, message, Notification.NotificationType.WARNING, "Cảnh báo", null);
    }
}
