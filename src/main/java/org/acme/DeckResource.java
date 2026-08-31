package org.acme;

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

@Path("/decks")
public class DeckResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response decks() {
        List<Deck> decks = Deck.listAll();
        return Response.ok(decks).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDeck(Deck myDeck) {
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
