package com.bob.vm;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;

import com.bob.security.SecurityContext;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.Clients;

import java.util.concurrent.TimeUnit;

public class MainVM {

	private final static String APPLICATION_MESSAGE_QUEUE = "APPLICATION_MESSAGE_QUEUE";
	private final static String SHOW_MESSAGE = "SHOW_MESSAGE";
	private final static String SHOW_NOTIFICATION = "SHOW_NOTIFICATION";
	private String displayTemplateURL;
	private boolean memoVisible;
	private boolean msgVisible;
	private EventQueue<Event> eventQueue;

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

	public boolean isMsgVisible() { return msgVisible; }

	public void setMsgVisible(boolean msgVisible) { this.msgVisible = msgVisible; }

	public Integer getUserId() {
		return SecurityContext.getId();
	}

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		displayTemplateURL = "articleList.zul";
		memoVisible = false;
		eventQueue = EventQueues.lookup(APPLICATION_MESSAGE_QUEUE, EventQueues.APPLICATION, true);
		eventQueue.subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				String name = event.getName();
				if (SHOW_MESSAGE.equals(name)) {
					BindUtils.postGlobalCommand(null, null, "showMsg", null);
				} else if (SHOW_NOTIFICATION.equals(name)) {
					Clients.evalJavaScript("new Notification('I am Message')");
				}
			}
		});
	}

	@GlobalCommand
	@NotifyChange({"memoVisible"})
	public void showMemo() {
		this.memoVisible = true;
	}

	@GlobalCommand
	@NotifyChange({"memoVisible"})
	public void hideMemo() {
		this.memoVisible = false;
	}

	@GlobalCommand
	@NotifyChange({"msgVisible"})
	public void showMsg() {
		this.msgVisible = true;
	}

	@GlobalCommand
	@NotifyChange({"msgVisible"})
	public void hideMsg() {
		this.msgVisible = false;
	}


	@Command
	public void sendMessage() {
		eventQueue.publish(new Event(SHOW_MESSAGE));
	}

	@Command
	public void sendNotification() {
		eventQueue.publish(new Event(SHOW_NOTIFICATION));
	}

}
