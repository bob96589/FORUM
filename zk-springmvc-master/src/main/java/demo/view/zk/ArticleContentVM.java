package demo.view.zk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.spring.SpringUtil;

import com.bob.model.Article;
import com.bob.service.ForumService;

public class ArticleContentVM {

	private List<Article> articleList = new ArrayList<Article>();
	private ForumService forumService;

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	@GlobalCommand
	@NotifyChange({"articleList"})
	public void loadDetail(@BindingParam("articleId") int articleId){
		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		Article article = forumService.findArticleById(articleId);
		articleList.clear();
		articleList.add(article);
	}
	

}
