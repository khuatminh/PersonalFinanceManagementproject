package com.finance.controller;

import com.finance.domain.Budget;
import com.finance.domain.User;
import com.finance.domain.Category;
import com.finance.form.BudgetForm;
import com.finance.service.BudgetService;
import com.finance.service.UserService;
import com.finance.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BudgetController {

    BudgetService budgetService;

    UserService userService;

    CategoryService categoryService;

    @GetMapping
    public String listBudgets(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BudgetService.BudgetProgress> activeBudgets = budgetService.getActiveBudgetProgress(user);
        List<BudgetService.BudgetProgress> expiredBudgets = budgetService.getExpiredBudgetProgress(user);
        List<BudgetService.BudgetProgress> upcomingBudgets = budgetService.getUpcomingBudgetProgress(user);

        model.addAttribute("user", user);
        model.addAttribute("activeBudgets", activeBudgets);
        model.addAttribute("expiredBudgets", expiredBudgets);
        model.addAttribute("upcomingBudgets", upcomingBudgets);
        model.addAttribute("totalBudgets", activeBudgets.size() + expiredBudgets.size() + upcomingBudgets.size());

        return "budgets/list";
    }

    @GetMapping("/add")
    public String showAddBudgetForm(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Category> categories = categoryService.getExpenseCategories();

        model.addAttribute("user", user);
        model.addAttribute("budgetForm", new BudgetForm());
        model.addAttribute("categories", categories);

        return "budgets/add";
    }

    @PostMapping("/add")
    public String addBudget(@Valid BudgetForm budgetForm, BindingResult result,
            Principal principal, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Custom date validation
        if (!budgetForm.isValidDateRange()) {
            result.rejectValue("endDate", "error.endDate", "End date must be after start date");
        }

        if (result.hasErrors()) {
            List<Category> categories = categoryService.getExpenseCategories();
            model.addAttribute("user", user);
            model.addAttribute("categories", categories);
            return "budgets/add";
        }

        try {
            Category category = null;
            if (budgetForm.getCategoryId() != null) {
                category = categoryService.findById(budgetForm.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
            }

            budgetService.createBudget(
                    budgetForm.getName(),
                    budgetForm.getAmount(),
                    budgetForm.getStartDate(),
                    budgetForm.getEndDate(),
                    user,
                    category,
                    budgetForm.getDescription());

            redirectAttributes.addFlashAttribute("success", "Budget created successfully!");
            return "redirect:/budgets";

        } catch (IllegalArgumentException e) {
            List<Category> categories = categoryService.getExpenseCategories();
            model.addAttribute("user", user);
            model.addAttribute("categories", categories);
            model.addAttribute("error", e.getMessage());
            return "budgets/add";
        } catch (Exception e) {
            List<Category> categories = categoryService.getExpenseCategories();
            model.addAttribute("user", user);
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Error creating budget: " + e.getMessage());
            return "budgets/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditBudgetForm(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetService.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        // Check if budget belongs to current user
        if (!budget.getUser().getId().equals(user.getId())) {
            return "redirect:/budgets?error=Budget not found";
        }

        List<Category> categories = categoryService.getExpenseCategories();

        BudgetForm budgetForm = new BudgetForm();
        budgetForm.setName(budget.getName());
        budgetForm.setAmount(budget.getAmount());
        budgetForm.setStartDate(budget.getStartDate());
        budgetForm.setEndDate(budget.getEndDate());
        if (budget.getCategory() != null) {
            budgetForm.setCategoryId(budget.getCategory().getId());
        }
        budgetForm.setDescription(budget.getDescription());

        model.addAttribute("user", user);
        model.addAttribute("budgetForm", budgetForm);
        model.addAttribute("budget", budget);
        model.addAttribute("categories", categories);

        return "budgets/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBudget(@PathVariable Long id, @Valid BudgetForm budgetForm, BindingResult result,
            Principal principal, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetService.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            return "redirect:/budgets?error=Budget not found";
        }

        // Custom date validation
        if (!budgetForm.isValidDateRange()) {
            result.rejectValue("endDate", "error.endDate", "End date must be after start date");
        }

        if (result.hasErrors()) {
            List<Category> categories = categoryService.getExpenseCategories();
            model.addAttribute("user", user);
            model.addAttribute("budget", budget);
            model.addAttribute("categories", categories);
            return "budgets/edit";
        }

        try {
            Category category = null;
            if (budgetForm.getCategoryId() != null) {
                category = categoryService.findById(budgetForm.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
            }

            budgetService.updateBudget(id, budgetForm.getName(), budgetForm.getAmount(),
                    budgetForm.getStartDate(), budgetForm.getEndDate(), category, budgetForm.getDescription());

            redirectAttributes.addFlashAttribute("success", "Budget updated successfully!");
            return "redirect:/budgets";

        } catch (IllegalArgumentException e) {
            List<Category> categories = categoryService.getExpenseCategories();
            model.addAttribute("user", user);
            model.addAttribute("budget", budget);
            model.addAttribute("categories", categories);
            model.addAttribute("error", e.getMessage());
            return "budgets/edit";
        } catch (Exception e) {
            List<Category> categories = categoryService.getExpenseCategories();
            model.addAttribute("user", user);
            model.addAttribute("budget", budget);
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Error updating budget: " + e.getMessage());
            return "budgets/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetService.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            return "redirect:/budgets?error=Budget not found";
        }

        try {
            budgetService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Budget deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting budget: " + e.getMessage());
        }

        return "redirect:/budgets";
    }

    @GetMapping("/view/{id}")
    public String viewBudget(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetService.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getId().equals(user.getId())) {
            return "redirect:/budgets?error=Budget not found";
        }

        BudgetService.BudgetProgress budgetProgress = budgetService.getBudgetProgress(budget);

        model.addAttribute("user", user);
        model.addAttribute("budget", budget);
        model.addAttribute("budgetProgress", budgetProgress);

        return "budgets/view";
    }
}