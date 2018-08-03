package com.bob.service;

import java.util.List;
import java.util.Map;

import com.bob.model.Article;
import com.bob.model.Comment;

public interface ForumService {

    List<Map<String, Object>> getLatestArticles();

    List<Map<String, Object>> getRepliedArticles();

    List<Map<String, Object>> getMyArticles(int id);

    Article findArticleById(int articleId);

    Article saveOrUpdateArticle(Article article);

    void deleteArticle(Integer articleId);

    List<Article> getArticles();

    List<Comment> getCommentsByArticleId(Integer articleId);

    Comment addComment(Integer articleId, Comment comment);

    Comment updateComment(Integer articleId, Integer commentId, Comment comment);

    void removeComment(Integer commentId);

    Comment getCommentById(Integer commentId);

}
