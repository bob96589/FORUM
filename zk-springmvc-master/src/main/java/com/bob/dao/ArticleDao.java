package com.bob.dao;

import java.util.List;
import java.util.Map;

import com.bob.model.Article;

public interface ArticleDao {
	
	public void saveOrUpdate(Article article);
	
    public List<Article> list();

	public List<Article> findForDetail();

	public List<Map<String, Object>> getNewArticles();

}
