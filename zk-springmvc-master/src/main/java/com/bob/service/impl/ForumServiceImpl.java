package com.bob.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bob.dao.ArticleDao;
import com.bob.dao.TagDao;
import com.bob.dao.UserDao;
import com.bob.model.Article;
import com.bob.model.Tag;
import com.bob.model.User;
import com.bob.service.ForumService;

@Service
public class ForumServiceImpl implements ForumService {

	@Autowired
	UserDao userDao;
	@Autowired
	ArticleDao articleDao;
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
	public List<Article> getArticlesForListView() {
		return articleDao.getArticlesForListView();
	}

	@Override
	public List<Article> getArticlesForTreeView() {
		return articleDao.getArticlesForTreeView();
	}

	@Override
	public Article findArticleById(int articleId) {
		return articleDao.findById(articleId);
	}

	@Override
	public void saveOrUpdateArticle(Article article, Set<Tag> tags) {
		for (Tag tag : tags) {
			tagDao.saveOrUpdate(tag);
		}
		article.setTags(tags);
		articleDao.saveOrUpdate(article);
	}

	@Override
	public void deleteArticle(Integer articleId) {
		Article article = articleDao.findById(articleId);
		Queue<Article> list = new LinkedList<Article>();
		list.offer(article);
		while (!list.isEmpty()) {
			Article temp = list.poll();
			temp.setStatus(1);
			articleDao.saveOrUpdate(temp);
			list.addAll(temp.getChildren());
		}
	}

	@Override
	public User findUserByAccount(String username) {
		return userDao.findByAccount(username);
	}

	@Override
	public List<Tag> getAllTag() {
		return tagDao.getAll();
	}

}
