package com.finance.validator;

import com.finance.form.UserRegistrationForm;
import com.finance.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegistrationFormValidator implements Validator {

    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationForm form = (UserRegistrationForm) target;

        if (!form.isPasswordMatch()) {
            errors.rejectValue("confirmPassword", "error.userForm", "Mật khẩu không khớp");
        }

        if (userService.existsByUsername(form.getUsername())) {
            errors.rejectValue("username", "error.userForm", "Tên người dùng đã tồn tại");
        }

        if (userService.existsByEmail(form.getEmail())) {
            errors.rejectValue("email", "error.userForm", "Email đã tồn tại");
        }
    }
}
