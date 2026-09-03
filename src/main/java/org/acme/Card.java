package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Card extends PanacheEntity {

    enum Language {
        Chinese,
        English,
        French,
        Korean
    }

    public String name;
    public Language language;
    @ManyToOne
    public User owner;

    public Card(String name, Language language, User owner){
        this.name = name;
        this.language = language;
        this.owner = owner;
    }
}
