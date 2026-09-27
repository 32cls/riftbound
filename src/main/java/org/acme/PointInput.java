package org.acme;

import jakarta.validation.constraints.NotBlank;

public class PointInput {
    @NotBlank 
    public Long X;
    @NotBlank 
    public Long Y;
}
