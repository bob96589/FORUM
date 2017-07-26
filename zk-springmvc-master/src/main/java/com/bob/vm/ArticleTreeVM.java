package com.bob.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zkplus.spring.SpringUtil;

import com.bob.model.Article;
import com.bob.service.ForumService;

public class ArticleTreeVM {

	private List<Article> articleList = new ArrayList<Article>();
	private ForumService forumService;

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		articleList = forumService.findForArticleTree();
	}
	
	@Command
	public void openDialog(@BindingParam("article") Article article) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("article", article);
		Executions.createComponents("dialog.zul", null, arg);
	}

}
