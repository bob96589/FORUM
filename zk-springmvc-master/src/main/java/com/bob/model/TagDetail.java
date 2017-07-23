package com.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "TAGDETAIL")
public class TagDetail {
	
	//@Column(name = "ARTICLE_ID")
	private Integer articleId;

	//@Column(name = "TAG_ID")
	private Integer tagId;

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

}
