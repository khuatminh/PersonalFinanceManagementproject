package com.finance.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

public class GoalForm {

    @NotBlank(message = "Goal name is required")
    @Size(max = 100, message = "Goal name must not exceed 100 characters")
    private String name ; 

    @NotNull(message = "Target amount is required")
    @DecimalMin(value = "0.01", message = "Target amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Please enter a valid amount")
    private BigDecimal targetAmount;

    @NotNull(message = "Target date is required")
    @Future(message = "Target date must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description ;

    public GoalForm() {
    }

    public GoalForm(String name ,BigDecimal targetAmount , LocalDate targetDate ,String description ) {
        this.name = name ;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
        this.description = description;
   }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getTargetAmount() {
        return targetAmount;
    }
    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }
    public LocalDate getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}
