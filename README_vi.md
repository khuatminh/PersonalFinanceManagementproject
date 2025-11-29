# 💰 Hệ Thống Quản Lý Tài Chính Cá Nhân

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.1.0-green.svg)](https://spring.io/projects/spring-ai)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://spring.io/projects/spring-boot)

> Ứng dụng web quản lý tài chính cá nhân toàn diện, tích hợp AI hiện đại được xây dựng với Java 21, Spring Boot 3.4.0, và Spring AI. Có các tính năng ghi nhận giao dịch thông minh, lập kế hoạch ngân sách, theo dõi mục tiêu và phân tích tài chính nâng cao với hỗ trợ ngôn ngữ tiếng Việt.

## 🌟 Tính Năng Nổi Bật

### 🤖 Quản Lý Giao Dịch Tích Hợp AI
- **Xử Lý Ngôn Ngữ Tự Nhiên**: Ghi nhận giao dịch bằng tiếng Việt hội thoại
- **Trích Xuất Giao Dịch Thông Minh**: AI tự động nhận diện số tiền, danh mục và mô tả
- **Tự Động Phân Loại**: Gợi ý danh mục thông minh dựa trên mẫu giao dịch
- **Tích Hợp Spring AI**: Kết nối liền mạch với Google Gemini qua framework Spring AI

### 💳 Quản Lý Tài Chính Toàn Diện
- **Theo Dõi Giao Dịch**: Quản lý thu nhập/chi tiêu hoàn chỉnh với phân loại chi tiết
- **Tìm Kiếm & Lọc Nâng Cao**: Tìm giao dịch theo ngày, loại, danh mục hoặc từ khóa
- **Thống Kê Thời Gian Thực**: Tổng quan tài chính và thông tin chi tiết cập nhật từng phút
- **Hỗ Trợ Đa Tiền Tệ**: Định dạng và địa phương hóa Đồng Việt Nam (₫)

### 📊 Lập Kế Hoạch & Giám Sát Ngân Sách
- **Tạo Ngân Sách Linh Hoạt**: Đặt ngân sách cho danh mục hoặc khoảng thời gian cụ thể
- **Theo Dõi Tiến Độ Thời Gian Thực**: Chỉ báo trực quan về mức sử dụng ngân sách
- **Cảnh Báo Thông Minh**: Thông báo tự động khi tiến gần đến giới hạn ngân sách
- **Phân Tích Lịch Sử**: So sánh chi tiêu hiện tại với các mẫu lịch sử

### 🎯 Đặt Mục Tiêu & Hoàn Thành
- **Mục Tiêu Tiết Kiệm**: Tạo và theo dõi nhiều mục tiêu tài chính đồng thời
- **Trực Quan Hóa Tiến Độ**: Biểu đồ tương tác hiển thị trạng thái hoàn thành mục tiêu
- **Theo Dõi Cột Mốc**: Đặt và đạt được các cột mốc trung gian
- **Quản Lý Hạn Chót**: Lời nhắc thông minh cho ngày đích của mục tiêu

### 📈 Phân Tích & Báo Cáo Nâng Cao
- **Dashboard Tương Tác**: Tổng quan tài chính toàn diện với dữ liệu thời gian thực
- **Phân Tích Theo Danh Mục**: Mẫu chi tiêu chi tiết theo từng danh mục
- **Báo Cáo Theo Thời Gian**: Báo cáo tài chính hàng tháng, hàng quý và hàng năm
- **Trực Quan Dữ Liệu**: Tích hợp Chart.js cho các biểu đồ tương tác đẹp mắt

### 🔐 Bảo Mật Cấp Doanh Nghiệp
- **Spring Security 6**: Framework bảo mật toàn diện hiện đại
- **Mã Hóa BCrypt**: Mã hóa mật khẩu cấp quân sự
- **Kiểm Soát Truy Cập Dựa trên Vai Trò**: Quản lý vai trò USER và ADMIN
- **Bảo Vệ CSRF**: Ngăn chặn tấn công giả mạo yêu cầu chéo trang web
- **Quản Lý Session**: Xử lý session an toàn với thời gian chờ cấu hình

### 🎨 Trải Nghiệm Người Dùng Hiện Đại
- **Thiết Kế Phản Ứng**: Giao diện Bootstrap 5 ưu tiên di động
- **Cập Nhật Thời Gian Thực**: Cập nhật nội dung động mà không cần tải lại trang
- **Dẫn Hướng Trực Quan**: Giao diện thân thiện với điều hướng breadcrumb
- **Khả Năng Tiếp Cận**: Thiết kế tuân thủ WCAG cho trải nghiệm người dùng toàn diện

## 🏗️ Kiến Trúc Kỹ Thuật

### 📋 Ngăn Công Nghệ

#### Công Nghệ Backend
- **Java 21** - Java phiên bản mới nhất với các tính năng hiện đại và cải tiến hiệu suất
- **Spring Boot 3.4.0** - Framework Spring hiện đại với tự động cấu hình
- **Spring AI** - Framework ứng dụng AI thống nhất cho Java
- **Spring Security 6** - Framework bảo mật toàn diện
- **Spring Data JPA** - Lớp trừu tượng cơ sở dữ liệu nâng cao
- **Hibernate 6** - Framework ORM mạnh mẽ
- **MySQL 8.0+** - Cơ sở dữ liệu quan hệ sản xuất
- **H2 Database** - Cơ sở dữ liệu trong bộ nhớ cho phát triển và kiểm thử

#### Công Nghệ Frontend
- **Thymeleaf 3.1** - Công cụ tạo khuôn mẫu phía máy chủ hiện đại
- **Bootstrap 5** - Framework CSS phản ứng ưu tiên di động
- **JavaScript ES6+** - JavaScript hiện đại với hỗ trợ async/await
- **Chart.js** - Thư viện trực quan hóa dữ liệu tương tác đẹp mắt
- **Apache Icons** - Thư viện biểu tượng chuyên nghiệp

#### Công Cụ Phát Triển & Xây Dựng
- **Maven 3.6+** - Xây dựng dự án và quản lý phụ thuộc
- **Lombok 1.18.36** - Giảm thiểu code mẫu
- **MapStruct 1.6.3** - Ánh xạ bean an toàn kiểu
- **JUnit 5** - Framework kiểm thử hiện đại
- **Mockito** - Framework mocking mạnh mẽ cho kiểm thử đơn vị
- **dotenv-java** - Quản lý biến môi trường

#### AI & Dịch Vụ Bên Ngoài
- **Spring AI** - Lớp trừu tượng cho các mô hình AI
- **Google Gemini API** - Xử lý ngôn ngữ tự nhiên nâng cao qua Spring AI
- **RESTful APIs** - Các mẫu thiết kế API hiện đại

### 🏛️ Kiến Trúc Sạch

```
┌─────────────────────────────────────────────────────────────┐
│                    🌐 Lớp Hiển Thị                           │
│  Controllers • Forms • Templates Thymeleaf • Security         │
├─────────────────────────────────────────────────────────────┤
│                  💼 Lớp Logic Kinh Doanh                     │
│  Services • Validation • AI Integration • Calculations       │
├─────────────────────────────────────────────────────────────┤
│                   🗄️ Lớp Truy Cập Dữ Liệu                    │
│  Repositories • JPA Entities • Database Operations           │
├─────────────────────────────────────────────────────────────┤
│                  🏗️ Lớp Hạ Tầng                              │
│  Configuration • Security • External APIs • Utilities       │
└─────────────────────────────────────────────────────────────┘
```

### 📂 Cấu Trúc Dự Án

```
PersonalFinanceManagementproject/
├── 📄 pom.xml                                    # Cấu hình build Maven
├── 📄 README.md                                  # Tài liệu dự án
├── 📄 README_VI.md                               # Tài liệu dự án tiếng Việt
├── 📄 DEVELOPER_GUIDE.md                         # Hướng dẫn cho nhà phát triển
├── 📄 CLAUDE.md                                  # Hướng dẫn Claude Code
├── 📄 [Unit].ini                                 # Cấu hình kiểm thử đơn vị
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/finance/                  # Mã nguồn Java
│   │   │   ├── 📄 PersonalFinanceManagerApplication.java    # Lớp ứng dụng chính
│   │   │   ├── 📂 config/                        # Lớp cấu hình
│   │   │   │   ├── 📄 SecurityConfig.java        # Cấu hình Spring Security
│   │   │   │   ├── 📄 WebConfig.java             # Cấu hình Web MVC
│   │   │   │   └── 📄 DataInitializer.java       # Khởi tạo cơ sở dữ liệu
│   │   │   ├── 📂 controller/                    # Controllers web
│   │   │   │   ├── 📄 HomeController.java        # Trang đích & xác thực
│   │   │   │   ├── 📄 DashboardController.java   # Bảng điều khiển tài chính
│   │   │   │   ├── 📄 TransactionController.java # Quản lý giao dịch
│   │   │   │   ├── 📄 BudgetController.java      # Hoạt động ngân sách
│   │   │   │   ├── 📄 GoalController.java        # Theo dõi mục tiêu
│   │   │   │   ├── 📄 UserController.java        # Quản lý người dùng
│   │   │   │   ├── 📄 AdminController.java       # Chức năng admin
│   │   │   │   ├── 📄 ReportController.java      # Báo cáo tài chính
│   │   │   │   ├── 📄 NotificationController.java # Hệ thống thông báo
│   │   │   │   └── 📄 ChatController.java         # Giao diện chat AI
│   │   │   ├── 📂 service/                       # Lớp logic kinh doanh
│   │   │   │   ├── 📄 TransactionService.java    # Hoạt động giao dịch
│   │   │   │   ├── 📄 BudgetService.java         # Quản lý ngân sách
│   │   │   │   ├── 📄 GoalService.java           # Hoạt động mục tiêu
│   │   │   │   ├── 📄 UserService.java           # Quản lý người dùng
│   │   │   │   ├── 📄 CategoryService.java       # Quản lý danh mục
│   │   │   │   ├── 📄 ReportService.java         # Phân tích tài chính
│   │   │   │   ├── 📄 NotificationService.java   # Xử lý thông báo
│   │   │   │   ├── 📄 ChatService.java           # Dịch vụ chat AI
│   │   │   │   ├── 📄 GeminiService.java         # Tích hợp Google Gemini
│   │   │   │   └── 📄 UserDetailsServiceImpl.java # Triển khai Security
│   │   │   ├── 📂 repository/                    # Lớp truy cập dữ liệu
│   │   │   │   ├── 📄 UserRepository.java        # Hoạt động dữ liệu người dùng
│   │   │   │   ├── 📄 TransactionRepository.java # Truy cập dữ liệu giao dịch
│   │   │   │   ├── 📄 CategoryRepository.java    # Hoạt động danh mục
│   │   │   │   ├── 📄 BudgetRepository.java      # Hoạt động dữ liệu ngân sách
│   │   │   │   ├── 📄 GoalRepository.java        # Hoạt động dữ liệu mục tiêu
│   │   │   │   ├── 📄 RoleRepository.java        # Quản lý vai trò
│   │   │   │   └── 📄 NotificationRepository.java # Dữ liệu thông báo
│   │   │   ├── 📂 domain/                        # Entities JPA
│   │   │   │   ├── 📄 User.java                  # Entity người dùng
│   │   │   │   ├── 📄 Transaction.java           # Entity giao dịch
│   │   │   │   ├── 📄 Category.java              # Entity danh mục
│   │   │   │   ├── 📄 Budget.java                # Entity ngân sách
│   │   │   │   ├── 📄 Goal.java                  # Entity mục tiêu
│   │   │   │   ├── 📄 Role.java                  # Entity vai trò
│   │   │   │   └── 📄 Notification.java          # Entity thông báo
│   │   │   ├── 📂 form/                          # DTOs & xác thực biểu mẫu
│   │   │   │   ├── 📄 TransactionForm.java       # Biểu mẫu nhập giao dịch
│   │   │   │   ├── 📄 BudgetForm.java            # Biểu mẫu tạo ngân sách
│   │   │   │   ├── 📄 GoalForm.java              # Biểu mẫu đặt mục tiêu
│   │   │   │   ├── 📄 UserRegistrationForm.java  # Đăng ký người dùng
│   │   │   │   └── 📄 PasswordChangeForm.java    # Quản lý mật khẩu
│   │   │   ├── 📂 exception/                     # Ngoại lệ tùy chỉnh
│   │   │   │   ├── 📄 UserNotFoundException.java  # Lỗi người dùng
│   │   │   │   ├── 📄 TransactionNotFoundException.java # Lỗi giao dịch
│   │   │   │   ├── 📄 DuplicateUserException.java # Xử lý trùng lặp
│   │   │   │   ├── 📄 InvalidPasswordException.java # Lỗi mật khẩu
│   │   │   │   └── 📄 GlobalExceptionHandler.java # Xử lý lỗi tập trung
│   │   │   └── 📂 validator/                     # Trình xác thực tùy chỉnh
│   │   ├── 📂 resources/
│   │   │   ├── 📄 application.yaml              # Cấu hình ứng dụng
│   │   │   ├── 📂 static/                        # Tài sản tĩnh
│   │   │   │   ├── 📂 css/
│   │   │   │   │   └── 📄 style.css              # Phong cách tùy chỉnh
│   │   │   │   ├── 📂 js/
│   │   │   │   │   ├── 📄 scripts.js             # Tính năng tương tác
│   │   │   │   │   └── 📄 reports.js             # Chức năng biểu đồ
│   │   │   │   └── 📂 images/                    # Tài sản hình ảnh
│   │   │   └── 📂 templates/                     # Templates Thymeleaf
│   │   │       ├── 📄 base.html                 # Template cơ bản
│   │   │       ├── 📄 index.html                # Trang đích
│   │   │       ├── 📄 dashboard.html            # Bảng điều khiển chính
│   │   │       ├── 📂 transaction/              # Trang giao dịch
│   │   │       ├── 📂 budgets/                  # Trang ngân sách
│   │   │       ├── 📂 goals/                    # Trang mục tiêu
│   │   │       ├── 📂 user/                     # Trang quản lý người dùng
│   │   │       ├── 📂 admin/                    # Giao diện admin
│   │   │       ├── 📂 reports/                  # Trang báo cáo
│   │   │       ├── 📂 notifications/            # Trang thông báo
│   │   │       └── 📂 chat/                     # Giao diện chat AI
│   │   └── 📂 test/
│   │       └── 📂 java/com/finance/              # Lớp kiểm thử
│   │           └── 📄 PersonalFinanceManagerApplicationTests.java
└── 📂 target/                                    # Kết quả build
```

## 🗄️ Sơ Đồ Cơ Sở Dữ Liệu

### Entities Chính & Mối Quan Hệ

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

### Tính Năng Chính Theo Entity

#### **Quản Lý Người Dùng**
- **Xác Thực**: Đăng nhập an toàn với mã hóa mật khẩu BCrypt
- **Quản Lý Vai Trò**: Vai trò USER và ADMIN với quyền chi tiết
- **Quản Lý Hồ Sơ**: Hồ sơ người dùng hoàn chỉnh với cài đặt tùy chỉnh
- **Theo Dõi Hoạt Động**: Nhật ký kiểm tra toàn diện các hành động người dùng

#### **Hệ Thống Giao Dịch**
- **Tính Chính xác**: BigDecimal cho các tính toán tài chính chính xác
- **Phân Loại Thông Minh**: Gợi ý danh mục dựa trên AI
- **Tìm Kiếm Nâng Cao**: Tìm kiếm đa trường với bộ lọc khoảng ngày
- **Loại Giao Dịch**: Hỗ trợ Thu nhập, Chi tiêu và Chuyển khoản

#### **Lập Kế Hoạch Ngân Sách**
- **Khoảng Thời Gian Linh Hoạt**: Hàng tháng, hàng quý hoặc khoảng ngày tùy chỉnh
- **Phân Bổ Danh Mục**: Ngân sách cho các danh mục cụ thể hoặc chi tiêu tổng thể
- **Giám Sát Tiến Độ**: So sánh ngân sách với thực tế thời gian thực
- **Hệ Thống Cảnh Báo**: Thông báo thông minh cho ngưỡng ngân sách

#### **Quản Lý Mục Tiêu**
- **Nhiều Mục Tiêu**: Theo dõi nhiều mục tiêu tài chính đồng thời
- **Tính Toán Tiến Độ**: Theo dõi tự động phần trăm hoàn thành
- **Quản Lý Trạng Thái**: Trạng thái Hoạt động, Hoàn thành, Tạm dừng và Hủy
- **Theo Dõi Cột Mốc**: Thành tựu trung gian hướng tới mục tiêu lớn hơn

## 🚀 Hướng Dẫn Bắt Đầu Nhanh

### Điều Kiện Tiên Quyết

- **Java 21** - Phiên bản LTS mới nhất được khuyến nghị
- **Maven 3.6+** - Xây dựng và quản lý phụ thuộc
- **MySQL 8.0+** - Cơ sở dữ liệu sản xuất (hoặc H2 cho phát triển)
- **Git** - Kiểm soát phiên bản

### Cài Đặt & Thiết Lập

#### 1. **Clone Repository**
```bash
git clone <repository-url>
cd PersonalFinanceManagementproject
```

#### 2. **Cấu Hình Cơ Sở Dữ Liệu**

**Lựa chọn A: MySQL (Sản xuất)**
```sql
-- Tạo cơ sở dữ liệu MySQL
CREATE DATABASE personal_finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tạo người dùng (tùy chọn, cho bảo mật)
CREATE USER 'financeapp'@'localhost' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL PRIVILEGES ON personal_finance_db.* TO 'financeapp'@'localhost';
FLUSH PRIVILEGES;
```

**Lựa chọn B: H2 (Phát Triển/Kiểm Thử)**
Không cần thiết lập - sử dụng cơ sở dữ liệu trong bộ nhớ tự động.

#### 3. **Cấu Hình Môi Trường**
Tạo tệp `.env` trong thư mục gốc dự án:
```env
# Gemini AI API Key (bắt buộc cho tính năng AI)
GEMINI_API_KEY=your_gemini_api_key_here

# Cấu hình Database (MySQL)
DATABASE_URL=jdbc:mysql://localhost:3306/personal_finance_db
DATABASE_USERNAME=root
DATABASE_PASSWORD=your_password
```

#### 4. **Xây Dựng và Chạy**
```bash
# Biên dịch sạch dự án
mvn clean compile

# Chạy kiểm thử
mvn test

# Khởi động ứng dụng
mvn spring-boot:run
```

#### 5. **Truy Cập Ứng Dụng**
- **URL**: http://localhost:8083
- **H2 Console** (phát triển): http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (để trống)

### Lệnh Phát Triển

```bash
# Xây dựng và kiểm thử
mvn clean install

# Chạy với profile cụ thể
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Đóng gói cho sản xuất
mvn clean package

# Chạy kiểm thử với độ phủ
mvn clean test jacoco:report
```

## 🎨 Khai Thác Tính Năng Chi Tiết

### 🤖 Giao Diện Chat Tích Hợp AI

Ứng dụng có hệ thống chat thông minh cho phép người dùng ghi nhận giao dịch bằng ngôn ngữ tự nhiên:

**Lệnh Hỗ Trợ (Tiếng Việt):**
```
"Chi 50000 ăn sáng hôm qua"
- Ghi nhận: Chi tiêu 50.000 VNĐ cho bữa sáng hôm qua
- Tự động phân loại: Ẩm thực & Đồ ăn

"Nhận lương 15 triệu tháng này"
- Ghi nhận: Thu nhập 15.000.000 VNĐ cho lương tháng này
- Tự động phân loại: Lương

"Đặt cọc phòng trọ 2 triệu"
- Ghi nhận: Chi tiêu 2.000.000 VNĐ cho đặt cọc nhà trọ
- Tự động phân loại: Nhà ở
```

**Tính Năng AI:**
- **Xử Lý Ngôn Ngữ Tự Nhiên**: Hiểu tiếng Việt hội thoại
- **Cung Cấp Bởi Spring AI**: Tận dụng mô hình Gemini 2.0 Flash của Google cho tốc độ và độ chính xác cao
- **Xử Lý Biểu Thức Ngày**: Xử lý ngày tương đối ("hôm qua", "tháng này")
- **Nhận Diện Tiền Tệ**: Xử lý các định dạng tiền tệ Việt Nam khác nhau
- **Phân Loại Thông Minh**: Học từ mẫu người dùng để đưa ra gợi ý tốt hơn
- **Phục Hồi Lỗi**: Xử lý tự nhiên các đầu vào mơ hồ hoặc không đầy đủ

### 📊 Dashboard Tương Tác

Dashboard chính cung cấp tổng quan tài chính toàn diện:

**Thống Kê Thời Gian Thực:**
- **Tổng Số Dư**: Vị trí tài chính hiện tại
- **Thu Nhập Hàng Tháng**: Tổng thu nhập tháng hiện tại
- **Chi Tiêu Hàng Tháng**: Tổng chi tiêu tháng hiện tại
- **Tỷ Lệ Tiết Kiệm**: Phần trăm thu nhập tiết kiệm được
- **Sức Khỏe Ngân Sách**: Trạng thái sử dụng ngân sách tổng thể

**Phân Tích Trực Quan:**
- **Xu Hướng Chi Tiêu**: Biểu đồ đường hiển thị mẫu chi tiêu theo thời gian
- **Phân Tách Danh Mục**: Biểu đồ tròn chi tiêu theo danh mục
- **Tiến Độ Ngân Sách**: Biểu đồ cột so sánh ngân sách với chi tiêu thực tế
- **Hoàn Thành Mục Tiêu**: Chỉ báo tiến độ cho các mục tiêu đang hoạt động

### 🔔 Hệ Thống Thông Báo Thông Minh

**Cảnh Báo Ngân Sách:**
- Cảnh báo khi đạt 80% giới hạn ngân sách
- Cảnh báo nghiêm trọng khi đạt 100% mức sử dụng ngân sách
- Tóm tắt và gợi ý ngân sách hàng tuần

**Nhắc Nhở Mục Tiêu:**
- Cập nhật tiến độ mục tiêu hàng tháng
- Lời nhắc hạn chót cho ngày mục tiêu đang đến gần
- Chúc mừng hoàn thành khi mục tiêu được đạt

**Thông Báo Hệ Thống:**
- Xác nhận giao dịch
- Cập nhật hoạt động tài khoản
- Thông báo liên quan đến bảo mật

## 🛡️ Triển Khai Bảo Mật

### Xác Thực & Phân Quyền

**Bảo Mật Đa Lớp:**
- **Xác Thực Dạng Form**: Đăng nhập an toàn với bảo vệ CSRF
- **Quản Lý Session**: Thời gian chờ session có cấu hình
- **Chức Năng Ghi Nhớ**: Tùy chọn đăng nhập bền vững an toàn
- **Khóa Tài Khoản**: Bảo vệ chống lại tấn công brute force

**Kiểm Soát Truy Cập Dựa trên Vai Trò:**
- **Vai Trò USER**: Truy cập các tính năng tài chính cá nhân
- **Vai Trò ADMIN**: Chức năng quản trị và quản lý người dùng
- **Bảo Vệ Tài Nguyên**: Chú thích bảo mật cấp phương thức

### Bảo Vệ Dữ Liệu

**Mã Hóa & Băm:**
- **Băm Mật Khẩu BCrypt**: Mã hóa mật khẩu tiêu chuẩn ngành
- **Hỗ Trợ HTTPS**: Cấu hình SSL/TLS cho truyền thông an toàn
- **Ngăn Chặn SQL Injection**: Truy vấn được tham số hóa qua JPA/Hibernate
- **Bảo Vệ XSS**: Khử trùng đầu vào và mã hóa đầu ra

**Kiểm Tra & Giám Sát:**
- **Ghi Nhật Ký Hoạt Động**: Nhật ký kiểm tra toàn diện
- **Theo Dõi Đăng Nhập Thất Bại**: Giám sát sự kiện bảo mật
- **Giám Sát Session**: Quản lý session hoạt động

## 🧪 Chiến Lược Kiểm Thử

### Thiết Lập Framework Kiểm Thử

**Kiểm Thử Đơn Vị:**
```java
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    // Phương thức kiểm thử...
}
```

**Kiểm Thử Tích Hợp:**
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class TransactionControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    // Phương thức kiểm thử tích hợp...
}
```

### Khu Vực Độ Phủ Kiểm Thử

**Controllers:**
- Xử lý yêu cầu/phản hồi
- Xác thực biểu mẫu
- Kiểm soát truy cập bảo mật
- Xử lý lỗi

**Services:**
- Xác thực logic kinh doanh
- Tính toán tài chính
- Xử lý dữ liệu
- Tích hợp API bên ngoài

**Repositories:**
- Hoạt động CRUD
- Truy vấn tùy chỉnh
- Xử lý mối quan hệ
- Ràng buộc dữ liệu

## 🔧 Hướng Dẫn Cấu Hình

### Cấu Hình Ứng Dụng (`application.yaml`)

**Cấu Hình Máy Chủ:**
```yaml
server:
  port: 8083
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
```

**Cấu Hình Cơ Sở Dữ Liệu:**
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

  # Cấu hình Spring AI
  ai:
    google:
      genai:
        api-key: ${GEMINI_API_KEY}
        chat:
          options:
            model: gemini-2.0-flash
            temperature: 0.1
```

