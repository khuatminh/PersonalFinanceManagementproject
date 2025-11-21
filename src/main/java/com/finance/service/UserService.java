package com.finance.service;

import com.finance.domain.Role;
import com.finance.domain.User;
import com.finance.exception.DuplicateUserException;
import com.finance.exception.InvalidPasswordException;
import com.finance.exception.UserNotFoundException;
import com.finance.repository.RoleRepository;
import com.finance.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;


    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.findByUsername(username);
    }


    public Optional<User> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // ĐĂNG KÍ VÀ QUẢN LÝ
    public User createUser(String username, String email, String password) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }


        if (userRepository.existByUserName(username)) {
            throw DuplicateUserException.forUserName(username);
        }

        if (userRepository.existByEmail(email)) {
            throw DuplicateUserException.forEmail(email);
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim().toLowerCase());


        user.setPassword(passwordEncoder.encode(password));


        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found in system"));
        user.setUserRole(userRole);

        return userRepository.save(user);

    }

    public User updateUser(Long id, User userDetails) {
        if(id == null)
        {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if(userDetails == null)
        {
            throw new IllegalArgumentException("User details cannot be null");
        }

        return userRepository.findById(id)
                .map(user -> {
                    if(StringUtils.hasText(userDetails.getUsername())) {
                        user.setUsername(userDetails.getUsername().trim());
                    }
                    if(StringUtils.hasText(userDetails.getEmail())) {
                        user.setEmail(userDetails.getEmail().trim().toLowerCase());
                    }
                    if(userDetails.getUserRole() != null) {
                        user.setUserRole(userDetails.getUserRole());
                    }
                    if(StringUtils.hasText(userDetails.getPassword())) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException(id));
    }

    public void deleteById(Long id) {
        if(id == null)
        {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if(!userRepository.existsById(id))
            {
            throw new UserNotFoundException(id);
            }
        userRepository.deleteById(id);
    }

    //VALIDATION
    //Kiểm tra liệu tên đăng nhập có tồn tại trên hệ thống hay chưa
    public boolean existsByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.existByUserName(username);
    }

    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.existByEmail(email);
    }
    //Tìm kiêm tên đăng nhập, email có chứa từ khóa

    public List<User> searchUsers(String keywords)
    {
        if(!StringUtils.hasText(keywords))
        {
            throw new IllegalArgumentException("Search keywords cannot be null or empty");
        }
        return userRepository.findByUsernameOrEmailContain(keywords.trim());
    }

    //Xác thực và bảo mật
    public boolean validatePassword(String rawPassword, String encodedPassword)
    {
        if(!StringUtils.hasText(rawPassword))
        {
            throw new IllegalArgumentException("Raw password cannot be null or empty");
        }
        if(!StringUtils.hasText(encodedPassword))
        {
            throw new IllegalArgumentException("Encoded password cannot be null or empty");
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void updateProfile(Long userID, User userDetails)
    {
        if(userID == null)
        {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if(userDetails  == null)
        {
            throw new IllegalArgumentException("User details cannot be null");
        }

        User user = userRepository.findById(userID)
                .orElseThrow(()->new UserNotFoundException(userID));

        if(!StringUtils.hasText(user.getUsername()))
        {
            user.setUsername(userDetails.getUsername().trim());
        }
        if(!StringUtils.hasText(user.getEmail()))
        {
            user.setEmail(userDetails.getEmail().trim().toLowerCase());
        }
        userRepository.save(user);
    }

    public void changePassword(Long userID, String currentPassword, String newPassword)
    {
        if(userID == null)
        {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if(!StringUtils.hasText(currentPassword))
        {
            throw new IllegalArgumentException("Current password cannot be null or empty");
        }
        if(!StringUtils.hasText(newPassword))
        {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }

        User user =  userRepository.findById(userID)
                .orElseThrow(()->new UserNotFoundException(userID));

        if(!validatePassword(currentPassword, user.getPassword()))
        {
            throw InvalidPasswordException.incorrectCurrentPassword();
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}