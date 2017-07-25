package demo.view.zk;

import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

import com.bob.model.Article;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;

public class AddArticleVM {

	private ForumService forumService;
	private Article article;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		this.article = new Article();
		article.setStatus(0);
		article.setUserId(SecurityContext.getId());
	}

	@Command
	public void addArticle() {
		int i = 0;
		int t = 0;
		int y = 0;
		int u = 0;
		article.setCreateTime(new Date());
		article.setStatus(0);
		article.setUserId(SecurityContext.getId());
		forumService.addArticle(article);
	}
	
	@Command
	public void test() {
		int i = 0;
		int t = 0;
		int y = 0;
		int u = 0;
	}

}
