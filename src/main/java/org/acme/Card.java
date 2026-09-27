package org.acme;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Card.findBorrowableCards", query = "SELECT c.id, cr.name, cr.set, c.quantity, c.quality, c.owner.username, cr.riftboundId, cr.imageUrl, distance_meters(:location, u.location) as dist FROM Card c INNER JOIN User u ON u.id = c.owner.id INNER JOIN CardReference cr ON c.cardReference.id = cr.id WHERE cr.name LIKE :name AND c.language = :language AND c.quality <= :quality AND c.isBorrowed IS FALSE AND u.id <> :id ORDER BY dist ASC, c.quantity DESC LIMIT 10"),
})
@JsonIdentityInfo(
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

    public static List<BorrowableCardDto> findBorrowableCards(List<CardInput> cards, User user){
        List<BorrowableCardDto> borrowableCards = cards.stream().map(cardInput -> {
            user.location.setSRID(4326);
            BorrowableCardDto bCard = find("#Card.findBorrowableCards", Map.of("name", cardInput.name, "language", cardInput.language, "quality", cardInput.quality, "id", user.id, "location", user.location)).project(BorrowableCardDto.class).firstResult();
            return bCard;
        }).toList();
        return borrowableCards;
    }
}
