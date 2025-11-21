package com.finance.repository;

import com.finance.domain.Budget;
import com.finance.domain.User;
import com.finance.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser(User user);

    List<Budget> findByUserOrderByStartDateDesc(User user);

    List<Budget> findByUserAndCategory(User user, Category category);

    List<Budget> findByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.startDate <= :currentDate AND b.endDate >= :currentDate")
    List<Budget> findActiveBudgetsByUser(@Param("user") User user, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.endDate < :currentDate")
    List<Budget> findExpiredBudgetsByUser(@Param("user") User user, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.startDate > :currentDate")
    List<Budget> findUpcomingBudgetsByUser(@Param("user") User user, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND " +
            "(b.name LIKE %:keyword% OR b.description LIKE %:keyword%)")
    List<Budget> findByUserAndNameContaining(@Param("user") User user, @Param("keyword") String keyword);

    @Query("SELECT COUNT(b) FROM Budget b WHERE b.user = :user AND b.startDate <= :currentDate AND b.endDate >= :currentDate")
    long countActiveBudgetsByUser(@Param("user") User user, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT SUM(b.amount) FROM Budget b WHERE b.user = :user AND b.startDate <= :currentDate AND b.endDate >= :currentDate")
    java.math.BigDecimal sumActiveBudgetAmountsByUser(@Param("user") User user, @Param("currentDate") LocalDate currentDate);
}