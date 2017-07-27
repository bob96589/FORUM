package com.bob.vm;

import java.util.HashMap;
import java.util.List;
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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.bob.model.Article;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;
import com.bob.utils.BeanFactory;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;

	List<Map<String, Object>> latestArticles;
	List<Map<String, Object>> repliedArticles;
	List<Map<String, Object>> myArticles;

	List<Article> allArticlesForListView;
	List<Article> allArticlesForTreeView;

	public List<Article> getAllArticlesForListView() {
		return allArticlesForListView;
	}

	public void setAllArticlesForListView(List<Article> allArticlesForListView) {
		this.allArticlesForListView = allArticlesForListView;
	}

	public List<Article> getAllArticlesForTreeView() {
		return allArticlesForTreeView;
	}

	public void setAllArticlesForTreeView(List<Article> allArticlesForTreeView) {
		this.allArticlesForTreeView = allArticlesForTreeView;
	}

	public List<Map<String, Object>> getLatestArticles() {
		return latestArticles;
	}

	public void setLatestArticles(List<Map<String, Object>> latestArticles) {
		this.latestArticles = latestArticles;
	}

	public List<Map<String, Object>> getRepliedArticles() {
		return repliedArticles;
	}

	public void setRepliedArticles(List<Map<String, Object>> repliedArticles) {
		this.repliedArticles = repliedArticles;
	}

	public List<Map<String, Object>> getMyArticles() {
		return myArticles;
	}

	public void setMyArticles(List<Map<String, Object>> myArticles) {
		this.myArticles = myArticles;
	}

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());

		// allArticlesForListView
		allArticlesForTreeView = forumService.findForArticleTree();

		eventQueue = EventQueues.lookup(APPLICATION_POSTING_QUEUE, EventQueues.APPLICATION, true);
		eventQueue.subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				// event.getName(): "trigger"
				// refresh
				// call back

				// ok
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("memoVisible", false);
				BindUtils.postGlobalCommand(null, null, "updateMemo", args);

				BindUtils.postGlobalCommand(null, null, "refreshArticleDisplay", args);
			}
		});
	}

	@GlobalCommand("refreshArticleDisplay")
	@NotifyChange({ "latestArticles", "repliedArticles", "myArticles", "allArticlesForTreeView" })
	public void refreshArticleDisplay() {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());
		allArticlesForTreeView = forumService.findForArticleTree();
	}

	@Command
	public void openDialog(@BindingParam("article") Article article) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("article", article);
		Executions.createComponents("dialog.zul", null, arg);
	}

	@GlobalCommand("add")
	public void open(@ContextParam(ContextType.VIEW) Component view) {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.action = "add";
		this.article = BeanFactory.getArticleInstance();

		// if ("add".equals(action)) {
		// this.article = BeanFactory.getArticleInstance();
		// } else if ("reply".equals(action)) {
		// this.article = BeanFactory.getArticleInstance();
		// article.setPid(articleId);
		// } else if ("edit".equals(action)) {
		// this.article = forumService.findArticleById(articleId);
		// }

		dialog = Executions.createComponents("addArticle.zul", view.getFirstChild(), arg);
	}

	Component dialog;

	private Article article;
	private String action;
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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@GlobalCommand("cancelArticle")
	public void cancelArticle() {
		executionOfTask.cancel(false);

		// ok
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", false);
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
	}

	@GlobalCommand("addArticle")
	public void doLongOp(@ContextParam(ContextType.VIEW) Component comp) {// TODO
		Runnable task = new Runnable() {
			@Override
			public void run() {
				forumService.addArticle(article);
				eventQueue.publish(new Event("trigger"));
			}
		};
		executionOfTask = SCHEDULED_THREAD_POOL.schedule(task, 3, TimeUnit.SECONDS);

		// ok
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", true);
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args); // show

		dialog.detach();
	}
}
