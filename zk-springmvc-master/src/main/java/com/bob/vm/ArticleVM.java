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

	private final static String APPLICATION_POSTING_QUEUE = "APPLICATION_POSTING_QUEUE";
	private final static String REFRESH_ARTICLE_DISPLAY = "REFRESH_ARTICLE_DISPLAY";
	private final static ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(20);

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	private Component editDialog;
	private Article articleInEditDialog;
	private EventQueue<Event> eventQueue;
	private ScheduledFuture executionOfTask;
	
	private List<Map<String, Object>> latestArticles;
	private List<Map<String, Object>> repliedArticles;
	private List<Map<String, Object>> myArticles;
	private List<Article> allArticlesForListView;
	private List<Article> allArticlesForTreeView;
	private Article selectedArticleInListView;
	private ListModelList<Tag> tagsModel;

	public Article getArticleInEditDialog() {
		return articleInEditDialog;
	}

	public void setArticleInEditDialog(Article articleInEditDialog) {
		this.articleInEditDialog = articleInEditDialog;
	}

	public Article getSelectedArticleInListView() {
		return selectedArticleInListView;
	}

	public void setSelectedArticleInListView(Article selectedArticleInListView) {
		this.selectedArticleInListView = selectedArticleInListView;
	}

	public ListModelList<Tag> getTagsModel() {
		return tagsModel;
	}

	public void setTagsModel(ListModelList<Tag> tagsModel) {
		this.tagsModel = tagsModel;
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

	@Init
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {

		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());
		allArticlesForListView = forumService.getArticlesForListView();
		allArticlesForTreeView = forumService.getArticlesForTreeView();

		tagsModel = new ListModelList<Tag>(forumService.getAllTag());
		tagsModel.setMultiple(true);

		eventQueue = EventQueues.lookup(APPLICATION_POSTING_QUEUE, EventQueues.APPLICATION, true);
		eventQueue.subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (REFRESH_ARTICLE_DISPLAY.equals(event.getName())) {
					// event.getName(): "trigger", call back, refresh
					BindUtils.postGlobalCommand(null, null, "refreshArticleDisplay", null);
					BindUtils.postGlobalCommand(null, null, "hideMemo", null);// TODO
				}
			}
		});
	}

	@GlobalCommand
	@NotifyChange({ "latestArticles", "repliedArticles", "myArticles", "allArticlesForListView", "allArticlesForTreeView", "selectedArticleInListView" })
	public void refreshArticleDisplay() {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());

		allArticlesForListView = forumService.getArticlesForListView();
		allArticlesForTreeView = forumService.getArticlesForTreeView();
		if (selectedArticleInListView != null) {
			selectedArticleInListView = forumService.findArticleById(selectedArticleInListView.getId());
		}
	}

	@Command
	public void saveOrUpdateArticle(@ContextParam(ContextType.VIEW) Component comp) {// TODO
		Runnable task = new Runnable() {
			@Override
			public void run() {
				forumService.saveOrUpdateArticle(articleInEditDialog, tagsModel.getSelection());
				eventQueue.publish(new Event(REFRESH_ARTICLE_DISPLAY));
			}
		};
		executionOfTask = SCHEDULED_THREAD_POOL.schedule(task, 3, TimeUnit.SECONDS);
		BindUtils.postGlobalCommand(null, null, "showMemo", null);
		editDialog.detach();
	}

	@Command
	@NotifyChange({ "selectedArticleInListView" })
	public void loadDetail(@BindingParam("selectedArticleId") Integer selectedArticleId) {
		this.selectedArticleInListView = forumService.findArticleById(selectedArticleId);
	}

	@GlobalCommand
	public void openDialogForAdd(@ContextParam(ContextType.VIEW) Component view) {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.articleInEditDialog = BeanFactory.getArticleInstance();
		tagsModel.clearSelection();
		editDialog = Executions.createComponents("editDialog.zul", view.getFirstChild(), arg);
	}

	@Command
	public void openDialogForReply(@ContextParam(ContextType.VIEW) Component view, @BindingParam("articleId") Integer articleId) {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.articleInEditDialog = BeanFactory.getArticleInstance();
		articleInEditDialog.setPid(articleId);
		tagsModel.clearSelection();
		editDialog = Executions.createComponents("editDialog.zul", view.getFirstChild(), arg);
	}

	@Command
	@NotifyChange({ "contactsModel" })
	public void openDialogForEdit(@ContextParam(ContextType.VIEW) Component view, @BindingParam("articleId") Integer articleId) {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.articleInEditDialog = forumService.findArticleById(articleId);
		tagsModel.clearSelection();
		// tagsModel.setSelection(article.getTags());
		for (Tag tag : articleInEditDialog.getTags()) {
			tagsModel.add(tag);
			tagsModel.addToSelection(tag);
		}
		editDialog = Executions.createComponents("editDialog.zul", view.getFirstChild(), arg);
	}
	
	@Command
	@NotifyChange({ "selectedArticleInListView" })
	public void delete(@BindingParam("articleId") Integer id) {
		forumService.deleteArticle(id);
		this.selectedArticleInListView = forumService.findArticleById(selectedArticleInListView.getId());
	}

	@GlobalCommand("cancelArticle")
	public void cancelArticle() {
		executionOfTask.cancel(false);
		BindUtils.postGlobalCommand(null, null, "hideMemo", null);
	}

	@Command
	public void openDialog(@BindingParam("article") Article article) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("article", article);
		Executions.createComponents("displayDialog.zul", null, arg);
	}

	@Command
	public void newTag(@BindingParam("tagName") String tagName) {
		Tag tag = new Tag(tagName);
		tagsModel.add(tag);
		tagsModel.addToSelection(tag);
	}

}
