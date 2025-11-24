package com.finance.interceptor;

import com.finance.domain.User;
import com.finance.service.NotificationService;
import com.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor to add notification data to all templates
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationInterceptor implements HandlerInterceptor {

    private final NotificationService notificationService;
    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && !isRedirectView(modelAndView)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                try {
                    User user = userService.findByUsername(auth.getName())
                            .orElse(null);

                    if (user != null) {
                        // Add unread notification count
                        long unreadCount = notificationService.countUnreadByUser(user);
                        modelAndView.addObject("unreadNotificationCount", unreadCount);

                        log.debug("Added {} unread notifications to model for user {}", unreadCount, auth.getName());
                    }
                } catch (Exception e) {
                    // Don't let notification errors break the page
                    log.warn("Could not load notification count for user {}: {}", auth.getName(), e.getMessage());
                }
            }
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        return modelAndView.getViewName() != null
                && modelAndView.getViewName().startsWith("redirect:");
    }
}