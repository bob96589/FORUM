package com.bob.vm;

import java.util.HashMap;
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

import com.bob.security.SecurityContext;

public class MainVM {

	private String includeSrc = "latestArticles.zul";
	private String account;
	private String text;
	private boolean memoVisible;

	public boolean isMemoVisible() {
		return memoVisible;
	}

	public void setMemoVisible(boolean memoVisible) {
		this.memoVisible = memoVisible;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		account = SecurityContext.getAccount();
		memoVisible = false;
	}

	@Command
	@GlobalCommand
	@NotifyChange("includeSrc")
	public void onNavigate(@BindingParam("includeSrc") String includeSrc) {
		this.includeSrc = includeSrc;
	}

	@Command("add")
	public void open(@ContextParam(ContextType.VIEW) Component view) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("action", "add");
		Executions.createComponents("addArticle.zul", view, arg);
	}

	@GlobalCommand
	@NotifyChange({ "memoVisible", "text" })
	public void updateMemo(@BindingParam("memoVisible") boolean memoVisible, @BindingParam("text") String text) {
		this.memoVisible = memoVisible;
		this.text = text;
	}

}
