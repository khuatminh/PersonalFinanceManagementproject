package com.finance.repository;

import com.finance.domain.Transaction;
import com.finance.domain.User;
import com.finance.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserOrderByTransactionDateDesc(User user);

    List<Transaction> findByUserAndCategory(User user, Category category);

    List<Transaction> findByUserAndType(User user, Transaction.TransactionType type);

    List<Transaction> findByUserAndTransactionDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserAndTypeAndTransactionDateBetween(User user, Transaction.TransactionType type,
                                                                 LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserAndCategoryAndTransactionDateBetween(User user, Category category,
                                                                     LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND " +
            "(t.description LIKE %:keyword% OR t.notes LIKE %:keyword%)")
    List<Transaction> findByUserAndDescriptionContaining(@Param("user") User user, @Param("keyword") String keyword);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type")
    BigDecimal sumAmountByUserAndType(@Param("user") User user, @Param("type") Transaction.TransactionType type);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type AND " +
            "t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByUserAndTypeAndDateRange(@Param("user") User user,
                                                  @Param("type") Transaction.TransactionType type,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);


    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.category = :category AND t.type = :type AND " +
            "t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByUserAndCategoryAndTypeAndDateRange(@Param("user") User user,
                                                             @Param("category") Category category,
                                                             @Param("type") Transaction.TransactionType type,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);
    @Query("SELECT t FROM Transaction t WHERE t.user = :user ORDER BY t.transactionDate DESC")
    List<Transaction> findRecentTransactionsByUser(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user AND t.type = :type")
    long countTransactionsByUserAndType(@Param("user") User user, @Param("type") Transaction.TransactionType type);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user AND t.type = :type AND " +
            "t.transactionDate BETWEEN :startDate AND :endDate")
    long countTransactionsByUserAndTypeAndDateRange(@Param("user") User user,
                                                    @Param("type") Transaction.TransactionType type,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);
    long countByUserAndTransactionDateAfter(User user, LocalDateTime date);

    @Query("SELECT t.category.name, COUNT(t), SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = 'EXPENSE' AND " +
            "t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.category.name ORDER BY SUM(t.amount) DESC")
    List<Object[]> getExpenseCategorySummaryForDateRange(@Param("user") User user,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.category.name, COUNT(t), SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = 'INCOME' AND " +
            "t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.category.name ORDER BY SUM(t.amount) DESC")
    List<Object[]> getIncomeCategorySummaryForDateRange(@Param("user") User user,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.category.name, COUNT(t), SUM(t.amount) FROM Transaction t WHERE t.user = :user AND " +
            "t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.category.name ORDER BY SUM(t.amount) DESC")
    List<Object[]> getCategoryTransactionSummaryForDateRange(@Param("user") User user,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.category.name, COUNT(t), SUM(t.amount) FROM Transaction t WHERE t.user = :user " +
            "GROUP BY t.category.name ORDER BY SUM(t.amount) DESC")
    List<Object[]> getCategoryTransactionSummary(@Param("user") User user);

    @Query("SELECT CAST(t.transactionDate AS date), " +
            "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), " +
            "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) " +
            "FROM Transaction t WHERE t.user = :user AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CAST(t.transactionDate AS date) ORDER BY CAST(t.transactionDate AS date)")
    List<Object[]> getTransactionTrendByDate(@Param("user") User user,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
}