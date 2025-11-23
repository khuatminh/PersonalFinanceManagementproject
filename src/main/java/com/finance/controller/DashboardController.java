package com.finance.controller;

import com.finance.domain.Transaction;
import com.finance.domain.Budget;
import com.finance.domain.Goal;
import com.finance.domain.User;
import com.finance.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

        TransactionService transactionService;

        BudgetService budgetService;

        GoalService goalService;

        UserService userService;

        CategoryService categoryService;

        @GetMapping("/dashboard")
        public String dashboard(Principal principal, Model model) {
                // Get current user
                User user = userService.findByUsername(principal.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Transaction Statistics
                TransactionService.TransactionStatistics stats = transactionService.getTransactionStatistics(user);
                List<Transaction> recentTransactions = transactionService.findRecentTransactionsByUser(user, 5);

                // Budget Information
                List<BudgetService.BudgetProgress> activeBudgets = budgetService.getActiveBudgetProgress(user);
                long totalBudgets = budgetService.getActiveBudgetCount(user);
                long overBudgetCount = activeBudgets.stream()
                                .mapToLong(b -> b.isOverBudget() ? 1 : 0)
                                .sum();

                // Goal Information
                GoalService.GoalSummary goalSummary = goalService.getGoalSummary(user);
                List<Goal> activeGoals = goalService.findActiveGoalsByUser(user).stream()
                                .limit(5)
                                .collect(java.util.stream.Collectors.toList());

                // Category Summary
                List<Object[]> categorySummary = transactionService.getCategoryTransactionSummary(user);
                Map<String, Object[]> topCategories = new HashMap<>();
                categorySummary.stream()
                                .limit(5)
                                .forEach(arr -> topCategories.put((String) arr[0], arr));

                // Add all data to model
                model.addAttribute("user", user);
                model.addAttribute("stats", stats);
                model.addAttribute("recentTransactions", recentTransactions);
                model.addAttribute("activeBudgets", activeBudgets);
                model.addAttribute("totalBudgets", totalBudgets);
                model.addAttribute("overBudgetCount", overBudgetCount);
                model.addAttribute("goalSummary", goalSummary);
                model.addAttribute("activeGoals", activeGoals);
                model.addAttribute("topCategories", topCategories);
                model.addAttribute("categorySummary", categorySummary);

                return "dashboard";
        }

        @GetMapping("/api/financial-summary")
        @ResponseBody
        public Map<String, Object> getFinancialSummary(Principal principal) {
                User user = userService.findByUsername(principal.getName())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                TransactionService.TransactionStatistics stats = transactionService.getTransactionStatistics(user);
                BudgetService.BudgetProgress budgetProgress = budgetService.getActiveBudgetProgress(user).stream()
                                .findFirst()
                                .orElse(null);
                GoalService.GoalSummary goalSummary = goalService.getGoalSummary(user);

                Map<String, Object> summary = new HashMap<>();
                summary.put("balance", stats.getBalance());
                summary.put("totalIncome", stats.getTotalIncome());
                summary.put("totalExpenses", stats.getTotalExpenses());
                // summary.put("transactionCount", stats.getTotalCount());

                if (budgetProgress != null) {
                        summary.put("budgetSpent", budgetProgress.getSpent());
                        summary.put("budgetRemaining", budgetProgress.getRemaining());
                }

                summary.put("goalProgress", goalSummary.getOverallProgressPercentage());
                summary.put("activeGoalsCount", goalSummary.getActiveGoalsCount());

                return summary;
        }
}