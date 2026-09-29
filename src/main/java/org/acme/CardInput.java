package org.acme;

import org.acme.Card.Language;
import org.acme.Card.Quality;

import jakarta.validation.constraints.NotBlank;

public class CardInput {
    @NotBlank
    public String name;
    public Language language;
    public Quality quality;
}
