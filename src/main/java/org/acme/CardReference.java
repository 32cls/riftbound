package org.acme;

import org.acme.dto.CardReferenceDto;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class CardReference extends PanacheEntity {

    public String riftboundId;

    public String cardName;

    public String set;

    public String imageUrl;

    public boolean isNew;

    public CardReference(String riftboundId, String name, String set, String imageUrl, boolean isNew){
        this.riftboundId = riftboundId;
        this.cardName = name;
        this.set = set;
        this.imageUrl = imageUrl;
        this.isNew = isNew;
    }

    public static CardReference findByName(String name){
        return find("cardName", name).firstResult();
    }

    public static CardReference fromCardReferenceDto(CardReferenceDto cardReferenceDto) {
        String correctedName = cardReferenceDto.name.replace(",", "").replace(" -", "");
        return new CardReference(cardReferenceDto.riftboundId, correctedName, cardReferenceDto.set.setId, cardReferenceDto.media.imageUrl, cardReferenceDto.isNew);
    }
}
