package org.acme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserInput {
    @NotBlank
    public String username;
    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
        message = "must be at least 8 characters and contain at least one lowercase letter, one uppercase letter, one digit, and one special symbol")
    public String password;
}
