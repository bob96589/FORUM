package com.bob.dao;

import java.util.List;
import java.util.Map;

import com.bob.model.Article;

public interface ArticleDao extends GenericDao<Article> {

    public List<Map<String, Object>> getLatestArticles();

    public List<Map<String, Object>> getRepliedArticles();

    public List<Map<String, Object>> getMyArticles(int id);

}
