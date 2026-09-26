package org.acme;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIdentityInfo (
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id")
public class Card extends PanacheEntity {

    enum Language {
        CHINESE,
        ENGLISH,
        FRENCH,
        KOREAN
    }

    enum Quality {
        MINT,
        NEAR_MINT,
        GOOD,
        PLAYED,
        POOR
    }

    public Quality quality;
    public Language language;
    public int quantity;
    public boolean isBorrowed;
    
    @ManyToOne
    public User owner;

    @ManyToOne
    public User borrower;

    @ManyToOne
    public CardReference cardReference;

    public Card(Language language, Quality quality, int quantity, User owner, CardReference cardReference){
        this.language = language;
        this.quality = quality;
        this.quantity = quantity;
        this.owner = owner;
        this.cardReference = cardReference;
        this.isBorrowed = false;
    }

    public static List<Card> findBorrowableCards(BorrowableCardsInput borrowableCardsInput){
        List<Card> borrowableCards = borrowableCardsInput.cards.stream().map(cardInput -> {
            Card card = Card.find("name LIKE ?1 AND LANGUAGE = ?2 AND QUALITY <= ?3 AND ISBORROWED IS FALSE INNER JOIN app_users ON app_users.id = card.owner ORDER BY ST_Distance(ST_Transform(?4), ST_Transform(card.owner.location)) ASC, QUANTITY DESC", cardInput.name, cardInput.language, cardInput.quality, borrowableCardsInput.location).firstResult();
            return card;
        }).toList();
        return borrowableCards;
    }
}
