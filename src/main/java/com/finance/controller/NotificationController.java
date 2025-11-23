package com.finance.controller;

import com.finance.domain.Notification;
import com.finance.domain.User;
import com.finance.service.NotificationService;
import com.finance.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;
    UserService userService;

    @GetMapping
    public String listNotifications(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Suggestion: Ensure your Service sorts these by Date (DESC)
        List<Notification> notifications = notificationService.findByUser(user);

        model.addAttribute("notifications", notifications);

        return "notifications/index";
    }

    // --- NEW FEATURE SUGGESTION ---

    @PostMapping("/mark-read")
    public String markAsRead(@RequestParam("id") Long notificationId) {
        notificationService.markAsRead(notificationId);
        return "redirect:/notifications";
    }

}
