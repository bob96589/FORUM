package demo.view.zk;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zkplus.spring.SpringUtil;

import com.bob.service.ForumService;

public class LatestArticlesVM {

	private ForumService forumService;

	List<Map<String, Object>> articleList;

	public List<Map<String, Object>> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Map<String, Object>> articleList) {
		this.articleList = articleList;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		articleList = forumService.getNewArticles();
	}

}
