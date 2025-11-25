package com.finance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    private static final String SYSTEM_PROMPT_TEXT = "You are an AI assistant integrated in a personal finance app.\n" +
            "Understand Vietnamese messages and SMARTLY extract income or expense transactions without requiring explicit keywords.\n"
            +
            "\n" +
            "Output strictly in JSON format:\n" +
            "{\n" +
            "  \"type\": \"income\" | \"expense\",\n" +
            "  \"category\": \"string\",\n" +
            "  \"amount\": number,\n" +
            "  \"date\": \"yyyy-MM-dd\",\n" +
            "  \"note\": \"string\"\n" +
            "}\n" +
            "\n" +
            "SMART DETECTION RULES:\n" +
            "- AUTOMATICALLY detect transactions even without \"chi\"/\"thu\" keywords.\n" +
            "- IMPLICIT EXPENSE: If the message starts with a description followed by a number (e.g., \"An sang 25000\"), TREAT AS EXPENSE.\n"
            +
            "- HANDLE UNACCENTED VIETNAMESE: \"an sang\" = \"ăn sáng\", \"com\" = \"cơm\", \"luong\" = \"lương\", \"xang\" = \"xăng\".\n"
            +
            "- If message contains amount + food/activity → assume EXPENSE\n" +
            "- If message contains amount + salary/income context → assume INCOME\n" +
            "- Food patterns: \"an sáng\", \"com trua\", \"pha\", \"cafe\", \"cà phê\", \"trà sữa\", \"ăn cơm\", \"bữa sáng\", \"bữa trưa\", \"bữa tối\", \"bún\", \"phở\", \"cơm\"\n"
            +
            "- Transport patterns: \"đi xe\", \"xăng\", \"grab\", \"taxi\", \"bus\"\n" +
            "- Shopping patterns: \"mua sắm\", \"shopee\", \"quần áo\", \"tạp hóa\"\n" +
            "- Income patterns: \"lương\", \"thưởng\", \"tiền\", \"nhận tiền\"\n" +
            "- Default: small amounts (<500k) without clear income context = EXPENSE\n" +
            "\n" +
            "Amount extraction:\n" +
            "- Extract any number that looks like money (ignore dots in formatting)\n" +
            "- Context matters: \"an sang 25000\" → 25000 expense\n" +
            "\n" +
            "Category mapping:\n" +
            "  * Food/Drinks: \"Ăn uống\"\n" +
            "  * Transport: \"Di chuyển\"\n" +
            "  * Shopping: \"Mua sắm\"\n" +
            "  * Entertainment: \"Giải trí\"\n" +
            "  * Bills: \"Hóa đơn\"\n" +
            "  * Health: \"Sức khỏe\"\n" +
            "  * Education: \"Giáo dục\"\n" +
            "  * Income: \"Lương\", \"Thưởng\", \"Đầu tư\", \"Freelance\", \"Kinh doanh\", \"Quà tặng\"\n" +
            "  * Others: \"Khác\"\n" +
            "\n" +
            "Date detection (Vietnam timezone UTC+7):\n" +
            "  * If no date mentioned → use TODAY's date\n" +
            "  * \"hôm nay\", \"nay\" → today\n" +
            "  * \"hôm qua\", \"qua\" → yesterday\n" +
            "  * DD/MM/YYYY formats\n" +
            "\n" +
            "Examples to understand:\n" +
            "- \"an sang 25000\" → {type: \"expense\", category: \"Ăn uống\", amount: 25000, note: \"Ăn sáng\"}\n" +
            "- \"pha 15000\" → {type: \"expense\", category: \"Ăn uống\", amount: 15000, note: \"Cà phê\"}\n" +
            "- \"lương 15trieu\" → {type: \"income\", category: \"Lương\", amount: 15000000, note: \"Lương tháng\"}\n" +
            "- \"50000 com trua\" → {type: \"expense\", category: \"Ăn uống\", amount: 50000, note: \"Cơm trưa\"}\n" +
            "\n" +
            "If no amount or unclear context → return {}.";

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public GeminiService(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.objectMapper = new ObjectMapper();
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        logger.info("✓ Gemini API Service initialized with Spring AI");
    }

    public Map<String, Object> extractTransactionFromMessage(String message) throws Exception {
        logger.debug("Extracting transaction from message: {}", message);

        try {
            // Get current date for context
            String currentDate = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).format(DateTimeFormatter.ISO_LOCAL_DATE);
            String currentDayOfWeek = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfWeek().toString();

            String fullPrompt = "\n\nIMPORTANT CONTEXT:" +
                    "\nCurrent date (Vietnam timezone): " + currentDate +
                    "\nCurrent day of week: " + currentDayOfWeek +
                    "\n\nUser message: " + message;

            SystemMessage systemMessage = new SystemMessage(SYSTEM_PROMPT_TEXT);
            UserMessage userMessage = new UserMessage(fullPrompt);
            Prompt prompt = new Prompt(java.util.List.of(systemMessage, userMessage));

            String response = chatModel.call(prompt).getResult().getOutput().getText();

            // Clean up response if it contains markdown code blocks
            if (response.contains("```json")) {
                response = response.replace("```json", "").replace("```", "").trim();
            } else if (response.contains("```")) {
                response = response.replace("```", "").trim();
            }

            return parseGeminiResponse(response);

        } catch (Exception e) {
            logger.error("Error calling Gemini API via Spring AI: {}", e.getMessage(), e);
            throw new Exception("Failed to process message with AI: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseGeminiResponse(String jsonText) throws Exception {
        try {
            logger.debug("Gemini response text: {}", jsonText);

            // Parse the JSON text into a map
            Map<String, Object> transactionData = objectMapper.readValue(jsonText, Map.class);

            // Validate and process the extracted data
            return validateAndProcessTransactionData(transactionData);

        } catch (Exception e) {
            logger.error("Error parsing Gemini response: {}", e.getMessage(), e);
            throw new Exception("Failed to parse AI response: " + e.getMessage());
        }
    }

    private Map<String, Object> validateAndProcessTransactionData(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Check if we have the required fields
            if (!data.containsKey("type") || !data.containsKey("amount")) {
                logger.warn("Missing required fields in AI response: {}", data);
                return new HashMap<>();
            }

            // Validate transaction type
            String type = String.valueOf(data.get("type")).toLowerCase();
            if (!type.equals("income") && !type.equals("expense")) {
                logger.warn("Invalid transaction type: {}", type);
                return new HashMap<>();
            }
            result.put("type", type);

            // Validate amount
            Object amountObj = data.get("amount");
            double amount;
            if (amountObj instanceof Number) {
                amount = ((Number) amountObj).doubleValue();
            } else {
                try {
                    amount = Double.parseDouble(String.valueOf(amountObj));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid amount format: {}", amountObj);
                    return new HashMap<>();
                }
            }

            if (amount <= 0) {
                logger.warn("Invalid amount value: {}", amount);
                return new HashMap<>();
            }
            result.put("amount", amount);

            // Process category
            String category = data.containsKey("category") ? String.valueOf(data.get("category")) : "Khác";
            result.put("category", category);

            // Process date - use Vietnam timezone (UTC+7)
            String date = processVietnameseDate(data.containsKey("date") ? String.valueOf(data.get("date")) : null);
            result.put("date", date);

            // Process note
            String note = data.containsKey("note") ? String.valueOf(data.get("note")) : category;
            result.put("note", note);

            logger.debug("Validated transaction data: {}", result);
            return result;

        } catch (Exception e) {
            logger.error("Error validating transaction data: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Processes Vietnamese date expressions and returns standardized date format.
     *
     * @param dateInput The date string from AI response, can be null
     * @return Date in yyyy-MM-dd format for Vietnam timezone
     */
    private String processVietnameseDate(String dateInput) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        try {
            // If no date provided, use today
            if (dateInput == null || dateInput.trim().isEmpty()) {
                logger.debug("No date provided, using today: {}", today);
                return today.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }

            dateInput = dateInput.trim().toLowerCase();

            // Handle common Vietnamese date expressions
            switch (dateInput) {
                case "hôm nay":
                case "nay":
                case "bây giờ":
                case "hiện tại":
                    logger.debug("Date expression '{}' maps to today: {}", dateInput, today);
                    return today.format(DateTimeFormatter.ISO_LOCAL_DATE);

                case "hôm qua":
                case "qua":
                    LocalDate yesterday = today.minusDays(1);
                    logger.debug("Date expression '{}' maps to yesterday: {}", dateInput, yesterday);
                    return yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE);

                case "hôm kia":
                    LocalDate dayBeforeYesterday = today.minusDays(2);
                    logger.debug("Date expression '{}' maps to day before yesterday: {}", dateInput,
                            dayBeforeYesterday);
                    return dayBeforeYesterday.format(DateTimeFormatter.ISO_LOCAL_DATE);

                case "mai":
                case "ngày mai":
                    LocalDate tomorrow = today.plusDays(1);
                    logger.debug("Date expression '{}' maps to tomorrow: {}", dateInput, tomorrow);
                    return tomorrow.format(DateTimeFormatter.ISO_LOCAL_DATE);

                default:
                    // Try to parse as standard date format
                    try {
                        LocalDate parsedDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
                        logger.debug("Successfully parsed ISO date: {} -> {}", dateInput, parsedDate);
                        return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                    } catch (Exception e) {
                        // Try common Vietnamese date formats
                        try {
                            // DD/MM/YYYY or DD/MM/YY
                            if (dateInput.matches("\\d{1,2}[\\/-]\\d{1,2}[\\/-]\\d{2,4}")) {
                                String cleanDate = dateInput.replaceAll("[^\\d\\/]", "");
                                String[] parts = cleanDate.split("[\\/-]");
                                int day = Integer.parseInt(parts[0]);
                                int month = Integer.parseInt(parts[1]);
                                int year = parts[2].length() == 2 ? 2000 + Integer.parseInt(parts[2])
                                        : Integer.parseInt(parts[2]);

                                LocalDate parsedDate = LocalDate.of(year, month, day);
                                logger.debug("Successfully parsed Vietnamese date format: {} -> {}", dateInput,
                                        parsedDate);
                                return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                            }

                            // Try other formats or fall back to today
                            logger.debug("Could not parse date '{}', using today: {}", dateInput, today);
                            return today.format(DateTimeFormatter.ISO_LOCAL_DATE);
                        } catch (Exception ex) {
                            logger.debug("All date parsing failed for '{}', using today: {}", dateInput, today);
                            return today.format(DateTimeFormatter.ISO_LOCAL_DATE);
                        }
                    }
            }
        } catch (Exception e) {
            logger.error("Error processing date '{}', using today: {}", dateInput, today, e);
            return today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public boolean isConfigured() {
        return true; // Spring AI handles configuration checks
    }
}
