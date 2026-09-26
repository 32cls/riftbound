package org.acme;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.ws.rs.NotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.util.List;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

@Entity
@Table(name="app_users")
@UserDefinition
@JsonIdentityInfo (
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id")
public class User extends PanacheEntity {
    @Username
    public String username;

    @Password
    @JsonIgnore
    public String password;
    @Roles
    public String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @JsonManagedReference
    public List<Card> inventory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "borrower")
    @JsonManagedReference
    public List<Card> borrowing;

    public Point location;

    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Adds a new user to the database
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    public static Long add(String username, String password) {
        User user = new User();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = "user";
        user.persist();
        return user.id;
    }

    public static User checkLogin(String username, String password) throws NotFoundException {
        User user = find("username", username).firstResult();
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (BcryptUtil.matches(password, user.password)) {
            return user;
        } else {
            throw new RuntimeException("Invalid password exception");
        }
    }

}
