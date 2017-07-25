package demo.view.zk;

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
		articleList = forumService.findForArticleTree();
	}

	@GlobalCommand
	@NotifyChange({ "articleList" })
	public void loadDetail(@BindingParam("articleId") Integer id) {
//		Article article = forumService.findArticleById(id);
//		articleList.clear();
//		articleList.add(article);
	}

	@Command
	@NotifyChange({ "articleList" })
	public void delete(@BindingParam("articleId") Integer id) {
		forumService.deleteArticle(id);
		Article article = forumService.findArticleById(this.articleId);
		articleList.clear();
		if (article != null) {
			articleList.add(article);
		}
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
	@NotifyChange({ "articleList" })
	public void refresh() {
		Article article = forumService.findArticleById(this.articleId);
		articleList.clear();
		articleList.add(article);
	}


}
