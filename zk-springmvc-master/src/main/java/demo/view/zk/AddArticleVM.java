package demo.view.zk;

import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

import com.bob.model.Article;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;

public class AddArticleVM {

	private ForumService forumService;
	private Article article;
	private String action;
	private String refreshCommand;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRefreshCommand() {
		return refreshCommand;
	}

	public void setRefreshCommand(String refreshCommand) {
		this.refreshCommand = refreshCommand;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, @BindingParam("action") String action,
			@BindingParam("articleId") Integer articleId) {
		this.forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		this.action = action;
		if ("add".equals(action)) {
			this.article = new Article();
			this.refreshCommand = "refreshArticle";
		} else if ("reply".equals(action)) {
			this.article = new Article();
			article.setPid(articleId);
			this.refreshCommand = "refreshRepliedArticle";
		}else if ("edit".equals(action)) {
			this.article = forumService.findArticleById(articleId);
			this.refreshCommand = "refreshRepliedArticle";
		}

	}

	@Command
	public void addArticle(@ContextParam(ContextType.VIEW) Window comp) {
		if ("add".equals(action) || "reply".equals(action)) {
			article.setCreateTime(new Date());
			article.setStatus(0);
			article.setUserId(SecurityContext.getId());
			forumService.addArticle(article);
		}else if ("edit".equals(action)) {
			forumService.addArticle(article);
		}
		comp.detach();
	}

	@Command
	public void close(@ContextParam(ContextType.VIEW) Window comp) {
		comp.detach();
	}

}
