package com.bob.dao;

import java.util.List;
import java.util.Map;

import com.bob.model.Article;

public interface ArticleDao {

	public Article findById(int articleId);

	public void saveOrUpdate(Article article);

	public List<Map<String, Object>> getLatestArticles();

	public List<Map<String, Object>> getRepliedArticles();

	public List<Map<String, Object>> getMyArticles(int id);

	public List<Article> getArticlesForListView();

	public List<Article> getArticlesForTreeView();

}
