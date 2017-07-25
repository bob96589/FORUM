package demo.view.zk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zkplus.spring.SpringUtil;

import com.bob.model.Article;
import com.bob.service.ForumService;

public class ArticleContentVM {

	private List<Article> articleList = new ArrayList<Article>();
	private Integer articleId;
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
	}

	@GlobalCommand
	@NotifyChange({"articleList"})
	public void loadDetail(@BindingParam("articleId") Integer id){
		this.articleId = id;
		Article article = forumService.findArticleById(id);
		articleList.clear();
		articleList.add(article);
	}
	
	
	@Command
	@NotifyChange({"articleList"})
	public void delete(@BindingParam("articleId") Integer id){
		forumService.deleteArticle(id);
		Article article = forumService.findArticleById(this.articleId);
		articleList.clear();
		articleList.add(article);
	}
	

}
