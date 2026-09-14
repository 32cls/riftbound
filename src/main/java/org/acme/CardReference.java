package org.acme;

import org.acme.dto.CardReferenceDto;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

import org.json.JSONObject;

@Entity
public class CardReference extends PanacheEntity {

    public String riftboundId;

    public String name;

    public String set;

    public String imageUrl;

    public boolean isNew;

    @PostPersist
    protected void onCreate() {
        updateIndexForCard();
    }

    @PostUpdate
    protected void onUpdate() {
        updateIndexForCard();
    }

    public CardReference(){
    }

    public CardReference(String riftboundId, String name, String set, String imageUrl, boolean isNew){
        this.riftboundId = riftboundId;
        this.name = name;
        this.set = set;
        this.imageUrl = imageUrl;
        this.isNew = isNew;
    }

    public static CardReference findByName(String name){
        return find("name", name).firstResult();
    }

    public static CardReference fromCardReferenceDto(CardReferenceDto cardReferenceDto) {
        String correctedName = cardReferenceDto.name.replace(",", "").replace(" -", "");
        return new CardReference(cardReferenceDto.riftboundId, correctedName, cardReferenceDto.set.setId, cardReferenceDto.media.imageUrl, cardReferenceDto.isNew);
    }

    private void updateIndexForCard(){
        Client client = CDI.current().select(Client.class).get();
        Index index = client.index("card_references");
        JSONObject jsonCard = new JSONObject().put("id", this.id.toString()).put("riftbound_id", this.riftboundId).put("name", this.name).put("image_url", this.imageUrl);
        index.addDocuments(jsonCard.toString());
    }
}