**Cấu Hình JPA/Hibernate:**
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

**Cấu Hình Bảo Mật:**
```yaml
spring:
  security:
    user:
      name: admin
      password: admin123
      roles: ADMIN
```

### Biến Môi Trường

```bash
# Môi trường sản xuất
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:mysql://prod-server:3306/finance_db
export DATABASE_USERNAME=prod_user
export DATABASE_PASSWORD=secure_password
export GEMINI_API_KEY=production_gemini_key

# Môi trường phát triển
export SPRING_PROFILES_ACTIVE=dev
export GEMINI_API_KEY=development_gemini_key
```

## 🚀 Hướng Dẫn Triển Khai

### Triển Khai Sản Xuất

#### 1. **Xây Dựng Cho Sản Xuất**
```bash
# Xây dựng sạch với kiểm thử
mvn clean package

# Tạo JAR thực thi
java -jar target/personal-finance-manager-1.0.0.jar
```

#### 2. **Triển Khai Docker**

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

#### 3. **Triển Khai Cloud**

**AWS Elastic Beanstalk:**
```bash
# Cài đặt EB CLI
pip install awsebcli

# Khởi tạo ứng dụng
eb init personal-finance-manager

# Tạo môi trường
eb create production

# Triển khai
eb deploy
```

**Google Cloud Platform:**
```bash
# Xây dựng và triển khai lên Cloud Run
gcloud builds submit --tag gcr.io/project-id/personal-finance-manager
gcloud run deploy --image gcr.io/project-id/personal-finance-manager --platform managed
```

