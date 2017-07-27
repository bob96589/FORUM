package com.bob.vm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.bob.model.Article;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;

	List<Map<String, Object>> latestArticles;
	List<Map<String, Object>> repliedArticles;
	List<Map<String, Object>> myArticles;

	List<Article> allArticlesForListView;
	List<Article> allArticlesForTreeView;

	public List<Article> getAllArticlesForListView() {
		return allArticlesForListView;
	}

	public void setAllArticlesForListView(List<Article> allArticlesForListView) {
		this.allArticlesForListView = allArticlesForListView;
	}

	public List<Article> getAllArticlesForTreeView() {
		return allArticlesForTreeView;
	}

	public void setAllArticlesForTreeView(List<Article> allArticlesForTreeView) {
		this.allArticlesForTreeView = allArticlesForTreeView;
	}

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

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());
		
		//allArticlesForListView
		allArticlesForTreeView = forumService.findForArticleTree();
	}

	@GlobalCommand("refreshArticle")
	@NotifyChange({ "articleList" })
	public void refresh() {
		latestArticles = forumService.getLatestArticles();
	}

	@GlobalCommand("refreshArticleDisplay")
	@NotifyChange({ "latestArticles", "repliedArticles", "myArticles", "allArticlesForTreeView" })
	public void refreshArticleDisplay() {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());
		allArticlesForTreeView = forumService.findForArticleTree();
	}
	
	@Command
	public void openDialog(@BindingParam("article") Article article) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("article", article);
		Executions.createComponents("dialog.zul", null, arg);
	}

}
