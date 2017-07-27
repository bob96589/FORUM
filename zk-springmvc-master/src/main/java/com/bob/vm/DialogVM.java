package com.bob.vm;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;

import com.bob.model.Article;

public class DialogVM {

	Article article;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Init
	public void init(@BindingParam("article") Article article) {
		this.article = article;
	}

}
