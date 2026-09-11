package org.acme;

import java.net.URI;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/borrows")
public class BorrowResource {

    @Inject
    Validator validator;

    @POST
    @Transactional
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response borrowCards(@Context SecurityContext ctx, List<CardInput> inputCards){
        User authenticatedUser = User.findById(ctx.getUserPrincipal().getName());
        List<Card> cards = Card.findBorrowableCards(inputCards);
        Borrow borrow = new Borrow();
        return Response.created(URI.create("/borrows/" + borrow.id)).build();
    }
}
