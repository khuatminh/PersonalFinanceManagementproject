package com.finance.utils;

import java.util.List;

public class ColorUtils {

    private static final List<String> EXPENSE_COLORS = List.of(
            "#ef4444", "#f97316", "#f59e0b", "#eab308", "#84cc16",
            "#22c55e", "#10b981", "#14b8a6", "#06b6d4", "#0ea5e9",
            "#3b82f6", "#6366f1", "#8b5cf6", "#a855f7", "#d946ef",
            "#ec4899", "#f43f5e", "#fb7185", "#fda4af", "#fecaca");

    private static final List<String> INCOME_COLORS = List.of(
            "#10b981", "#059669", "#047857", "#065f46", "#064e3b",
            "#22c55e", "#16a34a", "#15803d", "#166534", "#14532d",
            "#84cc16", "#65a30d", "#4d7c0f", "#365314", "#1a2e05",
            "#34d399", "#6ee7b7", "#9ca3af", "#d1d5db", "#e5e7eb");

    public static String getColor(int index, String type) {
        List<String> colors = "income".equalsIgnoreCase(type) ? INCOME_COLORS : EXPENSE_COLORS;
        return colors.get(index % colors.size());
    }
}
