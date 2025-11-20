package com.finance.service;

import com.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service

public class UserStatisticsService {
    private UserRepository userRepository;

    public long getUserCount() {
        return userRepository.count();
    }

    public long getAdminCount() {
        return userRepository.countByAdminRole();
    }

    public long getRegularCount() {
        return userRepository.countUserRole();
    }
}
