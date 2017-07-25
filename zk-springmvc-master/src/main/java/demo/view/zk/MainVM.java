package demo.view.zk;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

import com.bob.security.SecurityContext;

public class MainVM {

	private String includeSrc = "latestArticles.zul";
	private Integer userId;
	private String account;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIncludeSrc() {
		return includeSrc;
	}

	public void setIncludeSrc(String includeSrc) {
		this.includeSrc = includeSrc;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		userId = SecurityContext.getId();
		account = SecurityContext.getAccount();
		// forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		// articleList = forumService.getNewArticles();
	}

	@Command
	@GlobalCommand
	@NotifyChange("includeSrc")
	public void onNavigate(@BindingParam("includeSrc") String includeSrc) {
		this.includeSrc = includeSrc;
	}

}
