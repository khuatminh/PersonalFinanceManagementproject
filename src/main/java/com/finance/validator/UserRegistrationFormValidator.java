package com.finance.validator;
import com.finance.form.UserRegistrationForm;
import com.finance.service.UserService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component

public abstract class UserRegistrationFormValidator implements Validator {
    @Autowired
    UserService userService;
    @Override
    public boolean supports(Class<?> clazz)
    {
        return UserRegistrationForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        UserRegistrationForm form = (UserRegistrationForm) target;
        if(!form.isPasswordMatch()) {
            errors.rejectValue("confirmPassword", "error.userForm", "Passwords do not match");
        }

        if(userService.existsByUsername(form.getUsername())) {
            errors.rejectValue("username", "error.userForm", "Username already exists");
        }

        if(userService.existsByEmail(form.getEmail())) {
            errors.rejectValue("email", "error.userForm", "Email already exists");
        }
    }
}
