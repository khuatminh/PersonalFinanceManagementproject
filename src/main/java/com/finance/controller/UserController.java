package com.finance.controller;


import com.finance.domain.User;
import com.finance.exception.ErrorType;
import com.finance.form.PasswordChangeForm;
import com.finance.form.UpdateProfileForm;
import com.finance.form.UserRegistrationForm;
import com.finance.service.UserService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import com.finance.validator.UserRegistrationFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
@Controller
@RequestMapping("/user")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRegistrationFormValidator userRegistrationFormValidator;

    @InitBinder("userForm")
    protected void initRegistrationBinderr(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }
    //ĐĂNG KÍ
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userForm", new UserRegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userForm") UserRegistrationForm userForm,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "register";
        }
        userService.createUser(
                userForm.getUsername(),
                userForm.getEmail(),
                userForm.getPassword()
        );
        redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please login with your new account.");
        return "redirect:/login";
    }

    // Cá nhân hóa(Personalization)

    @GetMapping("/dashboard")
    public  String dashboard(Principal  principal, Model model) {
        User user = getAuthenticatedUser(principal);
        model.addAttribute("user", user);
        model.addAttribute("username", principal.getName());
        return "dashboard";
    }

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        User user = getAuthenticatedUser(principal);
        model.addAttribute("user", user);
        return "user/profile";
    }

    //Chỉnh sửa Profile


    @GetMapping("/edit")
    public String showEditProfile(Principal principal, Model model) {
        User user = getAuthenticatedUser(principal);
        UpdateProfileForm form = new UpdateProfileForm();
        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());

        model.addAttribute("profileForm", form);
        return "user/edit-profile";
    }
    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("profileForm") UpdateProfileForm form,
        BindingResult bindingResult, Principal principal,RedirectAttributes redirectAttributes)
    {
        User currentUser = getAuthenticatedUser(principal);
        if(userService.existsByUsername(form.getUsername()) && !currentUser.getUsername().equals(form.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username is already in use!");
        }

        if(userService.existsByEmail(form.getEmail()) && !currentUser.getEmail().equals(form.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email is already in use!");
        }
        if(bindingResult.hasErrors()) {
            return "user/edit-profile";
        }

        User userToUpdate = new User();
        userToUpdate.setUsername(form.getUsername());
        userToUpdate.setEmail(form.getEmail());

        userService.updateProfile((long) currentUser.getId(), userToUpdate);
        redirectAttributes.addFlashAttribute("successMessage",
                "Profile updated successfully!");
        return "redirect:/user/profile";
    }

    // Đổi mật khẩu

    @GetMapping("/change-password")
    public String showChangePassword( Model model) {
        model.addAttribute("passwordForm", new PasswordChangeForm());
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordForm") PasswordChangeForm form,
                                 BindingResult bindingResult, Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (!form.isPasswordMatching()) {
            bindingResult.rejectValue("confirmNewPassword", "error.passwordForm",
                    "New password and confirmation do not match");
        }
        if (bindingResult.hasErrors()) {
            return "user/change-password";
        }

        User user = getAuthenticatedUser(principal);
        userService.changePassword((long) user.getId(), form.getCurrentPassword(), form.getNewPassword());
        redirectAttributes.addFlashAttribute("successMessage",
                "Password changed successfully!");
        return "redirect:/user/change-password";
    }

    //lấy ErrorType
    private User getAuthenticatedUser(Principal principal) {
        return userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException(ErrorType.AUTH_USER_NOT_FOUND.getMessage() + principal.getName()));
    }
}
