package org.acme;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

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

}
