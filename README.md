# 💰 Personal Finance Management System

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.1.0-green.svg)](https://spring.io/projects/spring-ai)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://spring.io/projects/spring-boot)

> A comprehensive, AI-powered personal finance management web application built with modern Java 21, Spring Boot 3.4.0, and Spring AI. Features intelligent transaction recording, budget planning, goal tracking, and advanced financial analytics with Vietnamese language support.

## 🌟 Key Features

### 🤖 AI-Powered Transaction Management
- **Natural Language Processing**: Record transactions using conversational Vietnamese
- **Smart Transaction Extraction**: AI automatically identifies amount, category, and description
- **Auto-categorization**: Intelligent category suggestions based on transaction patterns
- **Spring AI Integration**: Seamless integration with Google Gemini via Spring AI framework

### 💳 Comprehensive Financial Management
- **Transaction Tracking**: Complete income/expense management with detailed categorization
- **Advanced Search & Filtering**: Find transactions by date, type, category, or keywords
- **Real-time Statistics**: Up-to-the-minute financial overview and insights
- **Multi-currency Support**: Vietnamese Dong (₫) formatting and localization

### 📊 Budget Planning & Monitoring
- **Flexible Budget Creation**: Set budgets for specific categories or time periods
- **Real-time Progress Tracking**: Visual indicators for budget utilization
- **Intelligent Alerts**: Automated notifications when approaching budget limits
- **Historical Analysis**: Compare current spending with historical patterns

### 🎯 Goal Setting & Achievement
- **Savings Goals**: Create and track multiple financial goals simultaneously
- **Progress Visualization**: Interactive charts showing goal completion status
- **Milestone Tracking**: Set and achieve intermediate milestones
- **Deadline Management**: Smart reminders for goal target dates

### 📈 Advanced Analytics & Reporting
- **Interactive Dashboard**: Comprehensive financial overview with real-time data
- **Category-wise Analysis**: Detailed spending patterns by category
- **Time-based Reports**: Monthly, quarterly, and yearly financial reports
- **Visual Data Representation**: Chart.js integration for beautiful, interactive charts

### 🔐 Enterprise-Grade Security
- **Spring Security 6**: Modern, comprehensive security framework
- **BCrypt Encryption**: Military-grade password hashing
- **Role-based Access Control**: USER and ADMIN role management
- **CSRF Protection**: Cross-site request forgery prevention
- **Session Management**: Secure session handling with configurable timeouts

### 🎨 Modern User Experience
- **Responsive Design**: Mobile-first Bootstrap 5 interface
- **Real-time Updates**: Dynamic content updates without page refreshes
- **Intuitive Navigation**: User-friendly interface with breadcrumb navigation
- **Accessibility**: WCAG-compliant design for inclusive user experience

## 🏗️ Technical Architecture

### 📋 Technology Stack

#### Backend Technologies
- **Java 21** - Latest Java with modern features and performance improvements
- **Spring Boot 3.4.0** - Modern Spring framework with auto-configuration
- **Spring AI** - Unified AI application framework for Java
- **Spring Security 6** - Comprehensive security framework
- **Spring Data JPA** - Advanced database abstraction layer
- **Hibernate 6** - Powerful ORM framework
- **MySQL 8.0+** - Production-ready relational database
- **H2 Database** - In-memory database for development and testing

#### Frontend Technologies
- **Thymeleaf 3.1** - Modern server-side templating engine
- **Bootstrap 5** - Mobile-first responsive CSS framework
- **JavaScript ES6+** - Modern JavaScript with async/await support
- **Chart.js** - Beautiful, interactive data visualization library
- **Apache Icons** - Professional icon library

#### Development & Build Tools
- **Maven 3.6+** - Project build and dependency management
- **Lombok 1.18.36** - Boilerplate code reduction
- **MapStruct 1.6.3** - Type-safe bean mapping
- **JUnit 5** - Modern testing framework
- **Mockito** - Powerful mocking framework for unit tests
- **dotenv-java** - Environment variable management

#### AI & External Services
- **Spring AI** - Abstraction layer for AI models
- **Google Gemini API** - Advanced natural language processing via Spring AI
- **RESTful APIs** - Modern API design patterns

### 🏛️ Clean Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    🌐 Presentation Layer                      │
│  Controllers • Forms • Thymeleaf Templates • Security         │
├─────────────────────────────────────────────────────────────┤
│                  💼 Business Logic Layer                      │
│  Services • Validation • AI Integration • Calculations       │
├─────────────────────────────────────────────────────────────┤
│                   🗄️ Data Access Layer                         │
│  Repositories • JPA Entities • Database Operations           │
├─────────────────────────────────────────────────────────────┤
│                  🏗️ Infrastructure Layer                       │
│  Configuration • Security • External APIs • Utilities       │
└─────────────────────────────────────────────────────────────┘
```

### 📂 Project Structure

```
PersonalFinanceManagementproject/
├── 📄 pom.xml                                    # Maven build configuration
├── 📄 README.md                                  # Project documentation
├── 📄 DEVELOPER_GUIDE.md                         # Developer onboarding guide
├── 📄 CLAUDE.md                                  # Claude Code instructions
├── 📄 [Unit].ini                                 # Unit test configuration
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/finance/                  # Java source code
│   │   │   ├── 📄 PersonalFinanceManagerApplication.java    # Main application class
│   │   │   ├── 📂 config/                        # Configuration classes
│   │   │   │   ├── 📄 SecurityConfig.java        # Spring Security configuration
│   │   │   │   ├── 📄 WebConfig.java             # Web MVC configuration
│   │   │   │   └── 📄 DataInitializer.java       # Database initialization
│   │   │   ├── 📂 controller/                    # Web controllers
│   │   │   │   ├── 📄 HomeController.java        # Landing page & auth
│   │   │   │   ├── 📄 DashboardController.java   # Financial dashboard
│   │   │   │   ├── 📄 TransactionController.java # Transaction management
│   │   │   │   ├── 📄 BudgetController.java      # Budget operations
│   │   │   │   ├── 📄 GoalController.java        # Goal tracking
│   │   │   │   ├── 📄 UserController.java        # User management
│   │   │   │   ├── 📄 AdminController.java       # Admin functions
│   │   │   │   ├── 📄 ReportController.java      # Financial reporting
│   │   │   │   ├── 📄 NotificationController.java # Notification system
│   │   │   │   └── 📄 ChatController.java         # AI chat interface
│   │   │   ├── 📂 service/                       # Business logic layer
│   │   │   │   ├── 📄 TransactionService.java    # Transaction operations
│   │   │   │   ├── 📄 BudgetService.java         # Budget management
│   │   │   │   ├── 📄 GoalService.java           # Goal operations
│   │   │   │   ├── 📄 UserService.java           # User management
│   │   │   │   ├── 📄 CategoryService.java       # Category management
│   │   │   │   ├── 📄 ReportService.java         # Financial analytics
│   │   │   │   ├── 📄 NotificationService.java   # Notification handling
│   │   │   │   ├── 📄 ChatService.java           # AI chat service
│   │   │   │   ├── 📄 GeminiService.java         # Google Gemini integration
│   │   │   │   └── 📄 UserDetailsServiceImpl.java # Security implementation
│   │   │   ├── 📂 repository/                    # Data access layer
│   │   │   │   ├── 📄 UserRepository.java        # User data operations
│   │   │   │   ├── 📄 TransactionRepository.java # Transaction data access
│   │   │   │   ├── 📄 CategoryRepository.java    # Category operations
│   │   │   │   ├── 📄 BudgetRepository.java      # Budget data operations
│   │   │   │   ├── 📄 GoalRepository.java        # Goal data operations
│   │   │   │   ├── 📄 RoleRepository.java        # Role management
│   │   │   │   └── 📄 NotificationRepository.java # Notification data
│   │   │   ├── 📂 domain/                        # JPA entities
│   │   │   │   ├── 📄 User.java                  # User entity
│   │   │   │   ├── 📄 Transaction.java           # Transaction entity
│   │   │   │   ├── 📄 Category.java              # Category entity
│   │   │   │   ├── 📄 Budget.java                # Budget entity
│   │   │   │   ├── 📄 Goal.java                  # Goal entity
│   │   │   │   ├── 📄 Role.java                  # Role entity
│   │   │   │   └── 📄 Notification.java          # Notification entity
│   │   │   ├── 📂 form/                          # Form DTOs & validation
│   │   │   │   ├── 📄 TransactionForm.java       # Transaction input form
│   │   │   │   ├── 📄 BudgetForm.java            # Budget creation form
│   │   │   │   ├── 📄 GoalForm.java              # Goal setting form
│   │   │   │   ├── 📄 UserRegistrationForm.java  # User registration
│   │   │   │   └── 📄 PasswordChangeForm.java    # Password management
│   │   │   ├── 📂 exception/                     # Custom exceptions
│   │   │   │   ├── 📄 UserNotFoundException.java  # User errors
│   │   │   │   ├── 📄 TransactionNotFoundException.java # Transaction errors
│   │   │   │   ├── 📄 DuplicateUserException.java # Duplicate handling
│   │   │   │   ├── 📄 InvalidPasswordException.java # Password errors
│   │   │   │   └── 📄 GlobalExceptionHandler.java # Centralized error handling
│   │   │   └── 📂 validator/                     # Custom validators
│   │   ├── 📂 resources/
│   │   │   ├── 📄 application.yaml              # Application configuration
│   │   │   ├── 📂 static/                        # Static assets
│   │   │   │   ├── 📂 css/
│   │   │   │   │   └── 📄 style.css              # Custom styling
│   │   │   │   ├── 📂 js/
│   │   │   │   │   ├── 📄 scripts.js             # Interactive features
│   │   │   │   │   └── 📄 reports.js             # Chart functionality
│   │   │   │   └── 📂 images/                    # Image assets
│   │   │   └── 📂 templates/                     # Thymeleaf templates
│   │   │       ├── 📄 base.html                 # Base template
│   │   │       ├── 📄 index.html                # Landing page
│   │   │       ├── 📄 dashboard.html            # Main dashboard
│   │   │       ├── 📂 transaction/              # Transaction pages
│   │   │       ├── 📂 budgets/                  # Budget pages
│   │   │       ├── 📂 goals/                    # Goal pages
│   │   │       ├── 📂 user/                     # User management pages
│   │   │       ├── 📂 admin/                    # Admin interface
│   │   │       ├── 📂 reports/                  # Reporting pages
│   │   │       ├── 📂 notifications/            # Notification pages
│   │   │       └── 📂 chat/                     # AI chat interface
│   │   └── 📂 test/
│   │       └── 📂 java/com/finance/              # Test classes
│   │           └── 📄 PersonalFinanceManagerApplicationTests.java
└── 📂 target/                                    # Build output
```

## 🗄️ Database Schema

### Core Entities & Relationships

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│      Users      │    │    Roles         │    │  Notifications  │
│─────────────────│    │──────────────────│    │─────────────────│
│ id (PK)         │◄───┤ id (PK)          │    │ id (PK)         │
│ username        │    │ name             │    │ message         │
│ email           │    └──────────────────┘    │ type            │
│ password        │                           │ isRead          │
│ userRole        │                           │ createdAt       │
│ createdAt       │                           └─────────────────┘
└─────────────────┘                                     ▲
         │                                                │
         │                                                │
         ▼                                                │
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│  Transactions   │    │    Categories    │    │     Goals       │
│─────────────────│    │──────────────────│    │─────────────────│
│ id (PK)         │◄───┤ id (PK)          │    │ id (PK)         │
│ description     │    │ name             │    │ name            │
│ amount          │    │ description      │    │ targetAmount    │
│ type            │    │ type             │    │ currentAmount   │
│ transactionDate │    │ color            │    │ targetDate      │
│ notes           │    └──────────────────┘    │ status          │
│ userId (FK)     │                           │ userId (FK)     │
│ categoryId (FK) │                           └─────────────────┘
└─────────────────┘
         │
         ▼
┌─────────────────┐
│     Budgets     │
│─────────────────│
│ id (PK)         │
│ name            │
│ amount          │
│ startDate       │
│ endDate         │
│ description     │
│ userId (FK)     │
│ categoryId (FK) │
└─────────────────┘
```

### Key Features by Entity

#### **User Management**
- **Authentication**: Secure login with BCrypt password encryption
- **Role Management**: USER and ADMIN roles with granular permissions
- **Profile Management**: Complete user profile with customizable settings
- **Activity Tracking**: Comprehensive audit trail of user actions

#### **Transaction System**
- **Precision Calculations**: BigDecimal for accurate financial computations
- **Smart Categorization**: AI-powered category suggestions
- **Advanced Search**: Multi-field search with date range filtering
- **Transaction Types**: Income, Expense, and Transfer support

#### **Budget Planning**
- **Flexible Periods**: Monthly, quarterly, or custom date ranges
- **Category Allocation**: Budget specific categories or overall spending
- **Progress Monitoring**: Real-time budget vs actual comparison
- **Alert System**: Intelligent notifications for budget thresholds

#### **Goal Management**
- **Multiple Goals**: Track various financial objectives simultaneously
- **Progress Calculation**: Automatic percentage completion tracking
- **Status Management**: Active, completed, paused, and cancelled states
- **Milestone Tracking**: Intermediate achievements toward larger goals

## 🚀 Quick Start Guide

### Prerequisites

- **Java 21** - Latest LTS version recommended
- **Maven 3.6+** - Build and dependency management
- **MySQL 8.0+** - Production database (or H2 for development)
- **Git** - Version control

### Installation & Setup

#### 1. **Clone the Repository**
```bash
git clone <repository-url>
cd PersonalFinanceManagementproject
```

#### 2. **Database Configuration**

**Option A: MySQL (Production)**
```sql
-- Create MySQL database
CREATE DATABASE personal_finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user (optional, for security)
CREATE USER 'financeapp'@'localhost' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL PRIVILEGES ON personal_finance_db.* TO 'financeapp'@'localhost';
FLUSH PRIVILEGES;
```

**Option B: H2 (Development/Testing)**
No setup required - uses in-memory database automatically.

#### 3. **Environment Configuration**
Create a `.env` file in the project root:
```env
# Gemini AI API Key (required for AI features)
GEMINI_API_KEY=your_gemini_api_key_here

# Database Configuration (MySQL)
DATABASE_URL=jdbc:mysql://localhost:3306/personal_finance_db
DATABASE_USERNAME=root
DATABASE_PASSWORD=your_password
```

#### 4. **Build and Run**
```bash
# Clean compile the project
mvn clean compile

# Run tests
mvn test

# Start the application
mvn spring-boot:run
```

#### 5. **Access the Application**
- **URL**: http://localhost:8083
- **H2 Console** (development): http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (leave empty)

### Development Commands

```bash
# Build and test
mvn clean install

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Package for production
mvn clean package

# Run tests with coverage
mvn clean test jacoco:report
```

## 🎨 Features Deep Dive

### 🤖 AI-Powered Chat Interface

The application features an intelligent chat system that allows users to record transactions using natural language:

**Supported Commands (Vietnamese):**
```
"Chi 50000 ăn sáng hôm qua"
- Records: Expense of 50,000 VND for breakfast yesterday
- Auto-categorizes as: Food & Dining

"Nhận lương 15 triệu tháng này"
- Records: Income of 15,000,000 VND for salary this month
- Auto-categorizes as: Salary

"Đặt cọc phòng trọ 2 triệu"
- Records: Expense of 2,000,000 VND for rent deposit
- Auto-categorizes as: Housing
```

**AI Features:**
- **Natural Language Processing**: Understands conversational Vietnamese
- **Powered by Spring AI**: Leverages Google's Gemini 2.0 Flash model for high speed and accuracy
- **Date Expression Handling**: Processes relative dates ("hôm qua", "tháng này")
- **Currency Recognition**: Handles various Vietnamese currency formats
- **Smart Categorization**: Learns from user patterns for better suggestions
- **Error Recovery**: Graceful handling of ambiguous or incomplete inputs

### 📊 Interactive Dashboard

The main dashboard provides a comprehensive financial overview:

**Real-time Statistics:**
- **Total Balance**: Current financial position
- **Monthly Income**: Total income for current month
- **Monthly Expenses**: Total expenses for current month
- **Savings Rate**: Percentage of income saved
- **Budget Health**: Overall budget utilization status

**Visual Analytics:**
- **Spending Trends**: Line charts showing spending patterns over time
- **Category Breakdown**: Pie charts of spending by category
- **Budget Progress**: Bar charts comparing budget vs actual spending
- **Goal Achievement**: Progress indicators for active goals

### 🔔 Intelligent Notification System

**Budget Alerts:**
- Warning when reaching 80% of budget limit
- Critical alerts at 100% budget utilization
- Weekly budget summaries and recommendations

**Goal Reminders:**
- Monthly goal progress updates
- Deadline reminders for approaching target dates
- Achievement celebrations when goals are completed

**System Notifications:**
- Transaction confirmations
- Account activity updates
- Security-related notifications

## 🛡️ Security Implementation

### Authentication & Authorization

**Multi-layered Security:**
- **Form-based Authentication**: Secure login with CSRF protection
- **Session Management**: Configurable session timeouts
- **Remember-me Functionality**: Secure persistent login options
- **Account Locking**: Protection against brute force attacks

**Role-based Access Control:**
- **USER Role**: Access to personal finance features
- **ADMIN Role**: Administrative functions and user management
- **Resource Protection**: Method-level security annotations

### Data Protection

**Encryption & Hashing:**
- **BCrypt Password Hashing**: Industry-standard password encryption
- **HTTPS Support**: SSL/TLS configuration for secure communications
- **SQL Injection Prevention**: Parameterized queries via JPA/Hibernate
- **XSS Protection**: Input sanitization and output encoding

**Audit & Monitoring:**
- **Activity Logging**: Comprehensive audit trail
- **Failed Login Tracking**: Security event monitoring
- **Session Monitoring**: Active session management

## 🧪 Testing Strategy

### Testing Framework Setup

**Unit Testing:**
```java
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    // Test methods...
}
```

**Integration Testing:**
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class TransactionControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    // Integration test methods...
}
```

### Test Coverage Areas

**Controllers:**
- Request/response handling
- Form validation
- Security access control
- Error handling

**Services:**
- Business logic validation
- Financial calculations
- Data processing
- External API integration

**Repositories:**
- CRUD operations
- Custom queries
- Relationship handling
- Data constraints

## 🔧 Configuration Guide

### Application Configuration (`application.yaml`)

**Server Configuration:**
```yaml
server:
  port: 8083
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
```

**Database Configuration:**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/personal_finance_db
    username: ${DATABASE_USERNAME:root}
    password: ${DATABASE_PASSWORD:your_password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
      idle-timeout: 300000

  # Spring AI Configuration
  ai:
    google:
      genai:
        api-key: ${GEMINI_API_KEY}
        chat:
          options:
            model: gemini-2.0-flash
            temperature: 0.1
```