### Giám Sát & Ghi Nhật Ký

**Giám Sát Ứng Dụng:**
- **Spring Boot Actuator**: Kiểm tra sức khỏe và số liệu
- **Micrometer**: Thu thập số liệu cho hệ thống giám sát
- **Chỉ Báo Sức Khỏe Tùy Chỉnh**: Kiểm tra sức khỏe cơ sở dữ liệu và dịch vụ bên ngoài

**Cấu Hình Ghi Nhật Ký:**
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

## 🤝 Hướng Dẫn Đóng Góp

### Quy Trình Phát Triển

1. **Thiết Lập Môi Trường Phát Triển**
   ```bash
   git clone <repository-url>
   cd PersonalFinanceManagementproject
   mvn clean install
   ```

2. **Tạo Branch Tính Năng**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Thực Hiện Thay Đổi**
   - Tuân thủ tiêu chuẩn mã hóa (thụt lề 4 khoảng trắng)
   - Viết kiểm thử toàn diện
   - Cập nhật tài liệu
   - Đảm bảo tất cả kiểm thử đều vượt qua

4. **Gửi Thay Đổi**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   git push origin feature/your-feature-name
   ```

5. **Tạo Pull Request**
   - Cung cấp mô tả rõ ràng
   - Bao gồm ảnh chụp màn hình cho các thay đổi giao diện
   - Đảm bảo pipeline CI/CD vượt qua

### Tiêu Chuẩn Mã

**Kiểu Mã Java:**
- Sử dụng thụt lề 4 khoảng trắng
- Tuân thủ quy ước đặt tên Java
- Thêm chú thích JavaDoc toàn diện
- Giữ các phương thức dưới 50 dòng
- Sử dụng tên biến có ý nghĩa

**Tiêu Chuẩn Cơ Sở Dữ Liệu:**
- Sử dụng snake_case cho tên bảng/cột
- Thêm ràng buộc khóa ngoại
- Bao gồm chỉ mục phù hợp cho hiệu suất
- Sử dụng kiểu dữ liệu phù hợp

**Tiêu Chuẩn Frontend:**
- Tuân thủ quy ước Bootstrap 5
- Sử dụng HTML ngữ nghĩa
- Triển khai thiết kế phản ứng
- Thêm nhãn ARIA phù hợp để có khả năng tiếp cận

## 📚 Tài Liệu API

### Các Endpoint REST API

#### Xác Thực
```
POST /login                    - Đăng nhập người dùng
POST /user/register            - Đăng ký người dùng
POST /logout                   - Đăng xuất người dùng
```

#### Giao Dịch
```
GET  /transactions             - Liệt kê tất cả giao dịch (phân trang)
GET  /transactions/{id}        - Lấy chi tiết giao dịch
POST /transactions/add         - Thêm giao dịch mới
PUT  /transactions/{id}        - Cập nhật giao dịch
DELETE /transactions/{id}      - Xóa giao dịch
GET  /transactions/search      - Tìm kiếm giao dịch
```

#### Ngân Sách
```
GET  /budgets                  - Liệt kê tất cả ngân sách
POST /budgets/add              - Tạo ngân sách mới
GET  /budgets/{id}             - Lấy chi tiết ngân sách
PUT  /budgets/{id}             - Cập nhật ngân sách
DELETE /budgets/{id}           - Xóa ngân sách
GET  /budgets/progress         - Lấy tiến độ ngân sách
```

#### Mục Tiêu
```
GET  /goals                    - Liệt kê tất cả mục tiêu
POST /goals/add                - Tạo mục tiêu mới
GET  /goals/{id}               - Lấy chi tiết mục tiêu
PUT  /goals/{id}               - Cập nhật mục tiêu
DELETE /goals/{id}             - Xóa mục tiêu
POST /goals/{id}/contribute    - Thêm tiền vào mục tiêu
```

#### Chat AI
```
POST /api/chat                 - Xử lý giao dịch qua chat AI
GET  /api/chat/history         - Lấy lịch sử chat
```

### Định Dạng Phản Hồi

**Phản Hồi Thành Công:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "description": "Giao dịch mẫu",
    "amount": 100000.00,
    "type": "EXPENSE",
    "category": {
      "id": 1,
      "name": "Ẩm thực & Đồ ăn"
    }
  },
  "message": "Hoạt động hoàn thành thành công"
}
```

