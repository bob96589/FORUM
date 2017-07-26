package com.bob.vm;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zkplus.spring.SpringUtil;

import com.bob.security.ForumUserDetails;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;

public class MyArticlesVM {

	private ForumService forumService;

	List<Map<String, Object>> articleList;

	public List<Map<String, Object>> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Map<String, Object>> articleList) {
		this.articleList = articleList;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		articleList = forumService.getMyArticles(SecurityContext.getId());
	}

}
