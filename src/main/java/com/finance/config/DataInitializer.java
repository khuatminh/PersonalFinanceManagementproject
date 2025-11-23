package com.finance.config;

import com.finance.domain.Role;
import com.finance.domain.User;
import com.finance.repository.RoleRepository;
import com.finance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Chỉ kiểm tra và tạo Role nếu bảng Role đang trống
            if (roleRepository.count() == 0) {

                // Tạo role ADMIN nếu chưa có
                if (!roleRepository.existsByName("ADMIN")) {
                    roleRepository.save(new Role("ADMIN"));
                }

                // QUAN TRỌNG: Tạo role USER để người dùng mới có thể đăng ký
                if (!roleRepository.existsByName("USER")) {
                    roleRepository.save(new Role("USER"));
                }

                System.out.println("Đã khởi tạo Role: USER và ADMIN");
            }

            // Lấy Role ADMIN sau khi đã đảm bảo nó tồn tại
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not found after initialization."));

            // 2. Khởi tạo Admin User mặc định
            User admin = userRepository.findByUsername("admin").orElse(null);
            if (admin == null) {
                admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@finance.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setUserRole(adminRole);
                userRepository.save(admin);
                System.out.println("Đã tạo tài khoản Admin mặc định: admin / admin123");
            } else {
                // Nếu user admin đã tồn tại, đảm bảo role là ADMIN
                if (admin.getUserRole() == null || !admin.getUserRole().getName().equals("ADMIN")) {
                    admin.setUserRole(adminRole);
                    userRepository.save(admin);
                    System.out.println("Đã cập nhật quyền ADMIN cho tài khoản: admin");
                }
            }
        };
    }
}