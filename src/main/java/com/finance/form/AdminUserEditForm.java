package com.finance.form;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.finance.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserEditForm {
    @NotEmpty(message = "Username cannot be empty")
    String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    String email;

    @NotNull(message = "Role must be selected")
    Role role;

}