**JPA/Hibernate Configuration:**
```yaml
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
        use_sql_comments: true
```

**Security Configuration:**
```yaml
spring:
  security:
    user:
      name: admin
      password: admin123
      roles: ADMIN
```

### Environment Variables

```bash
# Production Environment
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:mysql://prod-server:3306/finance_db
export DATABASE_USERNAME=prod_user
export DATABASE_PASSWORD=secure_password
export GEMINI_API_KEY=production_gemini_key

# Development Environment
export SPRING_PROFILES_ACTIVE=dev
export GEMINI_API_KEY=development_gemini_key
```

## 🚀 Deployment Guide

### Production Deployment

#### 1. **Build for Production**
```bash
# Clean build with tests
mvn clean package

# Create executable JAR
java -jar target/personal-finance-manager-1.0.0.jar
```

#### 2. **Docker Deployment**

**Dockerfile:**
```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY target/personal-finance-manager-1.0.0.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**docker-compose.yml:**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8083:8083"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DATABASE_URL=jdbc:mysql://db:3306/personal_finance_db
      - DATABASE_USERNAME=financeapp
      - DATABASE_PASSWORD=secure_password
    depends_on:
      - db

  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: personal_finance_db
      MYSQL_USER: financeapp
      MYSQL_PASSWORD: secure_password
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

#### 3. **Cloud Deployment**

**AWS Elastic Beanstalk:**
```bash
# Install EB CLI
pip install awsebcli

