package com.finance.controller;

import com.finance.domain.User;
import com.finance.service.TransactionService;
import com.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    TransactionService transactionService;
    UserService userService;

    @GetMapping
    public String getReports(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = today.with(TemporalAdjusters.lastDayOfMonth());

        return generateReportData(user, startDate, endDate, model);
    }

    @PostMapping("/generate")
    public String generateReport(@RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        return generateReportData(user, startDate, endDate, model);
    }

    private String generateReportData(User user, LocalDate startDate, LocalDate endDate, Model model) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        TransactionService.TransactionStatistics stats = transactionService.getTransactionStatisticsForDateRange(user,
                startDateTime, endDateTime);
        List<Object[]> expenseCategorySummary = transactionService.getExpenseCategorySummaryForDateRange(user,
                startDateTime, endDateTime);
        List<Object[]> incomeCategorySummary = transactionService.getIncomeCategorySummaryForDateRange(user,
                startDateTime, endDateTime);

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", stats.getTotalIncome());
        summary.put("totalExpenses", stats.getTotalExpenses());
        summary.put("netSavings", stats.getBalance());
        summary.put("expenseCategorySummary", expenseCategorySummary);
        summary.put("incomeCategorySummary", incomeCategorySummary);

        // Prepare chart data
        List<String> expenseLabels = new ArrayList<>();
        List<BigDecimal> expenseValues = new ArrayList<>();
        for (Object[] item : expenseCategorySummary) {
            expenseLabels.add((String) item[0]);
            expenseValues.add((BigDecimal) item[2]);
        }

        List<String> incomeLabels = new ArrayList<>();
        List<BigDecimal> incomeValues = new ArrayList<>();
        for (Object[] item : incomeCategorySummary) {
            incomeLabels.add((String) item[0]);
            incomeValues.add((BigDecimal) item[2]);
        }

        model.addAttribute("summary", summary);
        model.addAttribute("expensePieChartLabels", expenseLabels);
        model.addAttribute("expensePieChartData", expenseValues);
        model.addAttribute("incomePieChartLabels", incomeLabels);
        model.addAttribute("incomePieChartData", incomeValues);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "reports/index";
    }
}
