package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/users")
public class UserResource {

    @POST
    @Transactional
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserInput userInput) {
        Long userId = User.add(userInput.username, userInput.password);
        return Response.created(URI.create("/users/"+userId)).build();
    }

}
