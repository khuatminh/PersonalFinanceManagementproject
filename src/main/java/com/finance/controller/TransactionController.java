package com.finance.controller;

import com.finance.domain.Transaction;
import com.finance.domain.Category;
import com.finance.domain.User;
import com.finance.form.TransactionForm;
import com.finance.service.TransactionService;
import com.finance.service.CategoryService;
import com.finance.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {

    TransactionService transactionService;

    CategoryService categoryService;

    UserService userService;

    @GetMapping
    public String listTransactions(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionService.findByUserOrderByDateDesc(user);
        TransactionService.TransactionStatistics stats = transactionService.getTransactionStatistics(user);

        model.addAttribute("transactions", transactions);
        model.addAttribute("stats", stats);
        model.addAttribute("user", user);

        return "transaction/list";
    }

    @GetMapping("/add")
    public String showAddTransactionForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Category> incomeCategories = categoryService.getIncomeCategories();
        List<Category> expenseCategories = categoryService.getExpenseCategories();

        model.addAttribute("transactionForm", new TransactionForm());
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("user", user);

        return "transaction/add";
    }

    @PostMapping("/add")
    public String addTransaction(@Valid @ModelAttribute("transactionForm") TransactionForm form,
            BindingResult bindingResult, Principal principal,
            RedirectAttributes redirectAttributes, Model model) {

        System.out.println("=== Transaction Form Submission ===");
        System.out.println("Form data: " + form.getDescription() + ", " + form.getAmount() + ", " + form.getType()
                + ", " + form.getCategoryId());
        System.out.println("Has errors: " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
        }

        if (bindingResult.hasErrors()) {
            // Re-populate the model with categories when there are validation errors
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Category> incomeCategories = categoryService.getIncomeCategories();
            List<Category> expenseCategories = categoryService.getExpenseCategories();

            model.addAttribute("incomeCategories", incomeCategories);
            model.addAttribute("expenseCategories", expenseCategories);
            model.addAttribute("user", user);

            return "transaction/add";
        }

        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Category category = categoryService.findById(form.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            Transaction transaction = transactionService.createTransaction(
                    form.getDescription(),
                    form.getAmount(),
                    form.getType(),
                    user,
                    category,
                    form.getTransactionDate() != null ? form.getTransactionDate() : LocalDateTime.now(),
                    form.getNotes());

            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction added successfully: " + transaction.getDescription());
            return "redirect:/transactions";

        } catch (Exception e) {
            bindingResult.reject("transactionError", "Error adding transaction: " + e.getMessage());

            // Re-populate the model with categories when there's an exception
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Category> incomeCategories = categoryService.getIncomeCategories();
            List<Category> expenseCategories = categoryService.getExpenseCategories();

            model.addAttribute("incomeCategories", incomeCategories);
            model.addAttribute("expenseCategories", expenseCategories);
            model.addAttribute("user", user);

            return "transaction/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditTransactionForm(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = transactionService.findById(id);

        // Verify ownership
        if (!transaction.getUser().getId().equals(user.getId())) {
            return "redirect:/access-denied";
        }

        List<Category> incomeCategories = categoryService.getIncomeCategories();
        List<Category> expenseCategories = categoryService.getExpenseCategories();

        model.addAttribute("transactionForm", new TransactionForm(transaction));
        model.addAttribute("transaction", transaction);
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("user", user);

        return "transaction/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTransaction(@PathVariable Long id,
            @Valid @ModelAttribute("transactionForm") TransactionForm form,
            BindingResult bindingResult, Principal principal,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "transaction/edit";
        }

        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Transaction existingTransaction = transactionService.findById(id);

            // Verify ownership
            if (!existingTransaction.getUser().getId().equals(user.getId())) {
                return "redirect:/access-denied";
            }

            Category category = categoryService.findById(form.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // Update transaction details
            existingTransaction.setDescription(form.getDescription());
            existingTransaction.setAmount(form.getAmount());
            existingTransaction.setType(form.getType());
            existingTransaction.setCategory(category);
            existingTransaction.setTransactionDate(form.getTransactionDate() != null ? form.getTransactionDate()
                    : existingTransaction.getTransactionDate());
            existingTransaction.setNotes(form.getNotes());

            transactionService.save(existingTransaction);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction updated successfully: " + existingTransaction.getDescription());
            return "redirect:/transactions";

        } catch (Exception e) {
            bindingResult.reject("transactionError", "Error updating transaction: " + e.getMessage());
            return "transaction/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Transaction transaction = transactionService.findById(id);

            // Verify ownership
            if (!transaction.getUser().getId().equals(user.getId())) {
                return "redirect:/access-denied";
            }

            String description = transaction.getDescription();
            transactionService.deleteById(id);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction deleted successfully: " + description);
            return "redirect:/transactions";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting transaction: " + e.getMessage());
            return "redirect:/transactions";
        }
    }

    @GetMapping("/search")
    public String searchTransactions(@RequestParam String keyword, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionService.searchTransactions(user, keyword);

        model.addAttribute("transactions", transactions);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("user", user);
        model.addAttribute("searchPerformed", true);

        return "transaction/list";
    }

    @GetMapping("/filter")
    public String filterTransactions(
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
            Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions;

        if (type != null && categoryId != null && startDate != null && endDate != null) {
            Category category = categoryService.findById(categoryId).orElse(null);
            transactions = transactionService.findByUserAndTypeAndDateRange(user, type, startDate, endDate)
                    .stream()
                    .filter(t -> category == null || category.equals(t.getCategory()))
                    .collect(java.util.stream.Collectors.toList());
        } else if (type != null) {
            transactions = transactionService.findByUserAndType(user, type);
        } else if (categoryId != null) {
            Category category = categoryService.findById(categoryId).orElse(null);
            if (category != null) {
                transactions = transactionService.findByUserAndCategory(user, category);
            } else {
                transactions = transactionService.findByUserOrderByDateDesc(user);
            }
        } else if (startDate != null && endDate != null) {
            transactions = transactionService.findByUserAndDateRange(user, startDate, endDate);
        } else {
            transactions = transactionService.findByUserOrderByDateDesc(user);
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("user", user);
        model.addAttribute("filterApplied", true);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("incomeCategories", categoryService.getIncomeCategories());
        model.addAttribute("expenseCategories", categoryService.getExpenseCategories());

        return "transaction/list";
    }
}