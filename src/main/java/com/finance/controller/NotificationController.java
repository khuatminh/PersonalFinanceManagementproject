package com.finance.controller;

import com.finance.domain.Notification;
import com.finance.domain.User;
import com.finance.service.NotificationService;
import com.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    // Web endpoints
    @GetMapping
    public String listNotifications(
            Principal principal,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "all") String filter) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications;

        switch (filter.toLowerCase()) {
            case "unread":
                notifications = notificationService.findUnreadByUser(user, pageable);
                break;
            case "read":
                notifications = notificationService.findReadByUser(user, pageable);
                break;
            default:
                notifications = notificationService.findByUser(user, pageable);
        }

        model.addAttribute("notifications", notifications);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notifications.getTotalPages());
        model.addAttribute("totalItems", notifications.getTotalElements());
        model.addAttribute("filter", filter);
        model.addAttribute("unreadCount", notificationService.countUnreadByUser(user));

        return "notifications/index";
    }

    // Specific path mappings must come before generic path variable mappings
    @PostMapping("/mark-all-read")
    public String markAllAsRead(
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.markAllAsReadForUser(user);
        redirectAttributes.addFlashAttribute("success", "Tất cả thông báo đã được đánh dấu đã đọc");

        return "redirect:/notifications";
    }

    @PostMapping("/delete-read")
    public String deleteReadNotifications(
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.deleteReadNotifications(user);
        redirectAttributes.addFlashAttribute("success", "Tất cả thông báo đã đọc đã được xóa");

        return "redirect:/notifications";
    }

    @GetMapping("/{id}")
    public String viewNotification(
            @PathVariable Long id,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationService.findById(id)
                .map(notification -> {
                    // Check if notification belongs to current user
                    if (!notification.getUser().getId().equals(user.getId())) {
                        redirectAttributes.addFlashAttribute("error", "You don't have permission to view this notification");
                        return "redirect:/notifications";
                    }

                    // Mark as read if unread
                    if (!notification.isRead()) {
                        notificationService.markAsRead(id);
                    }

                    model.addAttribute("notification", notification);
                    return "notifications/detail";
                })
                .orElse("redirect:/notifications");
    }

    @PostMapping("/mark-read/{id}")
    public String markAsRead(
            @PathVariable Long id,
            Principal principal,
            @RequestHeader(value = "X-Requested-With", defaultValue = "") String requestedWith,
            RedirectAttributes redirectAttributes) {

        try {
            // Verify ownership before marking as read
            notificationService.findById(id)
                .ifPresent(notification -> {
                    User currentUser = userService.findByUsername(principal.getName())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    if (!notification.getUser().getId().equals(currentUser.getId())) {
                        throw new RuntimeException("Unauthorized");
                    }

                    notificationService.markAsRead(id);
                });

            if ("XMLHttpRequest".equals(requestedWith)) {
                return "notifications/fragments/notification-item :: content";
            }

            redirectAttributes.addFlashAttribute("success", "Notification marked as read");
            return "redirect:/notifications";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to mark notification as read");
            return "redirect:/notifications";
        }
    }

    @PostMapping("/mark-unread/{id}")
    public String markAsUnread(
            @PathVariable Long id,
            Principal principal,
            @RequestHeader(value = "X-Requested-With", defaultValue = "") String requestedWith,
            RedirectAttributes redirectAttributes) {

        try {
            // Verify ownership before marking as unread
            notificationService.findById(id)
                .ifPresent(notification -> {
                    User currentUser = userService.findByUsername(principal.getName())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    if (!notification.getUser().getId().equals(currentUser.getId())) {
                        throw new RuntimeException("Unauthorized");
                    }

                    notificationService.markAsUnread(id);
                });

            if ("XMLHttpRequest".equals(requestedWith)) {
                return "notifications/fragments/notification-item :: content";
            }

            redirectAttributes.addFlashAttribute("success", "Notification marked as unread");
            return "redirect:/notifications";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to mark notification as unread");
            return "redirect:/notifications";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteNotification(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            // Verify ownership before deleting
            notificationService.findById(id)
                .ifPresent(notification -> {
                    User currentUser = userService.findByUsername(principal.getName())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    if (!notification.getUser().getId().equals(currentUser.getId())) {
                        throw new RuntimeException("Unauthorized");
                    }

                    notificationService.deleteNotification(id);
                });

            redirectAttributes.addFlashAttribute("success", "Notification deleted successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete notification");
        }

        return "redirect:/notifications";
    }

    // REST API endpoints for AJAX calls
    @GetMapping("/api/unread-count")
    @ResponseBody
    public Map<String, Object> getUnreadCount(Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        long unreadCount = notificationService.countUnreadByUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("count", unreadCount);
        response.put("success", true);

        return response;
    }

    @GetMapping("/api/recent")
    @ResponseBody
    public List<Map<String, Object>> getRecentNotifications(Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications = notificationService.findUnreadByUser(user);

        return notifications.stream()
                .limit(5)
                .map(this::convertToMap)
                .toList();
    }

    @PostMapping("/api/mark-read-batch")
    @ResponseBody
    public Map<String, Object> markMultipleAsRead(
            @RequestBody List<Long> notificationIds,
            Principal principal) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Verify ownership for all notifications
            List<Long> validIds = notificationService.findByUser(user).stream()
                    .filter(notification -> notificationIds.contains(notification.getId()))
                    .map(Notification::getId)
                    .toList();

            notificationService.markAsRead(validIds);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("markedCount", validIds.size());
            response.put("message", "Notifications marked as read");

            return response;

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to mark notifications as read");

            return response;
        }
    }

    // Helper method to convert Notification to Map for JSON response
    private Map<String, Object> convertToMap(Notification notification) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", notification.getId());
        map.put("title", notification.getTitle());
        map.put("message", notification.getMessage());
        map.put("type", notification.getType().getValue());
        map.put("isRead", notification.isRead());
        map.put("createdAt", notification.getCreatedAt());
        map.put("readAt", notification.getReadAt());
        map.put("actionUrl", notification.getActionUrl());
        return map;
    }
}