**Phản Hồi Lỗi:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Số tiền giao dịch không hợp lệ",
    "details": [
      "Số tiền phải lớn hơn 0"
    ]
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 📈 Tối Ưu Hiệu Suất

### Tối Ưu Cơ Sở Dữ Liệu

**Chiến Lược Indexing:**
```sql
-- Các chỉ mục quan trọng về hiệu suất
CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date);
CREATE INDEX idx_transactions_category ON transactions(category_id);
CREATE INDEX idx_budgets_user_category ON budgets(user_id, category_id);
CREATE INDEX idx_goals_user_deadline ON goals(user_id, target_date);
```

**Tối Ưu Truy Vấn:**
- Sử dụng phân trang cho các bộ dữ liệu lớn
- Triển khai chiến lược join phù hợp
- Tận dụng các tính năng cụ thể của cơ sở dữ liệu
- Cache dữ liệu thường xuyên truy cập

### Hiệu Suất Ứng Dụng

**Chiến Lược Cache:**
```java
@Service
public class TransactionService {

    @Cacheable(value = "userTransactions", key = "#userId")
    public List<Transaction> getUserTransactions(Long userId) {
        // Triển khai
    }

    @CacheEvict(value = "userTransactions", key = "#userId")
    public void addTransaction(Transaction transaction) {
        // Triển khai
    }
}
```

