package org.acme;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/borrows")
public class BorrowResource {

    @POST
    @Transactional
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response borrowCard(@Context SecurityContext ctx, List<String> cardIds){
        User authenticatedUser = User.findById(Long.parseLong(ctx.getUserPrincipal().getName()));
        List<Borrow> borrowings = Borrow.createBorrowings(cardIds, authenticatedUser);
        List<BorrowDto> dtos = Borrow.mapBorrowingsToDto(borrowings);
        return Response.ok(dtos).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response returnBorrow(@Context SecurityContext ctx, Long borrowId){
        User authenticatedUser = User.findById(Long.parseLong(ctx.getUserPrincipal().getName()));
        Borrow borrow = Borrow.findById(borrowId);
        if (borrow.borrower.id != authenticatedUser.id) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        borrow.returned = true;
        borrow.persist();
        return Response.ok(borrow).build();
    }

    @GET
    @Transactional
    @RolesAllowed("user")
    public Response listBorrowings(@Context SecurityContext ctx){
        User authenticatedUser = User.findById(Long.parseLong(ctx.getUserPrincipal().getName()));
        List<Borrow> borrowings = Borrow.findByUserId(authenticatedUser);
        List<BorrowDto> dtos = Borrow.mapBorrowingsToDto(borrowings);
        return Response.ok(dtos).build();
    }

}
