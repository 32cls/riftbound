package org.acme;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Set;

@Path("/users")
public class UserResource {

    @Inject
    Validator validator;

    @POST
    @Transactional
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserInput userInput) {
        Set<ConstraintViolation<UserInput>> violations = validator.validate(userInput);
        if (violations.isEmpty()) {
            Long userId = User.add(userInput.username, userInput.password);
            return Response.created(URI.create("/users/"+userId)).build();
        } else {
            violations.forEach(v -> { System.out.println(v.getMessage());});
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

}
