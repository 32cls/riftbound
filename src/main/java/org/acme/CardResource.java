package org.acme;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/cards")
public class CardResource {

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCards() {
        List<Card> cards = Card.listAll();
        return Response.ok(cards).build();
    }

    @POST
    @Transactional
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCard(Card card) {
        card.persist();
        return Response.created(URI.create("/cards/"+card.id)).build();
    }

    @DELETE
    @Path("/{cardId}")
    @RolesAllowed("user")
    @Transactional
    public Response deleteDeck(Long cardId) {
        Card found = Card.findById(cardId);
        if (found == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Card.deleteById(cardId);
        return Response.noContent().build();
    }
}
