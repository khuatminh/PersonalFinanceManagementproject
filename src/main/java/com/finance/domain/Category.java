package com.finance.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"transactions"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    String name;

    @Column(length = 500)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CategoryType type;

    @Column(nullable = false)
    String color = "#007bff";

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Transaction> transactions = new ArrayList<>();

    public Category(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    public Category(String name, CategoryType type, String color) {
        this.name = name;
        this.type = type;
        this.color = color;
    }

    public enum CategoryType {
        INCOME("Income"),
        EXPENSE("Expense");

        private final String displayName;

        CategoryType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}