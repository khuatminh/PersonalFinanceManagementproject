package com.finance.form;

import com.finance.domain.Transaction;
import com.finance.domain.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TransactionForm {

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    Transaction.TransactionType type;

    LocalDateTime transactionDate;
    String notes;

    @NotNull(message = "Category is required")
    Long categoryId;

    // Default constructor
    public TransactionForm() {
        this.transactionDate = LocalDateTime.now();
        this.type = Transaction.TransactionType.EXPENSE;
    }

    // Constructor for editing existing transaction
    public TransactionForm(Transaction transaction) {
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.transactionDate = transaction.getTransactionDate();
        this.notes = transaction.getNotes();
        this.categoryId = transaction.getCategory() != null ? transaction.getCategory().getId() : null;
    }

}