package com.finance;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersonalFinanceManagerApplication {

    /**
     * Main method to bootstrap the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        // Load environment variables from .env file
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing() // Don't fail if .env file is missing
                    .load();

            // Set environment variables so Spring can access them
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

            System.out.println("✓ Environment variables loaded from .env file");
        } catch (Exception e) {
            System.err.println("⚠ Warning: Could not load .env file - " + e.getMessage());
        }

        SpringApplication.run(PersonalFinanceManagerApplication.class, args);
    }
}