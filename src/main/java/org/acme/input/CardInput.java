package org.acme.input;

import org.acme.domain.Card.Language;
import org.acme.domain.Card.Quality;

import jakarta.validation.constraints.NotBlank;

public class CardInput {
    @NotBlank
    public String name;
    public Language language;
    public Quality quality;
}
