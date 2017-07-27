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
import com.bob.service.ForumService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AllArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	List<Article> articleList;
	Article selectedArticle;

	public Article getSelectedArticle() {
		return selectedArticle;
	}

	public void setSelectedArticle(Article selectedArticle) {
		this.selectedArticle = selectedArticle;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		articleList = forumService.getAllArticle();
	}

	@GlobalCommand("refreshArticle")
	@NotifyChange({ "articleList" })
	public void refreshArticle() {
		articleList = forumService.getAllArticle();
	}
	
	@Command
	@NotifyChange({ "selectedArticle" })
	public void loadDetail(@BindingParam("selectedArticleId") Integer selectedArticleId) {
		this.selectedArticle = forumService.findArticleById(selectedArticleId);
	}
	
	
	@Command
	@NotifyChange({ "selectedArticle" })
	public void delete(@BindingParam("articleId") Integer id) {
		forumService.deleteArticle(id);
		this.selectedArticle = forumService.findArticleById(selectedArticle.getId());
	}

	@Command("reply")
	public void openReplyDialog(@BindingParam("articleId") Integer articleId) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("articleId", articleId);
		arg.put("action", "reply");
		Executions.createComponents("addArticle.zul", null, arg);
	}

	@Command("edit")
	public void openEditDialog(@BindingParam("articleId") Integer articleId) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("articleId", articleId);
		arg.put("action", "edit");
		Executions.createComponents("addArticle.zul", null, arg);
	}

	@GlobalCommand("refreshRepliedArticle")
	@NotifyChange({ "selectedArticle" })
	public void refresh() {
		this.selectedArticle = forumService.findArticleById(selectedArticle.getId());
	}
	
	
}
