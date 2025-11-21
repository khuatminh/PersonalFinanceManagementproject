package com.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finance.domain.Goal;
import com.finance.domain.User;
@Repository
public interface GoalRepository  extends JpaRepository<Goal, Long>{

    List<Goal> findByUser(User user);

    List<Goal> findByUserOrderByTargetDateAsc(User user);

    // Tìm theo trạng thái
    List<Goal> findByUserAndStatus(User user, Goal.GoalStatus status);

    // Tìm kiếm theo tên
    List<Goal> findByUserAndNameContaining(User user, String keyword);

    // Query tìm Active Goals (chưa hoàn thành và chưa quá hạn)
    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.status = 'ACTIVE'")
    List<Goal> findActiveGoalsByUser(@Param("user") User user, @Param("date") LocalDate date);

    // Query tìm Completed Goals
    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.status = 'COMPLETED'")
    List<Goal> findCompletedGoalsByUser(@Param("user") User user);

    // Query tìm Overdue Goals (Active nhưng ngày target < hiện tại)
    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.status = 'ACTIVE' AND g.targetDate < :date")
    List<Goal> findOverdueGoalsByUser(@Param("user") User user, @Param("date") LocalDate date);

    // Query tìm Goal trong khoảng thời gian
    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.targetDate BETWEEN :startDate AND :endDate")
    List<Goal> findGoalsTargetingDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // --- THỐNG KÊ ---
    
    long countGoalsByUserAndStatus(User user, Goal.GoalStatus status);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.user = :user AND g.status = 'ACTIVE' AND g.targetDate < :date")
    long countOverdueGoalsByUser(@Param("user") User user, @Param("date") LocalDate date);

    // Tính tổng tiền mục tiêu
    @Query("SELECT SUM(g.targetAmount) FROM Goal g WHERE g.user = :user AND g.status = 'ACTIVE'")
    BigDecimal sumTargetAmountsByUser(@Param("user") User user);

    // Tính tổng tiền đã tiết kiệm được
    @Query("SELECT SUM(g.currentAmount) FROM Goal g WHERE g.user = :user AND g.status = 'ACTIVE'")
    BigDecimal sumCurrentAmountsByUser(@Param("user") User user);

    // Tìm các goal đã đủ tiền để Service tự động đánh dấu hoàn thành
    @Query("SELECT g FROM Goal g WHERE g.status = 'ACTIVE' AND g.currentAmount >= g.targetAmount")
    List<Goal> findGoalsReadyToComplete();
    
    // Xóa tất cả của user (Query này giúp tối ưu hiệu năng thay vì loop xóa từng cái)
    void deleteByUser(User user);
}

     

