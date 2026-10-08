package org.acme.dto;

import org.acme.domain.Card.Quality;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BorrowableCardDto(Long cardId, String name, String set, Quality quality, String ownerName, String riftboundId, String imageUrl, Double dist) {
    
}
