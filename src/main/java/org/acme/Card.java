package org.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @ManyToOne
    public User owner;

    @ManyToOne
    public CardReference cardReference;

    public Card(Language language, Quality quality, int quantity, User owner, CardReference cardReference){
        this.language = language;
        this.quality = quality;
        this.quantity = quantity;
        this.owner = owner;
        this.cardReference = cardReference;
    }

    public static List<Card> findBorrowableCards(List<CardInput> cardInputs){
        return new ArrayList<>();
    }
}
