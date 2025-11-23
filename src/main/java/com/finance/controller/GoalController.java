package com.finance.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.finance.domain.Goal;
import com.finance.domain.User;
import com.finance.form.GoalForm;
import com.finance.service.GoalService;
import com.finance.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Controller
@RequestMapping("/goals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoalController {

    GoalService goalService;
    UserService userService;

    @GetMapping
    public String listGoals(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Goal> activeGoals = goalService.findActiveGoalsByUser(user);
        List<Goal> completedGoals = goalService.findCompletedGoalsByUser(user);
        List<Goal> overdueGoals = goalService.findOverdueGoalsByUser(user);

        GoalService.GoalSummary goalSummary = goalService.getGoalSummary(user);

        model.addAttribute("user", user);
        model.addAttribute("activeGoals", activeGoals);
        model.addAttribute("completedGoals", completedGoals);
        model.addAttribute("overdueGoals", overdueGoals);
        model.addAttribute("goalSummary", goalSummary);

        return "goals/list";
    }

    @GetMapping("/add")
    public String showAddGoalForm(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("goalForm", new GoalForm());

        return "goals/add";
    }

    @PostMapping("/add")
    public String addGoal(@Valid GoalForm goalForm, BindingResult result,
            Principal principal, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "goals/add";
        }

        try {
            goalService.createGoal(
                    goalForm.getName(),
                    goalForm.getTargetAmount(),
                    goalForm.getTargetDate(),
                    user,
                    goalForm.getDescription());

            redirectAttributes.addFlashAttribute("success", "Goal created successfully!");
            return "redirect:/goals";

        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Error creating goal: " + e.getMessage());
            return "goals/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditGoalForm(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        // Check if goal belongs to current user
        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        GoalForm goalForm = new GoalForm();
        goalForm.setName(goal.getName());
        goalForm.setTargetAmount(goal.getTargetAmount());
        goalForm.setTargetDate(goal.getTargetDate());
        goalForm.setDescription(goal.getDescription());

        model.addAttribute("user", user);
        model.addAttribute("goalForm", goalForm);
        model.addAttribute("goal", goal);

        return "goals/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateGoal(@PathVariable Long id, @Valid GoalForm goalForm, BindingResult result,
            Principal principal, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("goal", goal);
            return "goals/edit";
        }

        try {
            goalService.updateGoal(id, goalForm.getName(), goalForm.getTargetAmount(),
                    goalForm.getTargetDate(), goalForm.getDescription());

            redirectAttributes.addFlashAttribute("success", "Goal updated successfully!");
            return "redirect:/goals";

        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("goal", goal);
            model.addAttribute("error", "Error updating goal: " + e.getMessage());
            return "goals/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        try {
            goalService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Goal deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting goal: " + e.getMessage());
        }

        return "redirect:/goals";
    }

    @GetMapping("/complete/{id}")
    public String completeGoal(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        try {
            goalService.completeGoal(id);
            redirectAttributes.addFlashAttribute("success", "Goal marked as completed!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error completing goal: " + e.getMessage());
        }

        return "redirect:/goals";
    }

    @GetMapping("/contribute/{id}")
    public String showContributeForm(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        if (goal.getStatus() != Goal.GoalStatus.ACTIVE) {
            return "redirect:/goals?error=Can only contribute to active goals";
        }

        model.addAttribute("user", user);
        model.addAttribute("goal", goal);

        return "goals/contribute";
    }

    @PostMapping("/contribute/{id}")
    public String contributeToGoal(@PathVariable Long id, @RequestParam java.math.BigDecimal amount,
            Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        try {
            goalService.contributeToGoal(id, amount);
            redirectAttributes.addFlashAttribute("success",
                    String.format("Successfully contributed $%.2f to %s!", amount, goal.getName()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error contributing to goal: " + e.getMessage());
        }

        return "redirect:/goals";
    }

    @GetMapping("/view/{id}")
    public String viewGoal(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().getId().equals(user.getId())) {
            return "redirect:/goals?error=Goal not found";
        }

        model.addAttribute("user", user);
        model.addAttribute("goal", goal);

        return "goals/view";
    }

}
