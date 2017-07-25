package com.bob.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bob.dao.ArticleDao;
import com.bob.model.Article;
import com.bob.utils.SqlStatement;

@Repository
public class ArticleDaoImpl implements ArticleDao {
	
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    SqlStatement sqlStatement;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    
    public void saveOrUpdate(Article article) {
    	sessionFactory.getCurrentSession().persist(article);
    }
    
    public List<Article> list() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Article A");
        List<Article> list = query.list();
        return list;
    }

	@Override
	public List<Article> findForDetail() {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Article A WHERE A.pid = NULL ");
        List<Article> list = query.list();
		return list;
	}

	@Override
	public List<Map<String, Object>> getNewArticles() {
		String sql = sqlStatement.getValue("article_getNewArticles");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, map);
		return result;
	}

	@Override
	public Article findById(int articleId) {
		Article a = (Article) sessionFactory.getCurrentSession().get(Article.class, articleId);
		return a;
	}

	@Override
	public List<Map<String, Object>> getRepliedArticles() {
		String sql = sqlStatement.getValue("article_getRepliedArticles");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, map);
		return result;
	}

}
