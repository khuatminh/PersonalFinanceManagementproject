package com.finance.controller;

import com.finance.domain.User;
import com.finance.service.ChatService;
import com.finance.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> processMessage(@Valid @RequestBody ChatRequest request, Principal principal) {
        try {
            // Get the actual authenticated user from Spring Security
            if (principal == null) {
                logger.error("No authenticated user found");
                ChatResponse errorResponse = new ChatResponse("❌ Bạn cần đăng nhập để sử dụng tính năng này.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            User actualUser = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));

            logger.info("Received chat message from user '{}': {}", actualUser.getUsername(), request.getMessage());

            // Process the message through chat service
            Map<String, String> result = chatService.processMessage(request.getMessage(), actualUser);

            ChatResponse response = new ChatResponse(result.get("message"));

            // Add additional fields if available
            if (result.containsKey("transactionId")) {
                response.setTransactionId(result.get("transactionId"));
            }
            if (result.containsKey("type")) {
                response.setType(result.get("type"));
            }
            if (result.containsKey("amount")) {
                response.setAmount(result.get("amount"));
            }
            if (result.containsKey("category")) {
                response.setCategory(result.get("category"));
            }

            logger.info("Successfully processed message for user: {}", actualUser.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing chat message: {}", e.getMessage(), e);
            ChatResponse errorResponse = new ChatResponse("❌ Đã xảy ra lỗi khi xử lý tin nhắn. Vui lòng thử lại sau.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        try {
            Map<String, Object> status = chatService.getStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Error getting chat service status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get service status"));
        }
    }

    public static class ChatRequest {

        @NotBlank(message = "Message cannot be blank")
        private String message;

        public ChatRequest() {
        }

        public ChatRequest(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ChatResponse {

        private String message;
        private String transactionId;
        private String type;
        private String amount;
        private String category;

        public ChatResponse() {
        }

        public ChatResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