**Pooling Kết Nối:**
- HikariCP cho các kết nối cơ sở dữ liệu hiệu quả
- Cấu hình kích thước pool dựa trên tải ứng dụng
- Quản lý timeout kết nối và kết nối idle

## 🔍 Hướng Dẫn Gỡ Rối

### Vấn Đề Phổ Biến

#### 1. **Vấn Đề Kết Nối Cơ Sở Dữ Liệu**
```bash
# Kiểm tra trạng thái dịch vụ MySQL
sudo systemctl status mysql

# Kiểm tra kết nối
mysql -h localhost -u financeapp -p personal_finance_db

# Kiểm tra nhật ký ứng dụng
tail -f logs/personal-finance-manager.log | grep -i database
```

#### 2. **Tính Năng AI Không Hoạt Động**
```bash
# Xác minh khóa API
echo $GEMINI_API_KEY

# Kiểm tra kết nối API
curl -H "Authorization: Bearer $GEMINI_API_KEY" \
     https://generativelanguage.googleapis.com/v1/models

# Kiểm tra nhật ký ứng dụng để tìm lỗi liên quan AI
grep -i gemini logs/personal-finance-manager.log
```

#### 3. **Vấn Đề Hiệu Suất**
```bash
# Giám sát hiệu suất ứng dụng
jstat -gc -t $(pgrep java) 5s

# Phân tích hiệu suất cơ sở dữ liệu
mysql -e "SHOW PROCESSLIST;"
mysql -e "SHOW FULL PROCESSLIST;"

# Kiểm tra sử dụng bộ nhớ
free -h
```

