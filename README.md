# Personal Finance Manager

A comprehensive Spring Boot web application for managing personal finances, including income tracking, expense management, budget planning, and savings goals with advanced reporting capabilities.

## 🚀 Project Overview

This project demonstrates advanced Object-Oriented Programming concepts, clean architecture principles, and modern web development practices. Built with Spring Boot 2.1.3 and Java 8, it showcases enterprise-level development patterns including dependency injection, layered architecture, and comprehensive security implementation.

### Key Highlights
- **Clean Architecture**: Follows SOLID principles with clear separation of concerns
- **Comprehensive Documentation**: Extensive JavaDoc and inline comments throughout
- **Security-First Design**: BCrypt encryption, CSRF protection, and role-based access control
- **Responsive Design**: Mobile-friendly interface using Bootstrap 5
- **Financial Precision**: BigDecimal usage for accurate monetary calculations
- **Audit Trail**: Complete transaction history with timestamps

## 🏗️ Architecture Overview

### Technology Stack
- **Backend Framework**: Spring Boot 2.1.3 with Spring MVC
- **Language**: Java 8 with advanced OOP concepts
- **Database**: MySQL 8.0+ (Production), H2 (Development/Testing)
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security 5 with BCrypt password encoding
- **Frontend**: Thymeleaf templating, Bootstrap 5, vanilla JavaScript
- **Build Tool**: Maven 3.6+
- **Testing**: JUnit 5, Mockito, Spring Boot Test

### Layered Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  Controllers, Forms, Views (Thymeleaf Templates)            │
├─────────────────────────────────────────────────────────────┤
│                    Business Logic Layer                      │
│  Services, Validation, Business Rules, Calculations         │
├─────────────────────────────────────────────────────────────┤
│                    Data Access Layer                         │
│  Repositories, JPA Entities, Database Operations            │
├─────────────────────────────────────────────────────────────┤
│                    Database Layer                            │
│  MySQL/H2 Database, Schema, Indexes, Constraints           │
└─────────────────────────────────────────────────────────────┘
```

### Package Structure
```
src/main/java/com/finance/
├── PersonalFinanceManagerApplication.java    # Main application entry point
├── config/                                   # Configuration classes
│   ├── SecurityConfig.java                  # Spring Security configuration
│   ├── WebConfig.java                       # Web MVC configuration
│   ├── DatabaseMigration.java               # Database setup and migration
│   └── DataInitializer.java                 # Initial data loading
├── controller/                               # Web controllers (MVC)
│   ├── HomeController.java                  # Landing page and authentication
│   ├── UserController.java                  # User management operations
│   ├── TransactionController.java           # Transaction CRUD operations
│   ├── BudgetController.java                # Budget management
│   ├── GoalController.java                  # Savings goal management
│   ├── DashboardController.java             # Financial dashboard
│   ├── ReportController.java                # Financial reporting
│   ├── AdminController.java                 # Administrative functions
│   └── GlobalControllerAdvice.java          # Global exception handling
├── domain/                                   # JPA entities (Domain models)
│   ├── User.java                            # User account entity
│   ├── Transaction.java                     # Financial transaction entity
│   ├── Category.java                        # Transaction category entity
│   ├── Budget.java                          # Budget planning entity
│   ├── Goal.java                            # Savings goal entity
│   ├── Role.java                            # User role entity
│   └── Notification.java                    # System notification entity
├── repository/                               # Data access layer
│   ├── UserRepository.java                  # User data operations
│   ├── TransactionRepository.java           # Transaction data operations
│   ├── CategoryRepository.java              # Category data operations
│   ├── BudgetRepository.java                # Budget data operations
│   ├── GoalRepository.java                  # Goal data operations
│   ├── RoleRepository.java                  # Role data operations
│   └── NotificationRepository.java          # Notification data operations
├── service/                                  # Business logic layer
│   ├── UserService.java                     # User business operations
│   ├── TransactionService.java              # Transaction business logic
│   ├── CategoryService.java                 # Category management
│   ├── BudgetService.java                   # Budget planning logic
│   ├── GoalService.java                     # Goal tracking logic
│   ├── ReportService.java                   # Financial reporting
│   ├── NotificationService.java             # Notification management
│   └── UserDetailsServiceImpl.java          # Spring Security integration
├── form/                                     # Form DTOs and validation
│   ├── UserRegistrationForm.java            # User registration form
│   ├── TransactionForm.java                 # Transaction input form
│   ├── BudgetForm.java                      # Budget creation form
│   ├── GoalForm.java                        # Goal setting form
│   └── PasswordChangeForm.java              # Password change form
├── exception/                                # Custom exception classes
│   ├── UserNotFoundException.java           # User-related exceptions
│   ├── DuplicateUserException.java          # Duplicate user handling
│   ├── InvalidPasswordException.java        # Password validation errors
│   └── TransactionNotFoundException.java    # Transaction-related errors
├── utils/                                    # Utility classes
│   ├── FinancialCalculationUtils.java       # Financial calculations
│   └── ColorUtils.java                      # UI color utilities
└── utility/                                 # Additional utilities
    └── [Legacy utility classes]
