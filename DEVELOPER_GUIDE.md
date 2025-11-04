# Personal Finance Manager - Developer Onboarding Guide

Welcome to the Personal Finance Manager project! This guide provides all the information you need to get the project running locally, understand its structure, and contribute effectively.

## 1. Project Overview

### Purpose
This project is a web application for managing personal finances. It allows users to track income and expenses, create budgets, set savings goals, and view financial reports.

### Technology Stack
- **Backend**: Java 8, Spring Boot 2.1.3.RELEASE
- **Data Persistence**: Spring Data JPA, Hibernate      
- **Database**: MySQL (Production), H2 (Development)
- **Security**: Spring Security
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Build Tool**: Apache Maven

### Architecture
The application follows a standard layered MVC (Model-View-Controller) architecture:
- **Presentation Layer (`controller`, `form`, `templates` folders)**: Handles HTTP requests, binds user input, and renders the UI.
- **Business Logic Layer (`service` folder)**: Contains the core application logic, orchestrating data and operations.
- **Data Access Layer (`repository`, `domain` folders)**: Manages data persistence using Spring Data JPA and defines the domain model.

## 2. Local Development Setup

Follow these steps to set up and run the project on your local machine.

### Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Apache Maven**: Version 3.6 or higher.
- **MySQL**: Version 8.0 or higher.

### Step 1: Clone the Repository
```bash
git clone <your-repository-url>
cd PersonalFinanceManager
```

### Step 2: Configure the Database
1.  Create a MySQL database for the project.
    ```sql
    CREATE DATABASE personal_finance_db;
    ```
2.  Open the `src/main/resources/application.properties` file.
3.  Update the following properties with your MySQL credentials:
    ```properties
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    ```
    The application is configured to connect to a database named `personal_finance_db` on `localhost:3306`.

*(Optional: For a quick start without MySQL, you can switch to the in-memory H2 database by commenting out the MySQL properties and uncommenting the H2 properties in `application.properties`.)*

### Step 3: Build and Run the Application
You can run the application using the Maven Spring Boot plugin.

```bash
# Build the project and run all tests
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8083`.

## 3. Folder Structure

Here is a breakdown of the key directories and files in the project:

```
PersonalFinanceManager/
├── pom.xml                     # Maven build configuration
├── mysql-setup.sql             # Initial database schema setup script
├── src/
│   ├── main/
│   │   ├── java/com/finance/   # Root package for all Java source code
│   │   │   ├── config/         # Spring configuration (Security, Web, etc.)
│   │   │   ├── controller/     # MVC controllers for handling web requests
│   │   │   ├── domain/         # JPA entities (the data model)
│   │   │   ├── exception/      # Custom application-specific exceptions
│   │   │   ├── form/           # Form objects for handling user input
│   │   │   ├── repository/     # Spring Data JPA repositories for DB access
│   │   │   └── service/        # Business logic and service layer
│   │   └── resources/
│   │       ├── application.properties  # Application configuration settings
│   │       ├── static/                 # Static assets (CSS, JavaScript, images)
│   │       └── templates/              # Thymeleaf HTML templates for the UI
│   └── test/                   # Unit and integration tests
└── DEVELOPER_GUIDE.md          # This file
```

## 4. Code Style Guide

To maintain consistency, please adhere to the following conventions.

### Naming Conventions
- **Classes and Interfaces**: `PascalCase` (e.g., `TransactionService`)
- **Methods and Variables**: `camelCase` (e.g., `getTotalIncomeByUser`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `MAX_POOL_SIZE`)
- **JPA Entities**: Use `@Entity` and map to tables (e.g., `User.java`)
- **Repositories**: Extend Spring Data interfaces (e.g., `TransactionRepository`)

### Formatting
- **Indentation**: Use 4 spaces for indentation.
- **Braces**: Use the "one true brace" style (opening brace on the same line).
- **Organization**: Keep methods concise and focused on a single responsibility. Use private helper methods to break down complex logic.

### Best Practices
- **Dependency Injection**: Use `@Autowired` for constructor or field injection.
- **Service Layer**: All business logic should reside in service classes. Controllers should be thin and delegate calls to the service layer.
- **Use Forms/DTOs**: Do not pass JPA entities directly to and from controllers. Use dedicated form objects (e.g., `TransactionForm`) for validation and data transfer.
- **Exception Handling**: Use custom exceptions for specific error cases (e.g., `TransactionNotFoundException`).
- **Immutability**: Use `final` where appropriate.

## 5. Contribution Guide

### Reporting Issues
- Use the issue tracker to report bugs or suggest features.
- Provide a clear description, steps to reproduce, and any relevant logs.

### Adding New Features
1.  **Create a Branch**: Start by creating a new feature branch from the `main` branch.
    ```bash
    git checkout -b feature/your-new-feature
    ```
2.  **Follow the Architecture**:
    - Add/update domain objects in `src/main/java/com/finance/domain`.
    - Create or update the repository interface in `src/main/java/com/finance/repository`.
    - Implement the business logic in a service class in `src/main/java/com/finance/service`.
    - Expose the functionality via a controller in `src/main/java/com/finance/controller`.
    - Create or update Thymeleaf templates in `src/main/resources/templates`.
3.  **Write Tests**: Add unit tests for new service-layer logic in the `src/test/java` directory.
4.  **Submit a Pull Request**:
    - Push your branch and open a pull request against the `main` branch.
    - Ensure all automated checks and tests pass.
    - Provide a clear description of the changes in your PR.
