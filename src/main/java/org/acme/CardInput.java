package org.acme;

import org.acme.Card.Language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CardInput {
    @NotBlank
    public String name;
    @NotBlank
    public Language language;
    @Positive
    public int quantity;
}
