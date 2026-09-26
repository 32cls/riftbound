package org.acme;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class BorrowableCardsInput {
    @NotEmpty
    List<CardInput> cards;
    @NotBlank
    LocationInput location;
}
