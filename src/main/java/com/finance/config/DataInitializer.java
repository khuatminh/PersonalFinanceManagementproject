package com.finance.config;

import com.finance.domain.Role;
import com.finance.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository) {
        return args -> {
            // Chỉ kiểm tra và tạo Role nếu bảng Role đang trống
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
        };
    }
}