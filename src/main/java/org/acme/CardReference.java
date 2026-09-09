package org.acme;

import java.net.URI;
import java.net.URL;

import org.acme.dto.CardReferenceDto;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class CardReference extends PanacheEntity {

    enum Set {
        ORIGINS,
        ORIGINS_PROVING_GROUNDS,
        SPIRITFORGED,
        UNLEASHED,
        VENDETTA,
        RADIANCE
    }

    public Set valueOf(String set) {
        switch(set) {
            case "OGN":
                return Set.ORIGINS;
            case "OGS":
                return Set.ORIGINS_PROVING_GROUNDS;
            case "SFD":
                return Set.SPIRITFORGED;
            case "UNL":
                return Set.UNLEASHED;
            case "VEN":
                return Set.VENDETTA;
            case "RAD":
                return Set.RADIANCE;
        }
        return null;
    }

    public Long id;

    public String name;

    public Set set;

    public String imageUrl;

    public CardReference(Long id, String name, String set, String imageUrl){
        this.id = id;
        this.name = name;
        this.set = Set.valueOf(set);
        this.imageUrl = imageUrl;
    }

    public static CardReference findByName(String name){
        return find("name", name).firstResult();
    }

    public static CardReference fromCardReferenceDto(CardReferenceDto cardReferenceDto) {
        return new CardReference(Long.parseLong(cardReferenceDto.riftboundId), cardReferenceDto.name, cardReferenceDto.set.setId, cardReferenceDto.media.imageUrl);
    }
}
