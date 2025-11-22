package com.finance.form;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserRegistrationForm {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provided a valid email")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password is at least 6 characters")
    String password;

    @NotBlank(message = "Confirm password is required")
    String confirmPassword;

    public boolean isPasswordMatch()
    {
        return password != null && password.equals(confirmPassword);
    }
}
