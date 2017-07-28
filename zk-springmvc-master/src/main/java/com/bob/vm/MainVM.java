package com.bob.vm;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import com.bob.security.SecurityContext;

public class MainVM {

	private String includeSrc = "latestArticles.zul";
	private String account;
	private String displayMode;
	private boolean memoVisible;

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}
	
	public boolean isMemoVisible() {
		return memoVisible;
	}

	public void setMemoVisible(boolean memoVisible) {
		this.memoVisible = memoVisible;
	}

	public Integer getUserId() {
		return SecurityContext.getId();
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

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		account = SecurityContext.getAccount();
		setDisplayMode("articleList.zul");
		memoVisible = false;
	}

	@Command
	@GlobalCommand
	@NotifyChange("includeSrc")
	public void onNavigate(@BindingParam("includeSrc") String includeSrc) {
		this.includeSrc = includeSrc;
	}

	@GlobalCommand
	@NotifyChange({ "memoVisible", "text" })
	public void updateMemo(@BindingParam("memoVisible") boolean memoVisible) {
		this.memoVisible = memoVisible;
	}

}
