package com.bob.dao;

import java.util.List;
import java.util.Map;

import com.bob.model.Article;

public interface ArticleDao {
	
	public void saveOrUpdate(Article article);
	
    public List<Article> list();

	public List<Article> findForDetail();

	public List<Map<String, Object>> getNewArticles();

	public Article findById(int articleId);

	public List<Map<String, Object>> getRepliedArticles();

	public List<Map<String, Object>> getMyArticles(int id);

	public void save(Article article);

	public List<Article> findForArticleTree();

}
