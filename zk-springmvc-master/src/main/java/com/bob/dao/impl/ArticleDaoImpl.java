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
import com.bob.utils.SqlStatement;

@Repository
public class ArticleDaoImpl implements ArticleDao {

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	SqlStatement sqlStatement;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void saveOrUpdate(Article article) {
		sessionFactory.getCurrentSession().saveOrUpdate(article);
	}

	@Override
	public Article findById(int articleId) {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter("statusFilter");
		Query query = session.createQuery("FROM Article A WHERE A.id = :articleId AND A.status = 0");
		query.setParameter("articleId", articleId);
		Article article = (Article) query.uniqueResult();
		session.disableFilter("statusFilter");
		return article;
	}

	@Override
	public List<Map<String, Object>> getLatestArticles() {
		String sql = sqlStatement.getValue("article_getLatestArticles");
		return namedParameterJdbcTemplate.queryForList(sql, new HashMap<String, Object>());
	}

	@Override
	public List<Map<String, Object>> getRepliedArticles() {
		String sql = sqlStatement.getValue("article_getRepliedArticles");
		return namedParameterJdbcTemplate.queryForList(sql, new HashMap<String, Object>());
	}

	@Override
	public List<Map<String, Object>> getMyArticles(int id) {
		String sql = sqlStatement.getValue("article_getMyArticles");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", id);
		return namedParameterJdbcTemplate.queryForList(sql, map);
	}

	public List<Article> getArticlesForListView() {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Article A WHERE A.status = 0 ORDER BY A.createTime DESC");
		List<Article> list = query.list();
		return list;
	}

	@Override
	public List<Article> getArticlesForTreeView() {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter("statusFilter");
		Query query = session.createQuery("FROM Article A WHERE A.status = 0 AND A.pid IS NULL");
		List<Article> list = query.list();
		session.disableFilter("statusFilter");
		return list;
	}

}
