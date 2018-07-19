package com.bob.resources;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bob.model.User;
import com.bob.service.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UserResource {

    @Autowired
    private UserService userService;

    @GET
    @PermitAll
    public List<User> getUsers() {
        List<User> list = userService.getUsers();
        return list;
    }

    @POST
    @PermitAll
    public Response addUser(User user, @Context UriInfo uriInfo) {
        user.setId(null);
        userService.addUser(user);
        String newId = String.valueOf(user.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri).entity(user).build();
    }

    @PUT
    @PermitAll
    @Path("/{username}")
    public User updateUser(@PathParam("username") String username, User user) {
        User updatedUser = userService.UpdateUser(username, user.getPassword(), user.getAuthority());
        return updatedUser;
    }

    @DELETE
    @PermitAll
    @Path("/{username}")
    public void deleteUser(@PathParam("username") String username) {
        userService.deleteUserByUsername(username);
    }

    @GET
    @PermitAll
    @Path("/{userId}")
    // @RolesAllowed("admin")
    public User getUser(@PathParam("userId") Integer userId, @Context UriInfo uriInfo) {
        User user = userService.findUserById(userId);
        // article.addLink(getUriForSelf(uriInfo, article), "self");
        // article.addLink(getUriForProfile(uriInfo, article), "profile");
        // article.addLink(getUriForComments(uriInfo, article), "comments");
        return user;

    }

}
