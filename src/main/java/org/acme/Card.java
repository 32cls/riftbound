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

    public static List<Card> findBorrowableCards(List<CardInput> cardInputs){
        List<Card> borrowableCards = cardInputs.stream().map(cardInput -> {
            Card card = Card.find("name LIKE ?1 AND LANGUAGE = ?2 AND QUALITY <= ?3 AND ISBORROWED IS FALSE ORDER BY QUANTITY DESC", cardInput.name, cardInput.language, cardInput.quality).firstResult();
            return card;
        }).toList();
        return borrowableCards;
    }
}
