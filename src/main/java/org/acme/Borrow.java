package org.acme;

import java.util.List;

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

    public Borrow(User owner, User borrower, List<Card> cards) {
        this.owner = owner;
        this.borrower = borrower;
        this.cards = cards;
    }

}
