package com.finance.form;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GoalForm {
    
    private String name ; 
    private BigDecimal targetAmount;
    private LocalDate targetDate;
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
