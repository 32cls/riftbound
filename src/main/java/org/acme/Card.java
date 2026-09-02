package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Card extends PanacheEntity {
    public String name;
    public int quantity;
    @ManyToOne
    public User owner;
}
