package org.acme;

import java.util.List;

import io.quarkus.hibernate.orm.panache.Panache;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
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
        User authenticatedUser = User.findById(ctx.getUserPrincipal().getName());
        List<Borrow> borrowings = Borrow.createBorrowings(cardIds, authenticatedUser);
        Panache.flush();
        List<BorrowDto> dtos = borrowings.stream().map(b -> new BorrowDto(
                b.id,
                b.owner.id, b.owner.username,
                b.borrower.id, b.borrower.username,
                b.cards.stream().map(c -> c.id).toList(),
                b.getCreatedAt(), b.getUpdatedAt())).toList();
        return Response.ok(dtos).build();
    }

}