```

## 🎯 Features

### User Management
- User registration and authentication
- Role-based access control (USER/ADMIN)
- Profile management and password changes
- BCrypt password encryption

### Financial Management
- **Transaction Tracking**: Add, edit, delete, and search income/expense transactions
- **Categorization**: Organize transactions by customizable categories
- **Budget Planning**: Set and track budgets with real-time monitoring
- **Goal Setting**: Create and monitor savings goals with progress tracking

### Analytics & Reporting
- Financial dashboard with real-time statistics
- Category-wise spending analysis
- Budget progress tracking with visual indicators
- Goal completion monitoring

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Maven 3.6 or higher
- MySQL 5.7 or higher (for production)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PersonalFinanceManager
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE personal_finance_db;
   ```
   Update database credentials in `src/main/resources/application.properties`

3. **Build and Run**
   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

4. **Access the Application**
   - URL: http://localhost:8080
   - Demo Account: admin / admin123

### Development with H2 (For Testing)
For development without MySQL, uncomment the H2 configuration in `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

## 📚 API Endpoints

### Authentication
- `POST /login` - User login
- `POST /user/register` - User registration

### Transactions
- `GET /transactions` - List all transactions
- `POST /transactions/add` - Add new transaction
- `GET /transactions/edit/{id}` - Edit transaction
- `POST /transactions/edit/{id}` - Update transaction
- `GET /transactions/delete/{id}` - Delete transaction

### Dashboard
- `GET /dashboard` - Main dashboard
- `GET /api/financial-summary` - Financial summary API

### User Management
- `GET /user/profile` - View profile
- `POST /user/update` - Update profile
- `POST /user/change-password` - Change password

## 🎨 Frontend

### Technologies Used
- **Bootstrap 5**: Responsive UI framework
- **Thymeleaf**: Server-side templating engine
- **JavaScript**: Interactive features and AJAX

### Key Pages
- **Dashboard**: Financial overview with statistics
- **Login/Register**: User authentication
- **Transactions**: Transaction management interface
- **Profile**: User profile and settings

## 🔐 Security

### Authentication & Authorization
- Form-based authentication with Spring Security
- Role-based access control
- Session management
- CSRF protection

### Password Security
- BCrypt password hashing
- Password strength validation
- Secure password change functionality

## 📊 Database Schema

### Core Entities
- **users**: User accounts and authentication
- **categories**: Transaction categories
- **transactions**: Financial transactions
- **budgets**: Budget planning and tracking
- **goals**: Savings goals management

### Relationships
- Users have many transactions, budgets, and goals
- Transactions belong to categories
- Budgets can be associated with specific categories

## 🧪 Testing

### Manual Testing Checklist
- [ ] User registration works correctly
- [ ] Login functionality with valid/invalid credentials
- [ ] Dashboard displays correct financial data
- [ ] Transaction CRUD operations
- [ ] Budget creation and tracking
- [ ] Goal setting and progress monitoring
- [ ] Profile management
- [ ] Password change functionality
- [ ] Role-based access control

### Automated Testing (Future Enhancement)
```bash
mvn test
```

## 🔧 Configuration

### Application Properties
Key configuration options in `src/main/resources/application.properties`:
- Database connection settings
- JPA/Hibernate configuration
- Security settings
- Development tools configuration

### Security Configuration
Security rules configured in `SecurityConfig.java`:
- Public endpoints: `/`, `/login`, `/register`, static resources
- Authenticated endpoints: `/dashboard`, `/transactions/**`, `/budgets/**`, `/goals/**`
- Admin endpoints: `/admin/**`

## 🚀 Deployment

### Production Deployment
1. **Build the application**
   ```bash
   mvn clean package
   ```

2. **Run the JAR file**
   ```bash
   java -jar target/personal-finance-manager-1.0.0.jar
   ```

3. **Configure reverse proxy** (nginx/Apache) if needed

### Environment Variables
- `DATABASE_URL`: Database connection URL
- `DATABASE_USERNAME`: Database username
- `DATABASE_PASSWORD`: Database password
- `SPRING_PROFILES_ACTIVE`: Active profile (dev/prod)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 Development Notes

### OOP Principles Demonstrated
- **Encapsulation**: Private fields with public getters/setters
- **Inheritance**: Domain entities extend base classes
- **Polymorphism**: Different transaction types and goal statuses
- **Abstraction**: Service layer abstracts business logic
- **SOLID Principles**: Single responsibility, dependency injection

### Design Patterns Used
- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic separation
- **DTO Pattern**: Form objects for data transfer
- **Dependency Injection**: Spring's IoC container

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.


---

**Built for OOP Class Assignment**
**Version**: 1.0.0
**Last Updated**: 2024