### Chế Độ Gỡ Rối

**Bật Ghi Nhật Ký Debug:**
```yaml
logging:
  level:
    com.finance: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

**Bật H2 Console (Phát Triển):**
```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
```

## 📄 Giấy Phép & Ghi Nhận

### Giấy Phép
Dự án này được cấp phép theo Giấy phép MIT - xem tệp [LICENSE](LICENSE) để biết chi tiết.

### Ghi Nhận
- **Spring Boot Team** - Framework và tài liệu xuất sắc
- **Bootstrap Team** - Framework UI đẹp mắt
- **Google AI Team** - Gemini API cho xử lý ngôn ngữ tự nhiên
- **Cộng Đồng Mã Nguồn Mở** - Các thư viện và công cụ đã làm dự án này trở nên khả thi

### Thư Viện Bên Thứ Ba

**Phụ Thuộc Cốt Lõi:**
- Spring Boot 3.2.12 - Framework ứng dụng
- Spring Security 6 - Framework bảo mật
- Spring Data JPA - Trừu tượng hóa cơ sở dữ liệu
- MySQL Connector - Driver cơ sở dữ liệu
- Thymeleaf - Công cụ tạo khuôn mẫu

**Thư Viện UI/UX:**
- Bootstrap 5 - Framework CSS
- Chart.js - Trực quan hóa dữ liệu
- Apache Icons - Thư viện biểu tượng

**Công Cụ Phát Triển:**
- Lombok - Giảm thiểu code mẫu
- MapStruct - Ánh xạ đối tượng
- JUnit 5 - Framework kiểm thử
- Mockito - Framework mocking

---

## 📞 Hỗ Trợ & Liên Hệ

### Nhận Trợ Giúp
- **Tài liệu**: Kiểm tra README này và các chú thích mã nội tuyến
- **Vấn đề**: Báo lỗi qua GitHub Issues
- **Câu hỏi**: Liên hệ đội ngũ phát triển

### Đóng Góp
Chúng tôi chào đón sự đóng góp! Vui lòng xem phần [Hướng Dẫn Đóng Góp](#-hướng-dẫn-đóng-góp) ở trên.

### Cảm Ơn
Dự án này được phát triển như một phần của khóa học kỹ thuật phần mềm, thể hiện các thực hành phát triển Java hiện đại, các nguyên tắc kiến trúc sạch, và tích hợp AI trong các ứng dụng web.

---

**🚀 Xây dựng bằng đam mê cho quản lý tài chính tốt hơn**

**Phiên bản**: 1.0.0
**Cập nhật lần cuối**: Tháng 11 2025
**Phiên bản Java**: 21
**Phiên bản Spring Boot**: 3.2.12