# Initialize application
eb init personal-finance-manager

# Create environment
eb create production

# Deploy
eb deploy
```

**Google Cloud Platform:**
```bash
# Build and deploy to Cloud Run
gcloud builds submit --tag gcr.io/project-id/personal-finance-manager
gcloud run deploy --image gcr.io/project-id/personal-finance-manager --platform managed
```

### Monitoring & Logging

**Application Monitoring:**
- **Spring Boot Actuator**: Health checks and metrics
- **Micrometer**: Metrics collection for monitoring systems
- **Custom Health Indicators**: Database and external service health checks

**Logging Configuration:**
```yaml
logging:
  level:
    com.finance: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/personal-finance-manager.log
```

## 🤝 Contributing Guidelines

### Development Workflow

1. **Setup Development Environment**
   ```bash
   git clone <repository-url>
   cd PersonalFinanceManagementproject
   mvn clean install
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Changes**
   - Follow coding standards (4-space indentation)
   - Write comprehensive tests
   - Update documentation
   - Ensure all tests pass

4. **Submit Changes**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   git push origin feature/your-feature-name
   ```

5. **Create Pull Request**
   - Provide clear description
   - Include screenshots for UI changes
   - Ensure CI/CD pipeline passes

### Code Standards

**Java Code Style:**
- Use 4-space indentation
- Follow Java naming conventions
- Add comprehensive JavaDoc comments
- Keep methods under 50 lines
- Use meaningful variable names

**Database Standards:**
- Use snake_case for table/column names
- Add foreign key constraints
- Include proper indexes for performance
- Use appropriate data types

**Frontend Standards:**
- Follow Bootstrap 5 conventions
- Use semantic HTML
- Implement responsive design
- Add proper ARIA labels for accessibility

## 📚 API Documentation

### REST API Endpoints

#### Authentication
```
POST /login                    - User login
POST /user/register            - User registration
POST /logout                   - User logout
```

#### Transactions
```
GET  /transactions             - List all transactions (paginated)
GET  /transactions/{id}        - Get transaction details
POST /transactions/add         - Add new transaction
PUT  /transactions/{id}        - Update transaction
DELETE /transactions/{id}      - Delete transaction
GET  /transactions/search      - Search transactions
```

#### Budgets
```
GET  /budgets                  - List all budgets
POST /budgets/add              - Create new budget
GET  /budgets/{id}             - Get budget details
PUT  /budgets/{id}             - Update budget
DELETE /budgets/{id}           - Delete budget
GET  /budgets/progress         - Get budget progress
```

#### Goals
```
GET  /goals                    - List all goals
POST /goals/add                - Create new goal
GET  /goals/{id}               - Get goal details
PUT  /goals/{id}               - Update goal
DELETE /goals/{id}             - Delete goal
POST /goals/{id}/contribute    - Add funds to goal
```

#### AI Chat
```
POST /api/chat                 - Process transaction via AI chat
GET  /api/chat/history         - Get chat history
```

### Response Formats

**Success Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "description": "Sample transaction",
    "amount": 100000.00,
    "type": "EXPENSE",
    "category": {
      "id": 1,
      "name": "Food & Dining"
    }
  },
  "message": "Operation completed successfully"
}
```

