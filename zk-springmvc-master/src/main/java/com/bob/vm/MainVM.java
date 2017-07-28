package com.bob.vm;

import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import com.bob.security.SecurityContext;

public class MainVM {

	private String displayTemplateURL;
	private boolean memoVisible;

	public String getDisplayTemplateURL() {
		return displayTemplateURL;
	}

	public void setDisplayTemplateURL(String displayTemplateURL) {
		this.displayTemplateURL = displayTemplateURL;
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

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		displayTemplateURL = "articleList.zul";
		memoVisible = false;
	}

	@GlobalCommand
	@NotifyChange({ "memoVisible" })
	public void showMemo() {
		this.memoVisible = true;
	}
	
	@GlobalCommand
	@NotifyChange({ "memoVisible" })
	public void hideMemo() {
		this.memoVisible = false;
	}

}
