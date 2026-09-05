package org.acme;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
import java.util.Set;

@Path("/cards")
public class CardResource {

    @Inject
    Validator validator;

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
    public Response addCard(CardInput cardInput) {
        Set<ConstraintViolation<CardInput>> violations = validator.validate(cardInput);
        if (violations.isEmpty()) {
            CardReference cardReference = CardReference.findByName(cardInput.name);
            if (cardReference == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            Card card = new Card(cardInput.language, cardInput.quality, cardInput.quantity, new User(), cardReference);
            card.persist();
            return Response.created(URI.create("/cards/"+card.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
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
