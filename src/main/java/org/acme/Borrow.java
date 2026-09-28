package org.acme;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Borrow extends PanacheEntity {

    @OneToOne
    public User owner;
    @OneToOne
    public User borrower;
    @OneToMany
    public List<Card> cards;
    @CreationTimestamp 
    private Instant createdAt;
    @UpdateTimestamp 
    private Instant updatedAt;
        
    public Borrow(User owner, User borrower, List<Card> cards) {
        this.owner = owner;
        this.borrower = borrower;
        this.cards = cards;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static List<Borrow> createBorrowings(List<String> cardIds, User user){
        List<Card> cards = Card.findByIds(cardIds);
        
        Map<Long, List<Card>> byOwner = cards.stream().collect(Collectors.groupingBy(c -> c.owner.id));
    
        return byOwner.entrySet().stream().map(entry -> {
            User owner = User.findById(entry.getKey());
            Borrow b = new Borrow(owner, user, entry.getValue());
            b.persist();
            cards.forEach(card -> { 
                card.borrower = user; 
                card.isBorrowed = true;
                card.persist(); 
            });
            return b;
        }).toList();
    }

}
