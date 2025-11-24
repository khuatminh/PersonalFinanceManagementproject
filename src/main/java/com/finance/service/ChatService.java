package com.finance.service;

import com.finance.domain.Transaction;
import com.finance.domain.Category;
import com.finance.domain.User;
import com.finance.repository.TransactionRepository;
import com.finance.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final GeminiService geminiService;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public ChatService(GeminiService geminiService,
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository) {
        this.geminiService = geminiService;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Map<String, String> processMessage(String message, User user) {
        Map<String, String> response = new HashMap<>();

        try {
            logger.info("Processing message from user {}: {}", user.getUsername(), message);

            // Check if Gemini service is configured
            if (!geminiService.isConfigured()) {
                response.put("message", "❌ Dịch vụ AI chưa được cấu hình. Vui lòng liên hệ quản trị viên.");
                return response;
            }

            // Extract transaction data using AI
            Map<String, Object> transactionData = geminiService.extractTransactionFromMessage(message);

            // Check if AI detected a valid transaction
            if (transactionData.isEmpty()) {
                response.put("message",
                        "❌ Không nhận diện được giao dịch. Vui lòng thử lại với thông tin rõ ràng hơn (ví dụ: 'chi 50000 ăn trưa' hoặc 'thu 5000000 lương').");
                return response;
            }

            // Save the transaction to database
            Transaction savedTransaction = saveTransaction(transactionData, user);

            // Format success response
            String successMessage = String.format("✅ Đã thêm giao dịch: %s - %,.0f₫ - %s",
                    savedTransaction.getCategory().getName(),
                    savedTransaction.getAmount(),
                    savedTransaction.getDescription());

            response.put("message", successMessage);
            response.put("transactionId", savedTransaction.getId().toString());
            response.put("type", savedTransaction.getType().toString());
            response.put("amount", savedTransaction.getAmount().toString());
            response.put("category", savedTransaction.getCategory().getName());

            logger.info("Successfully processed transaction: {}", savedTransaction);

        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
            response.put("message", "❌ Đã xảy ra lỗi khi xử lý tin nhắn: " + e.getMessage());
        }

        return response;
    }

    private Transaction saveTransaction(Map<String, Object> transactionData, User user) {
        try {
            // Extract data from map
            String type = (String) transactionData.get("type");
            Double amount = (Double) transactionData.get("amount");
            String categoryName = (String) transactionData.get("category");
            String dateStr = (String) transactionData.get("date");
            String description = (String) transactionData.get("note");

            // Convert to appropriate types
            Transaction.TransactionType transactionType = "income".equals(type) ? Transaction.TransactionType.INCOME
                    : Transaction.TransactionType.EXPENSE;

            BigDecimal amountBigDecimal = BigDecimal.valueOf(amount);

            // Parse date - use Vietnam timezone
            LocalDateTime transactionDate = parseTransactionDate(dateStr);

            // Find or create category
            Category category = findOrCreateCategory(categoryName, transactionType, user);

            // Create transaction
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setAmount(amountBigDecimal);
            transaction.setType(transactionType);
            transaction.setTransactionDate(transactionDate);
            transaction.setUser(user);
            transaction.setCategory(category);
            transaction.setNotes("Tự động thêm từ chatbot: " + description);

            // Save transaction
            Transaction savedTransaction = transactionRepository.save(transaction);

            logger.debug("Saved transaction: {}", savedTransaction);
            return savedTransaction;

        } catch (Exception e) {
            logger.error("Error saving transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save transaction: " + e.getMessage());
        }
    }

    private LocalDateTime parseTransactionDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            // Use current time in Vietnam timezone instead of start of day (00:00)
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            // Combine the date from AI with the actual current time
            return date.atTime(now.toLocalTime());
        } catch (Exception e) {
            logger.warn("Invalid date format: {}, using current date and time in Vietnam timezone", dateStr);
            return LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        }
    }

    private Category findOrCreateCategory(String categoryName, Transaction.TransactionType transactionType, User user) {
        try {
            // Try to find existing category by name
            Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(categoryName.trim());

            if (existingCategory.isPresent()) {
                Category category = existingCategory.get();
                logger.debug("Found existing category: {}", category.getName());
                return category;
            }

            // Create new category if not found
            Category.CategoryType categoryType = transactionType == Transaction.TransactionType.INCOME
                    ? Category.CategoryType.INCOME
                    : Category.CategoryType.EXPENSE;

            Category newCategory = new Category();
            newCategory.setName(categoryName.trim());
            newCategory.setType(categoryType);
            newCategory.setColor(generateCategoryColor(transactionType));
            newCategory.setDescription("Tự động tạo từ chatbot");

            Category savedCategory = categoryRepository.save(newCategory);
            logger.debug("Created new category: {}", savedCategory.getName());
            return savedCategory;

        } catch (Exception e) {
            logger.error("Error finding or creating category: {}", e.getMessage(), e);
            // Fallback to a default category
            return getDefaultCategory(transactionType);
        }
    }

    private String generateCategoryColor(Transaction.TransactionType transactionType) {
        return transactionType == Transaction.TransactionType.INCOME ? "#28a745" : "#dc3545";
    }

    private Category getDefaultCategory(Transaction.TransactionType transactionType) {
        String defaultCategoryName = transactionType == Transaction.TransactionType.INCOME ? "Lương" : "Ăn uống";
        Category.CategoryType categoryType = transactionType == Transaction.TransactionType.INCOME
                ? Category.CategoryType.INCOME
                : Category.CategoryType.EXPENSE;

        // Try to find the default category
        Optional<Category> defaultCategory = categoryRepository.findByNameIgnoreCase(defaultCategoryName);
        if (defaultCategory.isPresent()) {
            return defaultCategory.get();
        }

        // Create default category if it doesn't exist
        Category category = new Category();
        category.setName(defaultCategoryName);
        category.setType(categoryType);
        category.setColor(generateCategoryColor(transactionType));
        category.setDescription("Danh mục mặc định");

        return categoryRepository.save(category);
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("geminiConfigured", geminiService.isConfigured());
        status.put("serviceActive", true);
        return status;
    }
}
