package org.acme;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Deck extends PanacheEntity {
    public String name;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Card> cards;
}
