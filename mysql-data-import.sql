
-- Chỉ chạy file import này sau khi đã khởi chạy ứng dụng Spring boot lần đầu
-- File import toàn data cần thiết (Categories, Sample Transaction, budgets, goals for user Admin)

--
-- Categories
INSERT INTO categories (name, description, type, color) VALUES ('Lương', 'Thu nhập thường xuyên từ công việc', 'INCOME', '#28a745');
INSERT INTO categories (name, description, type, color) VALUES ('Làm thêm', 'Thu nhập từ công việc tự do', 'INCOME', '#20c997');
INSERT INTO categories (name, description, type, color) VALUES ('Đầu tư', 'Thu nhập từ các khoản đầu tư', 'INCOME', '#6f42c1');
INSERT INTO categories (name, description, type, color) VALUES ('Quà tặng', 'Thu nhập từ quà tặng', 'INCOME', '#e83e8c');
INSERT INTO categories (name, description, type, color) VALUES ('Thu nhập khác', 'Các nguồn thu nhập khác', 'INCOME', '#6c757d');
INSERT INTO categories (name, description, type, color) VALUES ('Ăn uống', 'Thực phẩm, nhà hàng, v.v.', 'EXPENSE', '#fd7e14');
INSERT INTO categories (name, description, type, color) VALUES ('Đi lại', 'Xăng, phương tiện công cộng, v.v.', 'EXPENSE', '#007bff');
INSERT INTO categories (name, description, type, color) VALUES ('Mua sắm', 'Quần áo, đồ điện tử, v.v.', 'EXPENSE', '#dc3545');
INSERT INTO categories (name, description, type, color) VALUES ('Giải trí', 'Phim ảnh, buổi hòa nhạc, v.v.', 'EXPENSE', '#6610f2');
INSERT INTO categories (name, description, type, color) VALUES ('Hóa đơn & Tiện ích', 'Tiền thuê nhà, điện, internet, v.v.', 'EXPENSE', '#17a2b8');
INSERT INTO categories (name, description, type, color) VALUES ('Chăm sóc sức khỏe', 'Khám bác sĩ, thuốc men, v.v.', 'EXPENSE', '#20c997');
INSERT INTO categories (name, description, type, color) VALUES ('Giáo dục', 'Học phí, sách vở, v.v.', 'EXPENSE', '#6f42c1');
INSERT INTO categories (name, description, type, color) VALUES ('Tiền thuê nhà/Trả góp', 'Chi phí nhà ở', 'EXPENSE', '#e83e8c');
INSERT INTO categories (name, description, type, color) VALUES ('Tiết kiệm', 'Đóng góp vào các khoản tiết kiệm', 'EXPENSE', '#28a745');
INSERT INTO categories (name, description, type, color) VALUES ('Chi phí khác', 'Các chi phí linh tinh khác', 'EXPENSE', '#6c757d');

-- Transactions for admin (user_id = 1)
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 1, 25000000, 'INCOME', 'Lương tháng 9', '2025-09-20 10:00:00', NOW());
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 2, 5000000, 'INCOME', 'Dự án tự do', '2025-09-30 15:30:00', NOW());
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 6, 1500000, 'EXPENSE', 'Mua sắm thực phẩm', '2025-10-13 12:00:00', NOW());
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 6, 850000, 'EXPENSE', 'Ăn tối nhà hàng', '2025-10-10 20:00:00', NOW());
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 7, 650000, 'EXPENSE', 'Đổ xăng', '2025-10-12 08:00:00', NOW());
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 9, 280000, 'EXPENSE', 'Vé xem phim', '2025-10-05 19:00:00', NOW());
INSERT INTO transactions (user_id, category_id, amount, type, description, transaction_date, created_at) VALUES (1, 10, 599000, 'EXPENSE', 'Hóa đơn Internet', '2025-10-03 11:00:00', NOW());

-- Budgets for admin (user_id = 1)
INSERT INTO budgets (user_id, category_id, name, amount, start_date, end_date, created_at) VALUES (1, 6, 'Ngân sách ăn uống hàng tháng', 7500000, '2025-10-01', '2025-10-31', NOW());
INSERT INTO budgets (user_id, category_id, name, amount, start_date, end_date, created_at) VALUES (1, 7, 'Ngân sách đi lại', 2000000, '2025-10-01', '2025-10-31', NOW());
INSERT INTO budgets (user_id, category_id, name, amount, start_date, end_date, created_at) VALUES (1, NULL, 'Tổng chi tiêu', 20000000, '2025-10-01', '2025-10-31', NOW());

-- Goals for admin (user_id = 1)
INSERT INTO goals (user_id, name, target_amount, current_amount, target_date, status, created_at) VALUES (1, 'Quỹ mua Laptop mới', 15000000, 3500000, '2026-06-01', 'ACTIVE', NOW());
INSERT INTO goals (user_id, name, target_amount, current_amount, target_date, status, created_at) VALUES (1, 'Du lịch Đà Nẵng', 40000000, 12000000, '2026-12-20', 'ACTIVE', NOW());