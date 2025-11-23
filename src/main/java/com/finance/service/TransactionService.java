package com.finance.service;

import com.finance.domain.Transaction;
import com.finance.domain.User;
import com.finance.domain.Category;
import com.finance.repository.TransactionRepository;
import com.finance.exception.TransactionNotFoundException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {

    TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> findByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    public List<Transaction> findByUserOrderByDateDesc(User user) {
        return transactionRepository.findByUserOrderByTransactionDateDesc(user);
    }

    public List<Transaction> findRecentTransactionsByUser(User user, int limit) {
        List<Transaction> transactions = transactionRepository.findRecentTransactionsByUser(user);
        return transactions.size() > limit ? transactions.subList(0, limit) : transactions;
    }

    public List<Transaction> findByUserAndCategory(User user, Category category) {
        return transactionRepository.findByUserAndCategory(user, category);
    }

    public List<Transaction> findByUserAndType(User user, Transaction.TransactionType type) {
        return transactionRepository.findByUserAndType(user, type);
    }

    public List<Transaction> findByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserAndTransactionDateBetween(user, startDate, endDate);
    }

    public List<Transaction> findByUserAndTypeAndDateRange(User user, Transaction.TransactionType type,
            LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserAndTypeAndTransactionDateBetween(user, type, startDate, endDate);
    }

    public List<Transaction> findByUserAndCategoryAndDateRange(User user, Category category,
            LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserAndCategoryAndTransactionDateBetween(user, category, startDate, endDate);
    }

    public List<Transaction> searchTransactions(User user, String keyword) {
        return transactionRepository.findByUserAndDescriptionContaining(user, keyword);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction createTransaction(String description, BigDecimal amount, Transaction.TransactionType type,
            User user, Category category, LocalDateTime transactionDate, String notes) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setTransactionDate(transactionDate != null ? transactionDate : LocalDateTime.now());
        transaction.setNotes(notes);

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setDescription(transactionDetails.getDescription());
                    transaction.setAmount(transactionDetails.getAmount());
                    transaction.setType(transactionDetails.getType());
                    transaction.setCategory(transactionDetails.getCategory());
                    transaction.setTransactionDate(transactionDetails.getTransactionDate());
                    transaction.setNotes(transactionDetails.getNotes());

                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public void deleteById(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException(id);
        }
        transactionRepository.deleteById(id);
    }

    public BigDecimal getTotalIncomeByUser(User user) {
        BigDecimal total = transactionRepository.sumAmountByUserAndType(user, Transaction.TransactionType.INCOME);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpensesByUser(User user) {
        BigDecimal total = transactionRepository.sumAmountByUserAndType(user, Transaction.TransactionType.EXPENSE);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getBalanceByUser(User user) {
        return getTotalIncomeByUser(user).subtract(getTotalExpensesByUser(user));
    }

    public BigDecimal getTotalIncomeByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                user, Transaction.TransactionType.INCOME, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpensesByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                user, Transaction.TransactionType.EXPENSE, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpensesByUserAndCategoryAndDateRange(User user, Category category,
            LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = transactionRepository.sumAmountByUserAndCategoryAndTypeAndDateRange(
                user, category, Transaction.TransactionType.EXPENSE, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public long getTransactionCountByUserAndType(User user, Transaction.TransactionType type) {
        return transactionRepository.countTransactionsByUserAndType(user, type);
    }

    public List<Object[]> getCategoryTransactionSummaryForDateRange(User user, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepository.getCategoryTransactionSummaryForDateRange(user, startDate, endDate);
    }

    public List<Object[]> getExpenseCategorySummaryForDateRange(User user, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepository.getExpenseCategorySummaryForDateRange(user, startDate, endDate);
    }

    public List<Object[]> getIncomeCategorySummaryForDateRange(User user, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepository.getIncomeCategorySummaryForDateRange(user, startDate, endDate);
    }

    public List<Object[]> getTransactionTrendByDate(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.getTransactionTrendByDate(user, startDate, endDate);
    }

    public List<Object[]> getCategoryTransactionSummary(User user) {
        return transactionRepository.getCategoryTransactionSummary(user);
    }

    public void deleteAllByUser(User user) {
        List<Transaction> transactions = findByUser(user);
        transactions.forEach(transaction -> transactionRepository.delete(transaction));
    }

    // Statistics methods
    public TransactionStatistics getTransactionStatistics(User user) {
        return new TransactionStatistics(
                getTotalIncomeByUser(user),
                getTotalExpensesByUser(user),
                getBalanceByUser(user),
                getTransactionCountByUserAndType(user, Transaction.TransactionType.INCOME),
                getTransactionCountByUserAndType(user, Transaction.TransactionType.EXPENSE));
    }

    public TransactionStatistics getTransactionStatisticsForDateRange(User user, LocalDateTime startDate,
            LocalDateTime endDate) {
        BigDecimal income = getTotalIncomeByUserAndDateRange(user, startDate, endDate);
        BigDecimal expenses = getTotalExpensesByUserAndDateRange(user, startDate, endDate);
        long incomeCount = transactionRepository.countTransactionsByUserAndTypeAndDateRange(
                user, Transaction.TransactionType.INCOME, startDate, endDate);
        long expenseCount = transactionRepository.countTransactionsByUserAndTypeAndDateRange(
                user, Transaction.TransactionType.EXPENSE, startDate, endDate);

        return new TransactionStatistics(
                income,
                expenses,
                income.subtract(expenses),
                incomeCount,
                expenseCount);
    }

    @Getter
    public static class TransactionStatistics {
        final BigDecimal totalIncome;
        final BigDecimal totalExpenses;
        final BigDecimal balance;
        final long incomeCount;
        final long expenseCount;
        final long totalCount;

        public TransactionStatistics(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal balance,
                long incomeCount, long expenseCount) {
            this.totalIncome = totalIncome;
            this.totalExpenses = totalExpenses;
            this.balance = balance;
            this.incomeCount = incomeCount;
            this.expenseCount = expenseCount;
            this.totalCount = incomeCount + expenseCount;
        }

    }
}