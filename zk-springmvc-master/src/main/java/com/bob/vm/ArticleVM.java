package com.bob.vm;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.bob.security.ForumUserDetails;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;

	List<Map<String, Object>> latestArticles;
	List<Map<String, Object>> repliedArticles;
	List<Map<String, Object>> myArticles;

	public List<Map<String, Object>> getLatestArticles() {
		return latestArticles;
	}

	public void setLatestArticles(List<Map<String, Object>> latestArticles) {
		this.latestArticles = latestArticles;
	}

	public List<Map<String, Object>> getRepliedArticles() {
		return repliedArticles;
	}

	public void setRepliedArticles(List<Map<String, Object>> repliedArticles) {
		this.repliedArticles = repliedArticles;
	}

	public List<Map<String, Object>> getMyArticles() {
		return myArticles;
	}

	public void setMyArticles(List<Map<String, Object>> myArticles) {
		this.myArticles = myArticles;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());
	}

	@GlobalCommand("refreshArticle")
	@NotifyChange({ "articleList" })
	public void refresh() {
		latestArticles = forumService.getLatestArticles();
	}

}
