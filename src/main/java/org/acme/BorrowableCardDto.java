package org.acme;

import org.acme.Card.Quality;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BorrowableCardDto(Long cardId, String name, String set, int quantity, Quality quality, String ownerName, String riftboundId, String imageUrl, Double dist) {
    
}
