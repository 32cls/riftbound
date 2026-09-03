package org.acme;

import jakarta.validation.constraints.NotBlank;

public class UserInput {
    @NotBlank
    public String username;
    @NotBlank
    public String password;
}
