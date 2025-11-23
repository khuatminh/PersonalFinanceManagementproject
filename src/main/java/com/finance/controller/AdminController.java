package com.finance.controller;

import com.finance.domain.User;
import com.finance.exception.UserNotFoundException;
import com.finance.form.AdminUserEditForm;
import com.finance.repository.RoleRepository;
import com.finance.service.UserService;

import java.util.List;

import com.finance.service.UserStatisticsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    UserStatisticsService userStatisticsService;
    RoleRepository roleRespository;
    UserService userService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("userCount", userStatisticsService.getUserCount());
        model.addAttribute("adminCount", userStatisticsService.getAdminCount());
        model.addAttribute("regularUserCount", userStatisticsService.getRegularCount());
        return "admin/index";
    }

    @GetMapping("/users")
    public String listUser(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // EDIT USER

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        AdminUserEditForm form = new AdminUserEditForm();
        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());
        form.setRole(user.getUserRole());

        model.addAttribute("userForm", form);
        model.addAttribute("userId", id);
        model.addAttribute("role", roleRespository.findAll());

        return "admin/edit-user";
    }

    @PostMapping("/users/update/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("userForm") AdminUserEditForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        User currentUser = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userService.existsByUsername(form.getUsername()) &&
                !currentUser.getUsername().equals(form.getUsername())) {
            result.rejectValue("username", "error.form", "username is already taken");
        }
        if (result.hasErrors()) {
            model.addAttribute("roles", roleRespository.findAll());
            model.addAttribute("userId", id);
            return "admin/edit-user";
        }
        User userToUpdate = new User();
        userToUpdate.setUsername(form.getUsername());
        userToUpdate.setEmail(form.getEmail());
        userToUpdate.setUserRole(form.getRole());
        userService.updateUser(id, userToUpdate);

        redirectAttributes.addFlashAttribute("sucessMessage", "User updated successfully!");
        return "redirect:/admin/users";
    }

    // DELETE USER
    @GetMapping("/users/delete/{id}")
    public String showDeleteUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        model.addAttribute("user", user);
        return "admin/delete-user";
    }

    @PostMapping("users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        if (!userService.findById(id).isPresent()) {
            throw new UserNotFoundException(id);
        }
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("sucessMessage", "User deleted successfully!");
        return "redirect:/admin/users";
    }

}