**Error Response:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid transaction amount",
    "details": [
      "Amount must be greater than 0"
    ]
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 📈 Performance Optimization

### Database Optimization

**Indexing Strategy:**
```sql
-- Performance-critical indexes
CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date);
CREATE INDEX idx_transactions_category ON transactions(category_id);
CREATE INDEX idx_budgets_user_category ON budgets(user_id, category_id);
CREATE INDEX idx_goals_user_deadline ON goals(user_id, target_date);
```

**Query Optimization:**
- Use pagination for large datasets
- Implement proper join strategies
- Utilize database-specific features
- Cache frequently accessed data

### Application Performance

**Caching Strategy:**
```java
@Service
public class TransactionService {

    @Cacheable(value = "userTransactions", key = "#userId")
    public List<Transaction> getUserTransactions(Long userId) {
        // Implementation
    }

    @CacheEvict(value = "userTransactions", key = "#userId")
    public void addTransaction(Transaction transaction) {
        // Implementation
    }
}
```

**Connection Pooling:**
- HikariCP for efficient database connections
- Configured pool sizes based on application load
- Connection timeout and idle connection management

## 🔍 Troubleshooting Guide

### Common Issues

#### 1. **Database Connection Issues**
```bash
# Check MySQL service status
sudo systemctl status mysql

# Test connection
mysql -h localhost -u financeapp -p personal_finance_db

# Check application logs
tail -f logs/personal-finance-manager.log | grep -i database
```

