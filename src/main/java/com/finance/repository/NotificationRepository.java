
package com.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finance.domain.Notification;
import com.finance.domain.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find operations
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user);

    Page<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Notification> findByUserAndTypeOrderByCreatedAtDesc(User user, Notification.NotificationType type);

    // Count operations
    long countByUserAndIsReadFalse(User user);

    long countByUser(User user);

    long countByUserAndType(User user, Notification.NotificationType type);

    // Delete operations
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user = :user AND n.isRead = true")
    void deleteByUserAndIsReadTrue(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user = :user AND n.createdAt < :beforeDate")
    void deleteByUserAndCreatedAtBefore(@Param("user") User user, @Param("beforeDate") LocalDateTime beforeDate);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.id = :id")
    int markAsRead(@Param("id") Long id, @Param("readAt") LocalDateTime readAt);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = false, n.readAt = null WHERE n.id = :id")
    int markAsUnread(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user = :user AND n.isRead = false")
    int markAllAsReadForUser(@Param("user") User user, @Param("readAt") LocalDateTime readAt);

    // Additional find method for read notifications
    Page<Notification> findByUserAndIsReadTrueOrderByCreatedAtDesc(User user, Pageable pageable);
}