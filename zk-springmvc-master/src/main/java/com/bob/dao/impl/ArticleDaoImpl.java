package com.bob.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bob.dao.ArticleDao;
import com.bob.model.Article;

@Repository
public class ArticleDaoImpl extends GenericDaoImpl<Article> implements ArticleDao {

    @Override
    public List<Map<String, Object>> getLatestArticles() {
        return getCustomNamedParameterJdbcTemplate().queryForListBySqlId("article_getLatestArticles", new HashMap<String, Object>());
    }

    @Override
    public List<Map<String, Object>> getRepliedArticles() {
        return getCustomNamedParameterJdbcTemplate().queryForListBySqlId("article_getRepliedArticles", new HashMap<String, Object>());
    }

    @Override
    public List<Map<String, Object>> getMyArticles(int id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", id);
        return getCustomNamedParameterJdbcTemplate().queryForListBySqlId("article_getMyArticles", map);
    }

}
