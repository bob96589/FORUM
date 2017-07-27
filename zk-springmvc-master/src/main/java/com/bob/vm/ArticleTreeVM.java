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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.bob.model.Article;
import com.bob.service.ForumService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleTreeVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	private List<Article> articleList = new ArrayList<Article>();

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		articleList = forumService.findForArticleTree();
	}

	@Command
	public void openDialog(@BindingParam("article") Article article) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("article", article);
		Executions.createComponents("dialog.zul", null, arg);
	}

}
