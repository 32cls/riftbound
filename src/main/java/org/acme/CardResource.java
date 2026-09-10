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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.acme.Card.Language;
import org.acme.Card.Quality;
import org.jboss.resteasy.reactive.RestForm;

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
    public Response addCard(@Context SecurityContext ctx, CardInput cardInput) {
        Set<ConstraintViolation<CardInput>> violations = validator.validate(cardInput);
        if (violations.isEmpty()) {
            User authenticatedUser = User.findById(ctx.getUserPrincipal().getName());
            CardReference cardReference = CardReference.findByName(cardInput.name);
            if (cardReference == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            Card card = new Card(cardInput.language, cardInput.quality, cardInput.quantity, authenticatedUser, cardReference);
            card.persist();
            return Response.created(URI.create("/cards/"+card.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Transactional
    @RolesAllowed("user")
    @Path("/list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addCardList(@Context SecurityContext ctx, @RestForm("list") File file) {
        User authenticatedUser = User.findById(ctx.getUserPrincipal().getName());
        if (!file.canRead()){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "File cannot be read").build();
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            List<Card> cards = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parsedLine = line.split(" ", 2);
                int quantity = Integer.parseInt(parsedLine[0]);
                String parsedCardName = parsedLine[1].split(" \\(", 2)[0].replace(",", "").replace(" -", "");
                CardReference cardReference = CardReference.findByName(parsedCardName);
                if (cardReference == null) {
                    System.out.println("couldn't find card with name : " + parsedCardName);
                }
                cards.add(new Card(Language.ENGLISH, Quality.NEAR_MINT, quantity, authenticatedUser, cardReference));
            }
            Card.persist(cards);
            scanner.close();
        } catch (FileNotFoundException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "File not found").build();
        }
        return Response.created(URI.create("/cards")).build();
    }

    @DELETE
    @Path("/{cardId}")
    @RolesAllowed("user")
    @Transactional
    public Response deleteDeck(@Context SecurityContext ctx, Long cardId) {
        if (ctx.getUserPrincipal() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Card found = Card.findById(cardId);
        if (found == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (found.owner.id != Long.parseLong(ctx.getUserPrincipal().getName())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Card.deleteById(cardId);
        return Response.noContent().build();
    }
}
