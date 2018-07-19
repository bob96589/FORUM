package com.bob.resources;

import javax.annotation.security.PermitAll;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bob.model.User;
import com.bob.security.api.AuthenticationTokenDetails;
import com.bob.security.api.TokenBasedSecurityContext;
import com.bob.security.model.AuthenticationToken;
import com.bob.security.model.UserCredentials;
import com.bob.security.service.AuthenticationTokenService;
import com.bob.security.service.UsernamePasswordValidator;

/**
 * JAX-RS resource class that provides operations for authentication.
 *
 * @author cassiomolin
 */
// @RequestScoped
@Path("auth")
@Component
public class AuthenticationResource {

    @Context
    private SecurityContext securityContext;

    @Autowired
    private UsernamePasswordValidator usernamePasswordValidator;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    /**
     * Validate user credentials and issue a token for the user.
     *
     * @param credentials
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response authenticate(UserCredentials credentials) {

        User user = usernamePasswordValidator.validateCredentials(credentials.getUsername(), credentials.getPassword());
        String token = authenticationTokenService.issueToken(user.getUsername(), user.getAuthorities());
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(token);
        return Response.ok(authenticationToken).build();
    }

    /**
     * Refresh the authentication token for the current user.
     *
     * @return
     */
    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh(@Context ContainerRequestContext requestContext) {

        AuthenticationTokenDetails tokenDetails = ((TokenBasedSecurityContext) requestContext.getSecurityContext()).getAuthenticationTokenDetails();
        String token = authenticationTokenService.refreshToken(tokenDetails);

        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(token);
        return Response.ok(authenticationToken).build();
    }
}
