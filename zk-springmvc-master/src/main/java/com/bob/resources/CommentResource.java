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

import com.bob.model.Comment;
import com.bob.service.ForumService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class CommentResource {

    @Autowired
    ForumService forumService;

    @GET
    @PermitAll
    public List<Comment> getComments(@PathParam("articleId") Integer articleId) {
        List<Comment> replies = forumService.getCommentsByArticleId(articleId);
        return replies;
    }

    @POST
    @PermitAll
    public Response addComment(@PathParam("articleId") Integer articleId, Comment comment, @Context UriInfo uriInfo) {
        forumService.addComment(articleId, comment);
        String newId = String.valueOf(comment.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri).entity(comment).build();
    }

    @PUT
    @Path("/{commentId}")
    @PermitAll
    public Comment updateComment(@PathParam("articleId") Integer articleId, @PathParam("commentId") Integer commentId, Comment comment) {
        return forumService.updateComment(articleId, commentId, comment);
    }

    @DELETE
    @Path("/{commentId}")
    @PermitAll
    public void deleteComment(@PathParam("articleId") Integer articleId, @PathParam("commentId") Integer commentId) {
        forumService.removeComment(commentId);
    }

    @GET
    @Path("/{commentId}")
    @PermitAll
    public Comment getComment(@PathParam("articleId") Integer articleId, @PathParam("commentId") Integer commentId) {
        return forumService.getCommentById(commentId);
    }

}
