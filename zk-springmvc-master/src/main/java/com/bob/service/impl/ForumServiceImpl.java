package com.bob.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bob.dao.ArticleDao;
import com.bob.dao.TagDao;
import com.bob.dao.UserDao;
import com.bob.model.Article;
import com.bob.model.Tag;
import com.bob.service.ForumService;

@Service
public class ForumServiceImpl implements ForumService {

	@Autowired
	UserDao userDao;

	@Autowired
	ArticleDao articleDao;

	@Autowired
	TagDao tagDao;

	public Set<Tag> doSomething() {
//		List<Article> list = articleDao.list(0, 4);
//		Article a = list.get(0);
//		Set<Tag> tags = a.getTags();
		
		Article a = new Article();
		a.setContent("aa");
		a.setStatus(0);
		a.setTitle("title");
		a.setUserId(44);
		a.setCreateTime(new Date());
		
		Set<Tag> set = new HashSet<Tag>();
		for(int i = 0; i < 5; i++) {
			Tag t1 = new Tag();
			t1.setName("tag" + i);
			tagDao.saveOrUpdate(t1);
			set.add(t1);
		}
		a.setTags(set);
		articleDao.saveOrUpdate(a);
		return null;
//		Article a = list.get(0);
//		return a.getTags();
	}

	@Override
	public List<Article> getAllArticle() {
		
//		List<Article> list = articleDao.list();
//		Article a = list.get(0);
//		Set<Article> set = a.getChildren();
//		System.out.println(set);
		
		return articleDao.list();
	}

	@Override
	public List<Article> getArticleForDetail() {
		// TODO Auto-generated method stub
		return articleDao.findForDetail();
	}

	@Override
	public List<Map<String, Object>> getNewArticles() {
		return articleDao.getNewArticles();
	}

	@Override
	public Article findArticleById(int articleId) {
		return articleDao.findById(articleId);
	}

	@Override
	public List<Map<String, Object>> getRepliedArticles() {
		return articleDao.getRepliedArticles();
	}

}
