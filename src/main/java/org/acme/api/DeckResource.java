package org.acme.api;

import java.net.URI;
import java.util.List;

import org.acme.domain.Card;
import org.acme.domain.Deck;
import org.acme.domain.User;
import org.acme.dto.DeckDto;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/decks")
public class DeckResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed("user")
    public Response decks(@Context SecurityContext ctx) {
        User authenticatedUser = User.findById(Long.parseLong(ctx.getUserPrincipal().getName()));
        List<Deck> decks = Deck.findDecksByUserId(authenticatedUser);
        return Response.ok(decks).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/{id}")
    @RolesAllowed("user")
    public Response deck(@Context SecurityContext ctx, String id) {
        User authenticatedUser = User.findById(Long.parseLong(ctx.getUserPrincipal().getName()));
        Deck deck = Deck.findById(id);
        if (authenticatedUser.id != deck.owner.id) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        List<Card> missingCards = deck.cards.stream().filter(card -> !authenticatedUser.inventory.contains(card)).toList();
        DeckDto deckDto = Deck.mapDeckToDto(deck, missingCards);
        return Response.ok(deckDto).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response createDeck(@Context SecurityContext ctx, Deck myDeck) {
        User authenticatedUser = User.findById(Long.parseLong(ctx.getUserPrincipal().getName()));
        myDeck.owner = authenticatedUser;
        myDeck.persist();
        return Response.created(URI.create("/decks/"+myDeck.id)).build();
    }

    @DELETE
    @Path("/{deckId}")
    @Transactional
    public Response deleteDeck(Long deckId) {
        Deck found = Deck.findById(deckId);
        if (found == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Deck.deleteById(deckId);
        return Response.noContent().build();
    }
}
