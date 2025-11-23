package com.finance.service;

import com.finance.domain.Budget;
import com.finance.domain.User;
import com.finance.domain.Category;
import com.finance.domain.Transaction;
import com.finance.repository.BudgetRepository;
import com.finance.service.TransactionService;
import com.finance.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationService notificationService;

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public Optional<Budget> findById(Long id) {
        return budgetRepository.findById(id);
    }

    public List<Budget> findByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    public List<Budget> findByUserOrderByStartDateDesc(User user) {
        return budgetRepository.findByUserOrderByStartDateDesc(user);
    }

    public List<Budget> findActiveBudgetsByUser(User user) {
        return budgetRepository.findActiveBudgetsByUser(user, LocalDate.now());
    }

    public List<Budget> findExpiredBudgetsByUser(User user) {
        return budgetRepository.findExpiredBudgetsByUser(user, LocalDate.now());
    }

    public List<Budget> findUpcomingBudgetsByUser(User user) {
        return budgetRepository.findUpcomingBudgetsByUser(user, LocalDate.now());
    }

    public List<Budget> findByUserAndCategory(User user, Category category) {
        return budgetRepository.findByUserAndCategory(user, category);
    }

    public List<Budget> searchBudgets(User user, String keyword) {
        return budgetRepository.findByUserAndNameContaining(user, keyword);
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget createBudget(String name, BigDecimal amount, LocalDate startDate, LocalDate endDate,
                              User user, Category category, String description) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        Budget budget = new Budget();
        budget.setName(name);
        budget.setAmount(amount);
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);
        budget.setUser(user);
        budget.setCategory(category);
        budget.setDescription(description);

        return budgetRepository.save(budget);
    }



    public Budget updateBudget(Long id, String name, BigDecimal amount, LocalDate startDate,
                              LocalDate endDate, Category category, String description) {
        return budgetRepository.findById(id)
                .map(budget -> {
                    if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException("Start date cannot be after end date.");
                    }

                    budget.setName(name);
                    budget.setAmount(amount);
                    budget.setStartDate(startDate);
                    budget.setEndDate(endDate);
                    budget.setCategory(category);
                    budget.setDescription(description);

                    return budgetRepository.save(budget);
                })
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
    }

    public List<BudgetProgress> getExpiredBudgetProgress(User user) {
        List<Budget> expiredBudgets = findExpiredBudgetsByUser(user);
        return expiredBudgets.stream()
                .map(this::getBudgetProgress)
                .sorted((b1, b2) -> Long.compare(b2.getDaysRemaining(), b1.getDaysRemaining()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<BudgetProgress> getUpcomingBudgetProgress(User user) {
        List<Budget> upcomingBudgets = findUpcomingBudgetsByUser(user);
        return upcomingBudgets.stream()
                .map(this::getBudgetProgress)
                .sorted((b1, b2) -> Long.compare(b1.getDaysRemaining(), b2.getDaysRemaining()))
                .collect(java.util.stream.Collectors.toList());
    }

    public void deleteById(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new RuntimeException("Budget not found with id: " + id);
        }
        budgetRepository.deleteById(id);
    }

    public long getActiveBudgetCount(User user) {
        return budgetRepository.countActiveBudgetsByUser(user, LocalDate.now());
    }

    public BigDecimal getTotalActiveBudgetAmount(User user) {
        BigDecimal total = budgetRepository.sumActiveBudgetAmountsByUser(user, LocalDate.now());
        return total != null ? total : BigDecimal.ZERO;
    }

    public BudgetProgress getBudgetProgress(Budget budget) {
        BigDecimal spent;
        // If budget has a specific category, filter by that category
        if (budget.getCategory() != null) {
            spent = transactionService.getTotalExpensesByUserAndCategoryAndDateRange(
                budget.getUser(),
                budget.getCategory(),
                budget.getStartDate().atStartOfDay(),
                budget.getEndDate().atTime(23, 59, 59)
            );
        } else {
            // Calculate spent amount based on all transactions within budget period
            spent = transactionService.getTotalExpensesByUserAndDateRange(
                budget.getUser(),
                budget.getStartDate().atStartOfDay(),
                budget.getEndDate().atTime(23, 59, 59)
            );
        }

        BigDecimal remaining = budget.getAmount().subtract(spent);
        double percentageSpent = budget.getAmount().compareTo(BigDecimal.ZERO) > 0 ?
            spent.doubleValue() / budget.getAmount().doubleValue() * 100 : 0.0;

        boolean overBudget = spent.compareTo(budget.getAmount()) > 0;
        long daysRemaining = budget.getDaysRemaining();

        BudgetProgress budgetProgress = new BudgetProgress(budget, spent, remaining, percentageSpent, overBudget, daysRemaining);
        checkAndSendBudgetNotifications(budgetProgress);
        return budgetProgress;
    }

    public List<BudgetProgress> getAllBudgetProgress(User user) {
        List<Budget> budgets = findByUser(user);
        return budgets.stream()
                .map(this::getBudgetProgress)
                .sorted((b1, b2) -> Long.compare(b2.getDaysRemaining(), b1.getDaysRemaining()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<BudgetProgress> getActiveBudgetProgress(User user) {
        List<Budget> activeBudgets = findActiveBudgetsByUser(user);
        return activeBudgets.stream()
                .map(this::getBudgetProgress)
                .sorted((b1, b2) -> Double.compare(b2.getPercentageSpent(), b1.getPercentageSpent()))
                .collect(java.util.stream.Collectors.toList());
    }

    public void deleteAllByUser(User user) {
        List<Budget> budgets = findByUser(user);
        budgets.forEach(budget -> budgetRepository.delete(budget));
    }

    private void checkAndSendBudgetNotifications(BudgetProgress budgetProgress) {
        Budget budget = budgetProgress.getBudget();
        double percentageSpent = budgetProgress.getPercentageSpent();
        Integer lastNotificationPercentage = budget.getLastNotificationPercentage();

        int currentThreshold = 0;
        if (percentageSpent >= 100) {
            currentThreshold = 100;
        } else if (percentageSpent >= 90) {
            currentThreshold = 90;
        } else if (percentageSpent >= 80) {
            currentThreshold = 80;
        }

        if (currentThreshold > 0 && (lastNotificationPercentage == null || currentThreshold > lastNotificationPercentage)) {
            String message;
            if (budgetProgress.isOverBudget()) {
                message = String.format("You have exceeded your budget '%s' by %s!", budget.getName(), budgetProgress.getRemaining().abs());
            } else {
                message = String.format("You have spent over %d%% of your budget '%s'.", currentThreshold, budget.getName());
            }
            notificationService.createNotification(budget.getUser(), message);
            budget.setLastNotificationPercentage(currentThreshold);
            budgetRepository.save(budget);
        }
    }



    public static class BudgetProgress {
        private final Budget budget;
        private final BigDecimal spent;
        private final BigDecimal remaining;
        private final double percentageSpent;
        private final boolean overBudget;
        private final long daysRemaining;

        public BudgetProgress(Budget budget, BigDecimal spent, BigDecimal remaining,
                            double percentageSpent, boolean overBudget, long daysRemaining) {
            this.budget = budget;
            this.spent = spent;
            this.remaining = remaining;
            this.percentageSpent = percentageSpent;
            this.overBudget = overBudget;
            this.daysRemaining = daysRemaining;
        }

        // Getters
        public Budget getBudget() { return budget; }
        public BigDecimal getSpent() { return spent; }
        public BigDecimal getRemaining() { return remaining; }
        public double getPercentageSpent() { return percentageSpent; }
        public boolean isOverBudget() { return overBudget; }
        public long getDaysRemaining() { return daysRemaining; }
        public boolean isActive() { return budget.isActive(); }
        public String getStatus() {
            if (!budget.isActive()) return "Expired";
            if (overBudget) return "Over Budget";
            if (percentageSpent >= 80) return "Warning";
            return "On Track";
        }
    }
}