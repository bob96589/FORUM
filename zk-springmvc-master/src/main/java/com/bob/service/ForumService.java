package com.bob.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bob.model.Article;
import com.bob.model.Tag;
import com.bob.model.User;

public interface ForumService {

	List<Map<String, Object>> getLatestArticles();

	List<Map<String, Object>> getRepliedArticles();

	List<Map<String, Object>> getMyArticles(int id);

	List<Article> getArticlesForListView();

	List<Article> getArticlesForTreeView();

	Article findArticleById(int articleId);

	void saveOrUpdateArticle(Article article, Set<Tag> tags);

	void deleteArticle(Integer articleId);

	User findUserByAccount(String username);

	List<Tag> getAllTag();

}
