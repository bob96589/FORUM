package com.bob.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import com.bob.security.SecurityContext;

@Entity
@Table(name = "ARTICLE")
@FilterDef(name = "test", defaultCondition = "STATUS = 0")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "PID")
	private Integer pid;

	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENT")
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "STATUS")
	private Integer status;

	@ManyToMany
	@JoinTable(name = "TAGDETAIL", joinColumns = {
			@JoinColumn(name = "ARTICLE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "TAG_ID", nullable = false, updatable = false) })
	private Set<Tag> tags;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PID", insertable = false, updatable = false)
	private Article parent;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	@OrderBy("createTime")	
	@Filter(name="test", condition="STATUS = 0")
	private Set<Article> children = new HashSet<Article>();

	public Article getParent() {
		return parent;
	}

	public void setParent(Article parent) {
		this.parent = parent;
	}

	public Set<Article> getChildren() {
		return children;
	}

	public void setChildren(Set<Article> children) {
		this.children = children;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", pid=" + pid + ", userId=" + userId + ", title=" + title + ", content=" + content
				+ ", createTime=" + createTime + ", status=" + status + "]";
	}

}
