package org.acme;

import java.net.URL;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class CardReference extends PanacheEntity {

    enum Set {
        ORIGINS,
        PROVING_GROUNDS,
        SPIRITFORGED,
        UNLEASHED,
        VENDETTA,
        RADIANCE
    }

    public String name;

    public Set set;

    public URL imageUrl;

    public static CardReference findByName(String name){
        return find("name", name).firstResult();
    }
}
