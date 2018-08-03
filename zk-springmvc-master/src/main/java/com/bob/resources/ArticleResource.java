package com.bob.resources;

import java.net.URI;
import java.util.List;

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

import com.bob.model.Article;
import com.bob.service.ForumService;

@Path("/articles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ArticleResource {

    @Autowired
    ForumService forumService;

    @Autowired
    CommentResource commentResource;

    @GET
    public List<Article> getArticles() {
        List<Article> articleList = forumService.getArticles();
        return articleList;
    }

    @POST
    public Response addArticle(Article article, @Context UriInfo uriInfo) {
        article.setId(null);
        forumService.saveOrUpdateArticle(article);
        String newId = String.valueOf(article.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri).entity(article).build();
    }

    @PUT
    @Path("/{articleId}")
    public Article updateArticle(@PathParam("articleId") Integer id, Article article) {
        article.setId(id);
        return forumService.saveOrUpdateArticle(article);
    }

    @DELETE
    @Path("/{articleId}")
    public void deleteArticle(@PathParam("articleId") Integer id) {
        forumService.deleteArticle(id);
    }

    @GET
    @Path("/{articleId}")
    public Article getArticle(@PathParam("articleId") Integer id, @Context UriInfo uriInfo) {
        Article article = forumService.findArticleById(id);
        if (article != null) {
            article.addLink(getUriForSelf(uriInfo, article), "self");
            article.addLink(getUriForProfile(uriInfo, article), "user");
            article.addLink(getUriForComments(uriInfo, article), "comments");
        }
        return article;

    }

    private String getUriForComments(UriInfo uriInfo, Article article) {
        URI uri = uriInfo.getBaseUriBuilder().path(ArticleResource.class).path(ArticleResource.class, "getCommentResource").path(CommentResource.class).resolveTemplate("articleId", article.getId())
                .build();
        return uri.toString();
    }

    private String getUriForProfile(UriInfo uriInfo, Article article) {
        URI uri = uriInfo.getBaseUriBuilder().path(UserResource.class).path(Integer.toString(article.getUserId())).build();
        return uri.toString();
    }

    private String getUriForSelf(UriInfo uriInfo, Article article) {
        String uri = uriInfo.getBaseUriBuilder().path(ArticleResource.class).path(Integer.toString(article.getId())).build().toString();
        return uri;
    }

    @Path("/{articleId}/comments")
    public CommentResource getCommentResource() {
        return commentResource;
    }

}
