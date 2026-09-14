package org.acme.auth;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.Duration;
import java.util.Set;

import org.acme.User;
import org.acme.LoginInput;
import org.acme.RegisterInput;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.jwt.build.Jwt;

@Path("/auth")
public class AuthResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    Validator validator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    @PermitAll
    public Response login(LoginInput loginInput) {
        User authenticatedUser = null;
        try {
            authenticatedUser = User.checkLogin(loginInput.username, loginInput.password);
        } catch (RuntimeException e){
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), e.getLocalizedMessage()).build();
        }
        String newJwtCookie = Jwt.upn(authenticatedUser.id.toString())
                                .groups(authenticatedUser.role)
                                .claim("username", authenticatedUser.username)
                                .issuer("https://example.com/issuer")
                                .expiresIn(Duration.ofDays(7))
                                .sign();
        NewCookie newCookie = new NewCookie.Builder("jwt").path("/").value(newJwtCookie).build();
        return Response.ok("").cookie(newCookie).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    @Transactional
    @PermitAll
    public Response register(RegisterInput userInput) {
        Set<ConstraintViolation<RegisterInput>> violations = validator.validate(userInput);
        if (violations.isEmpty() && userInput.password == userInput.confirmPassword) {
            Long userId = User.add(userInput.username, userInput.password);
            return Response.created(URI.create("/users/"+userId)).build();
        } else {
            violations.forEach(v -> { System.out.println(v.getMessage());});
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

    }

}
