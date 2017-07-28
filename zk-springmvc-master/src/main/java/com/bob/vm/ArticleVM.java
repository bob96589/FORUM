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
import org.zkoss.zul.ListModelList;

import com.bob.model.Article;
import com.bob.model.Tag;
import com.bob.security.SecurityContext;
import com.bob.service.ForumService;
import com.bob.utils.BeanFactory;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleVM {

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	
	
	
	
	
	public ListModelList<Tag> getContactsModel() {
		return contactsModel;
	}

	public void setContactsModel(ListModelList<Tag> contactsModel) {
		this.contactsModel = contactsModel;
	}

	private ListModelList<Tag> contactsModel;
	@Command("newContact")
	public void newContact(@BindingParam("contact") String tagName) {
		Tag tag = new Tag(tagName);
		contactsModel.add(tag);
		contactsModel.addToSelection(tag);
	}

	private List<Map<String, Object>> latestArticles;
	private List<Map<String, Object>> repliedArticles;
	private List<Map<String, Object>> myArticles;
	private List<Article> allArticlesForListView;
	private List<Article> allArticlesForTreeView;
	private Article selectedArticle;
	private Component dialog;
	private Article article;
	private ScheduledFuture executionOfTask;
	private EventQueue<Event> eventQueue;
	private final static String APPLICATION_POSTING_QUEUE = "APPLICATION_POSTING_QUEUE";
	private final static ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(20);

	public Article getSelectedArticle() {
		return selectedArticle;
	}

	public void setSelectedArticle(Article selectedArticle) {
		this.selectedArticle = selectedArticle;
	}

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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());

		allArticlesForListView = forumService.getAllArticle();
		allArticlesForTreeView = forumService.findForArticleTree();
		contactsModel = new ListModelList<Tag>(forumService.getAllTag());
		contactsModel.setMultiple(true);
		

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
	@NotifyChange({ "latestArticles", "repliedArticles", "myArticles", "allArticlesForListView", "allArticlesForTreeView", "selectedArticle" })
	public void refreshArticleDisplay() {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());

		allArticlesForListView = forumService.getAllArticle();
		allArticlesForTreeView = forumService.findForArticleTree();
		if (selectedArticle != null) {
			selectedArticle = forumService.findArticleById(selectedArticle.getId());
		}
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
		this.article = BeanFactory.getArticleInstance();
		contactsModel.clearSelection();
		dialog = Executions.createComponents("addArticle.zul", view.getFirstChild(), arg);
	}

	@GlobalCommand("cancelArticle")
	public void cancelArticle() {
		executionOfTask.cancel(false);

		// ok
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", false);
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
	}

	@Command("addArticle")
	public void doLongOp(@ContextParam(ContextType.VIEW) Component comp) {// TODO
		Runnable task = new Runnable() {
			@Override
			public void run() {
				forumService.addArticle(article, contactsModel.getSelection());
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

	@Command
	@NotifyChange({ "selectedArticle" })
	public void loadDetail(@BindingParam("selectedArticleId") Integer selectedArticleId) {
		this.selectedArticle = forumService.findArticleById(selectedArticleId);
	}

	@Command
	@NotifyChange({ "selectedArticle" })
	public void delete(@BindingParam("articleId") Integer id) {
		forumService.deleteArticle(id);
		this.selectedArticle = forumService.findArticleById(selectedArticle.getId());
	}

	@Command("reply")
	public void openReplyDialog(@ContextParam(ContextType.VIEW) Component view, @BindingParam("articleId") Integer articleId) {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.article = BeanFactory.getArticleInstance();
		article.setPid(articleId);
		contactsModel.clearSelection();
		dialog = Executions.createComponents("addArticle.zul", view.getFirstChild(), arg);
	}

	@Command("edit")
	@NotifyChange({ "contactsModel" })
	public void openEditDialog(@ContextParam(ContextType.VIEW) Component view, @BindingParam("articleId") Integer articleId) {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.article = forumService.findArticleById(articleId);
		contactsModel.clearSelection();
//		contactsModel.setSelection(article.getTags());
		for(Tag tag : article.getTags()){
			contactsModel.add(tag);
			contactsModel.addToSelection(tag);
		}
		dialog = Executions.createComponents("addArticle.zul", view.getFirstChild(), arg);
	}

}
