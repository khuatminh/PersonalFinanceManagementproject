package com.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Personal Finance Manager Application - Main Entry Point
 *
 * This is a comprehensive Spring Boot web application for managing personal finances,
 * including income tracking, expense management, budget planning, and savings goals.
 *
 * @author Personal Finance Manager Team
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
public class PersonalFinanceManagerApplication {

    /**
     * Main method to bootstrap the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(PersonalFinanceManagerApplication.class, args);
    }
}