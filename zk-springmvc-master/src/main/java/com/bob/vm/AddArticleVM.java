package com.bob.vm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
import com.bob.utils.BeanFactory;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AddArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	private Article article;
	private String action;
	private String refreshCommand;
	ScheduledFuture executionOfTask;
	EventQueue<Event> eventQueue;

	private final static String APPLICATION_POSTING_QUEUE = "APPLICATION_POSTING_QUEUE";
	private final static ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(20);

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
			this.article = BeanFactory.getArticleInstance();
			this.refreshCommand = "refreshArticle";
		} else if ("reply".equals(action)) {
			this.article = BeanFactory.getArticleInstance();
			article.setPid(articleId);
			this.refreshCommand = "refreshRepliedArticle";
		} else if ("edit".equals(action)) {
			this.article = forumService.findArticleById(articleId);
			this.refreshCommand = "refreshRepliedArticle";
		}
		eventQueue = EventQueues.lookup(APPLICATION_POSTING_QUEUE, EventQueues.APPLICATION, true);
		eventQueue.subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				// event.getName(): "trigger"
				// refresh
				// call back
				Map<String, Object> args = new HashMap<String, Object>();
				BindUtils.postGlobalCommand(null, null, refreshCommand, args);
				
				// ok
				args.put("memoVisible", false);
				BindUtils.postGlobalCommand(null, null, "updateMemo", args);
				BindUtils.postGlobalCommand(null, null, "refreshArticleDisplay", args);
			}
		});

	}

	@GlobalCommand("cancelArticle")
	public void cancelArticle() {
		executionOfTask.cancel(false);
		
		//ok
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", false);
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
	}

	@Command("addArticle")
	public void doLongOp(@ContextParam(ContextType.VIEW) Window comp) {// TODO
		Runnable task = new Runnable() {
			@Override
			public void run() {
				forumService.addArticle(article);
				eventQueue.publish(new Event("trigger"));
			}
		};
		executionOfTask = SCHEDULED_THREAD_POOL.schedule(task, 3, TimeUnit.SECONDS);

		//ok
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", true);
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args); // show

		// comp.detach();
		comp.setVisible(false);
	}

}
