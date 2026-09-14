package org.acme;

import jakarta.validation.constraints.NotBlank;

public class LoginInput {
    @NotBlank
    public String username;
    @NotBlank
    public String password;

}
