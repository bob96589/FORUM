package com.bob.utils;

import java.util.Date;

import com.bob.model.Article;
import com.bob.security.SecurityContext;

public class BeanFactory {
	public static Article getArticleInstance(){
		Article article = new Article();
		article.setUserId(SecurityContext.getId());
		article.setCreateTime(new Date());
		article.setStatus(0);
		return article;
	}
}
