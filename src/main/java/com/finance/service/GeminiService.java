package com.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String SYSTEM_PROMPT = "You are an AI assistant integrated in a personal finance app.\n" +
            "Understand Vietnamese messages and extract income or expense transactions.\n" +
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
            "Rules:\n" +
            "- Identify if it's income (e.g. \"nhận lương\", \"được thưởng\", \"thu nhập\") or expense (\"mua\", \"ăn\", \"trả\", \"chi tiêu\").\n"
            +
            "- Extract amount (number only, remove any currency symbols).\n" +
            "- Guess appropriate category based on context:\n" +
            "  * For expenses: \"Ăn uống\", \"Di chuyển\", \"Mua sắm\", \"Giải trí\", \"Hóa đơn\", \"Sức khỏe\", \"Giáo dục\", \"Khác\"\n"
            +
            "  * For income: \"Lương\", \"Thưởng\", \"Đầu tư\", \"Freelance\", \"Kinh doanh\", \"Quà tặng\", \"Khác\"\n"
            +
            "- For date detection:\n" +
            "  * If no date mentioned → use TODAY's date (current date)\n" +
            "  * Handle Vietnamese date expressions:\n" +
            "    - \"hôm nay\", \"bây giờ\", \"nay\" → today\n" +
            "    - \"hôm qua\", \"qua\" → yesterday\n" +
            "    - \"hôm kia\" → day before yesterday\n" +
            "    - \"mai\", \"ngày mai\" → tomorrow\n" +
            "    - \"tuần này\" → appropriate date this week\n" +
            "    - \"tháng này\" → appropriate date this month\n" +
            "  * Handle specific date formats:\n" +
            "    - DD/MM/YYYY, DD-MM-YYYY, DD/MM/YY\n" +
            "    - \"ngày X tháng Y\", \"X/Y\", \"X- Y\"\n" +
            "    - \"thứ Hai\", \"thứ Ba\" etc. → this week's corresponding day\n" +
            "- Extract note/description from the message context.\n" +
            "- If no amount or unclear transaction → return {}.\n" +
            "- IMPORTANT: Always use current Vietnam timezone (UTC+7) for date calculations.";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // API key injected from application.yaml
    @Value("${gemini.api.key:#{null}}")
    private String geminiApiKey;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        if (isConfigured()) {
            logger.info("✓ Gemini API Service initialized successfully with API key from .env file");
        } else {
            logger.warn("⚠ Gemini API key is not configured. Please set GEMINI_API_KEY in your .env file");
        }
    }

    public Map<String, Object> extractTransactionFromMessage(String message) throws Exception {
        logger.debug("Extracting transaction from message: {}", message);

        try {
            // Build the request payload for Gemini API
            Map<String, Object> requestBody = buildGeminiRequest(message);

            // Set up HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build the complete URL with API key
            String urlWithKey = GEMINI_API_URL + "?key=" + geminiApiKey;

            // Create HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Make the API call
            ResponseEntity<String> response = restTemplate.postForEntity(urlWithKey, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseGeminiResponse(response.getBody());
            } else {
                logger.error("Gemini API returned error status: {}", response.getStatusCode());
                return new HashMap<>();
            }

        } catch (Exception e) {
            logger.error("Error calling Gemini API: {}", e.getMessage(), e);
            throw new Exception("Failed to process message with AI: " + e.getMessage());
        }
    }

    private Map<String, Object> buildGeminiRequest(String message) {
        // Get current date for context
        String currentDate = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String currentDayOfWeek = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfWeek().toString();

        // Create the main content structure with date context
        Map<String, Object> textPart = new HashMap<>();
        String fullPrompt = SYSTEM_PROMPT +
                "\n\nIMPORTANT CONTEXT:" +
                "\nCurrent date (Vietnam timezone): " + currentDate +
                "\nCurrent day of week: " + currentDayOfWeek +
                "\n\nUser message: " + message;
        textPart.put("text", fullPrompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[] { textPart });

        // Build generation config
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.1);
        generationConfig.put("maxOutputTokens", 250);
        generationConfig.put("responseMimeType", "application/json");

        // Build the complete request
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[] { content });
        requestBody.put("generationConfig", generationConfig);

        return requestBody;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseGeminiResponse(String responseBody) throws Exception {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Navigate to the content text
            JsonNode candidates = rootNode.path("candidates");
            if (!candidates.isArray() || candidates.size() == 0) {
                logger.warn("No candidates in Gemini response");
                return new HashMap<>();
            }

            JsonNode content = candidates.get(0).path("content");
            JsonNode parts = content.path("parts");
            if (!parts.isArray() || parts.size() == 0) {
                logger.warn("No parts in Gemini response");
                return new HashMap<>();
            }

            String jsonText = parts.get(0).path("text").asText();
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
        return geminiApiKey != null && !geminiApiKey.trim().isEmpty();
    }
}