#### 2. **AI Features Not Working**
```bash
# Verify API key
echo $GEMINI_API_KEY

# Test API connection
curl -H "Authorization: Bearer $GEMINI_API_KEY" \
     https://generativelanguage.googleapis.com/v1/models

# Check application logs for AI-related errors
grep -i gemini logs/personal-finance-manager.log
```

#### 3. **Performance Issues**
```bash
# Monitor application performance
jstat -gc -t $(pgrep java) 5s

# Database performance analysis
mysql -e "SHOW PROCESSLIST;"
mysql -e "SHOW FULL PROCESSLIST;"

# Check memory usage
free -h
```

### Debug Mode

**Enable Debug Logging:**
```yaml
logging:
  level:
    com.finance: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

**Enable H2 Console (Development):**
```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
```

## 📄 License & Credits

### License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Credits
- **Spring Boot Team** - Excellent framework and documentation
- **Bootstrap Team** - Beautiful UI framework
- **Google AI Team** - Gemini API for natural language processing
- **Open Source Community** - Various libraries and tools that made this project possible

### Third-Party Libraries

**Core Dependencies:**
- Spring Boot 3.2.12 - Application framework
- Spring Security 6 - Security framework
- Spring Data JPA - Database abstraction
- MySQL Connector - Database driver
- Thymeleaf - Template engine

**UI/UX Libraries:**
- Bootstrap 5 - CSS framework
- Chart.js - Data visualization
- Apache Icons - Icon library

**Development Tools:**
- Lombok - Boilerplate reduction
- MapStruct - Object mapping
- JUnit 5 - Testing framework
- Mockito - Mocking framework

---

## 📞 Support & Contact

### Getting Help
- **Documentation**: Check this README and inline code comments
- **Issues**: Report bugs via GitHub Issues
- **Questions**: Contact development team

### Contributing
We welcome contributions! Please see the [Contributing Guidelines](#-contributing-guidelines) section above.

### Acknowledgments
This project was developed as part of a software engineering course, demonstrating modern Java development practices, clean architecture principles, and AI integration in web applications.

---

**🚀 Built with passion for better financial management**

**Version**: 1.0.0
**Last Updated**: November 2025
**Java Version**: 21
**Spring Boot Version**: 3.2.12