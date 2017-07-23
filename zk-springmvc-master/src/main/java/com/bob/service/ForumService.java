package com.bob.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bob.model.Article;
import com.bob.model.Tag;

public interface ForumService {

	Set<Tag> doSomething();

	List<Article> getAllArticle();

	List<Article> getArticleForDetail();

	List<Map<String, Object>> getNewArticles();

}
