package org.acme;

import java.time.Instant;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BorrowDto(Long id, Long ownerId, String ownerUsername, Long borrowerId, String borrowerUsername, List<Long> cardIds, Instant createdAt, Instant updatedAt) {

}
