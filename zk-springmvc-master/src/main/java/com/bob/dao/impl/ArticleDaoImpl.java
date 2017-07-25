package com.bob.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bob.dao.ArticleDao;
import com.bob.model.Article;
import com.bob.model.User;
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
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Article A WHERE A.status = 0 ORDER BY A.createTime DESC");
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
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter("test");
		Query query = session.createQuery("FROM Article A WHERE A.id = :articleId AND A.status = 0");
		query.setParameter("articleId", articleId);
		Article a = (Article) query.uniqueResult();
//		Article a = (Article) session.get(Article.class, articleId);
		session.disableFilter("test");
		return a;
	}

	@Override
	public List<Map<String, Object>> getRepliedArticles() {
		String sql = sqlStatement.getValue("article_getRepliedArticles");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, map);
		return result;
	}

	@Override
	public List<Map<String, Object>> getMyArticles(int id) {
		String sql = sqlStatement.getValue("article_getMyArticles");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", id);
		List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, map);
		return result;
	}

	@Override
	public void save(Article article) {
		sessionFactory.getCurrentSession().persist(article);
	}

}
