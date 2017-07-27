package com.bob.vm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.bob.model.Article;
import com.bob.service.ForumService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AddArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	private Article article;
	private String action;
	private String refreshCommand;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRefreshCommand() {
		return refreshCommand;
	}

	public void setRefreshCommand(String refreshCommand) {
		this.refreshCommand = refreshCommand;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, @BindingParam("action") String action,
			@BindingParam("articleId") Integer articleId) {
		this.action = action;
		if ("add".equals(action)) {
			this.article = new Article();
			this.refreshCommand = "refreshArticle";
		} else if ("reply".equals(action)) {
			this.article = new Article();
			article.setPid(articleId);
			this.refreshCommand = "refreshRepliedArticle";
		} else if ("edit".equals(action)) {
			this.article = forumService.findArticleById(articleId);
			this.refreshCommand = "refreshRepliedArticle";
		}
		// eventQueue, don't unsciubscribe

	}

	String result = "";
	EventQueue<Event> eventQueue;
	EventListener<Event> listener;
	String workingQueueName;

	@GlobalCommand("cancelArticle")
	public void cancelArticle() {
		eventQueue = EventQueues.lookup(workingQueueName, EventQueues.APPLICATION, true);
		eventQueue.unsubscribe(listener);
		EventQueues.remove(workingQueueName);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", false);
		args.put("text", "cancel..");
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
	}
	
	//

	@Command("addArticle")
	public void doLongOp(@ContextParam(ContextType.VIEW) Window comp) {//TODO
		workingQueueName = "APPLICATION_POSTING_QUEUE"; //TODO static final
		eventQueue = EventQueues.lookup(workingQueueName, EventQueues.APPLICATION, true);
		// init
		eventQueue.subscribe(listener = new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				//event.getName(): "trigger"
				//refresh
				//call back
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("memoVisible", false);
				args.put("text", result);
				BindUtils.postGlobalCommand(null, null, refreshCommand, args);
				BindUtils.postGlobalCommand(null, null, "updateMemo", args);
				//
				eventQueue.unsubscribe(listener);
				EventQueues.remove(workingQueueName);
			}
		});

		// final object
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(20); //TODO
		scheduledThreadPool.schedule(new Runnable() {
			@Override
			public void run() {
				
				System.out.println("update article");
				if ("add".equals(action) || "reply".equals(action)) {
					// article constructor
					article.setCreateTime(new Date());
					article.setStatus(0);
					// article.setUserId(SecurityContext.getId()); //TODO
					article.setUserId(1001); // TODO
					forumService.addArticle(article);
				} else if ("edit".equals(action)) {
					forumService.addArticle(article);
				}
				eventQueue.publish(new Event("trigger"));
			}
		}, 3, TimeUnit.SECONDS);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", true);
		args.put("text", "Article Sending..");
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args); //show

		// comp.detach();
		comp.setVisible(false);
	}

}
