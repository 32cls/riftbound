package org.acme;

import java.time.Instant;
import java.util.List;


public record DeckDto(Long id, Long ownerId, List<Card> ownedCards, List<Card> missingCards, Double completion, Instant createdAt, Instant updatedAt) {

}
