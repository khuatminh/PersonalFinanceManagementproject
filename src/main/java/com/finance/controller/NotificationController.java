package com.finance.controller;

public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    // Constructor Injection (Standard Spring Best Practice)
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

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
