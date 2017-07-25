package demo.view.zk;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

public class MainVM {

	String includeSrc = "latestArticles.zul";

	public String getIncludeSrc() {
		return includeSrc;
	}

	public void setIncludeSrc(String includeSrc) {
		this.includeSrc = includeSrc;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		// articleList = forumService.getNewArticles();
	}

	@Command
	@NotifyChange("includeSrc")
	public void onNavigate(@BindingParam("includeSrc") String includeSrc) {
		this.includeSrc = includeSrc;
	}

}
