package com.finance.service;

import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatisticsService {
    UserRepository userRepository;

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
