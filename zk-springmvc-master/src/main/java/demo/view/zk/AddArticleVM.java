package demo.view.zk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

import com.bob.model.Article;
import com.bob.service.ForumService;

public class AddArticleVM {

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

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, @BindingParam("action") String action,
			@BindingParam("articleId") Integer articleId) {
		this.forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
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

	@Command("addArticle")
	public void doLongOp(@ContextParam(ContextType.VIEW) Window comp) {
		workingQueueName = "workingQueue" + System.currentTimeMillis();
		eventQueue = EventQueues.lookup(workingQueueName, EventQueues.APPLICATION, true);
		eventQueue.subscribe(listener = new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				System.out.println("update article");
				if ("add".equals(action) || "reply".equals(action)) {
					article.setCreateTime(new Date());
					article.setStatus(0);
					// article.setUserId(SecurityContext.getId()); //TODO
					article.setUserId(1001); // TODO
					forumService.addArticle(article);
				} else if ("edit".equals(action)) {
					forumService.addArticle(article);
				}
			}
		}, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("memoVisible", false);
				args.put("text", result);
				BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, refreshCommand, args);
				BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
				eventQueue.unsubscribe(listener);
				EventQueues.remove(workingQueueName);
			}
		});

		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
		scheduledThreadPool.schedule(new Runnable() {
			@Override
			public void run() {
				eventQueue.publish(new Event("trigger"));
			}
		}, 3, TimeUnit.SECONDS);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", true);
		args.put("text", "Article Sending..");
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);

		// comp.detach();
		comp.setVisible(false);
	}

	@Command
	public void close(@ContextParam(ContextType.VIEW) Window comp) {
		comp.detach();
	}


}
