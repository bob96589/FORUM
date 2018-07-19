package com.bob.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bob.dao.ArticleDao;
import com.bob.dao.CommentDao;
import com.bob.dao.TagDao;
import com.bob.dao.UserDao;
import com.bob.model.Article;
import com.bob.model.Comment;
import com.bob.model.Tag;
import com.bob.service.ForumService;

@Service
public class ForumServiceImpl implements ForumService {

    private final Logger logger = LoggerFactory.getLogger(ForumService.class);

    @Autowired
    UserDao userDao;
    @Autowired
    ArticleDao articleDao;
    @Autowired
    CommentDao commentDao;
    @Autowired
    TagDao tagDao;

    @Override
    public List<Map<String, Object>> getLatestArticles() {
        return articleDao.getLatestArticles();
    }

    @Override
    public List<Map<String, Object>> getRepliedArticles() {
        return articleDao.getRepliedArticles();
    }

    @Override
    public List<Map<String, Object>> getMyArticles(int id) {
        return articleDao.getMyArticles(id);
    }

    @Override
    public Article findArticleById(int articleId) {
        return articleDao.find(Article.class, articleId);
    }

    @Override
    public Article saveOrUpdateArticle(Article article, Set<Tag> tags) {
        if (tags != null) {
            for (Tag tag : tags) {
                tagDao.save(tag);
            }
        }
        // article.setTags(tags);
        articleDao.save(article);
        logger.debug("Article: {}", article);
        return article;
    }

    @Override
    public void deleteArticle(Integer articleId) {
        Article article = articleDao.find(Article.class, articleId);
        articleDao.delete(article);
    }

    @Override
    public List<Article> getArticles() {
        return articleDao.findAll(Article.class);
    }

    @Override
    public List<Comment> getCommentsByArticleId(Integer articleId) {
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq("articleId", articleId));
        return commentDao.find(Comment.class, criterions);
    }

    @Override
    public Comment addComment(Integer articleId, Comment comment) {
        comment.setArticleId(articleId);
        comment.setId(null);
        return commentDao.save(comment);
    }

    @Override
    public Comment updateComment(Integer articleId, Integer commentId, Comment comment) {
        comment.setId(commentId);
        comment.setArticleId(articleId);
        return commentDao.save(comment);
    }

    @Override
    public void removeComment(Integer commentId) {
        Comment comment = commentDao.find(Comment.class, commentId);
        commentDao.delete(comment);
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        return commentDao.find(Comment.class, commentId);
    }

}
