package com.bob.vm;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
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
import com.bob.validator.EmptyCKEditorValidator;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ArticleVM {

	private final static String APPLICATION_POSTING_QUEUE = "APPLICATION_POSTING_QUEUE";
	private final static String REFRESH_ARTICLE_DISPLAY = "REFRESH_ARTICLE_DISPLAY";
	private final static ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(20);
	private final static Validator EMPTY_CKEDITOR_VALIDATOR = new EmptyCKEditorValidator();

	@WireVariable("forumServiceImpl")
	private ForumService forumService;
	private Component editDialog;
	private Article articleInEditDialog;
	private String actionInEditDialog;
	private ListModelList<Tag> tagsModel;
	private EventQueue<Event> eventQueue;
	private ScheduledFuture executionOfTask;
	private List<Map<String, Object>> latestArticles;
	private List<Map<String, Object>> repliedArticles;
	private List<Map<String, Object>> myArticles;
	private List<Article> allArticlesForListView;
	private List<Article> allArticlesForTreeView;
	private Article selectedArticleInListView;
	private final Logger logger = LoggerFactory.getLogger(ArticleVM.class);

	public Validator getEmptyCKEditorValidator() {
		return EMPTY_CKEDITOR_VALIDATOR;
	}

	public String getActionInEditDialog() {
		return actionInEditDialog;
	}

	public void setActionInEditDialog(String actionInEditDialog) {
		this.actionInEditDialog = actionInEditDialog;
	}

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
	public void initSetup() {
		latestArticles = forumService.getLatestArticles();
		repliedArticles = forumService.getRepliedArticles();
		myArticles = forumService.getMyArticles(SecurityContext.getId());
		allArticlesForListView = forumService.getArticlesForListView();
		allArticlesForTreeView = forumService.getArticlesForTreeView();

		eventQueue = EventQueues.lookup(APPLICATION_POSTING_QUEUE, EventQueues.APPLICATION, true);
		eventQueue.subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (REFRESH_ARTICLE_DISPLAY.equals(event.getName())) {
					BindUtils.postGlobalCommand(null, null, "refreshArticleDisplay", null);
					logger.debug("Trigger global command: refreshArticleDisplay.");
				}
			}
		});
		logger.info("Subscribe event from {}.", APPLICATION_POSTING_QUEUE);
	}

	@GlobalCommand
	@NotifyChange({ "latestArticles", "repliedArticles", "myArticles", "allArticlesForListView",
			"allArticlesForTreeView", "selectedArticleInListView" })
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
	public void saveOrUpdateArticle() {
		logger.info("Submit article form.");
		editDialog.detach();
		Desktop desktop = Executions.getCurrent().getDesktop();
		if ("add".equals(actionInEditDialog)) {
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try {
						Executions.activate(desktop);
						BindUtils.postGlobalCommand(null, null, "hideMemo", null);
						logger.debug("Trigger global command: hideMemo.");
					} catch (DesktopUnavailableException | InterruptedException e) {
						e.printStackTrace();
					} finally {
						Executions.deactivate(desktop);
					}
					forumService.saveOrUpdateArticle(articleInEditDialog, tagsModel.getSelection());
					logger.info("Processing article(action: {}) succeeds.", actionInEditDialog);
					eventQueue.publish(new Event(REFRESH_ARTICLE_DISPLAY));
					logger.info("Publish event to {}.", APPLICATION_POSTING_QUEUE);
				}
			};
			executionOfTask = SCHEDULED_THREAD_POOL.schedule(task, 3, TimeUnit.SECONDS);
			BindUtils.postGlobalCommand(null, null, "showMemo", null);
		} else {
			forumService.saveOrUpdateArticle(articleInEditDialog, tagsModel.getSelection());
			logger.info("Processing article(action: {}) succeeds.", actionInEditDialog);
			BindUtils.postGlobalCommand(null, null, "refreshArticleDisplay", null);
		}
	}

	@Command
	@NotifyChange({ "selectedArticleInListView" })
	public void loadDetail(@BindingParam("selectedArticleId") Integer selectedArticleId) {
		this.selectedArticleInListView = forumService.findArticleById(selectedArticleId);
	}

	@GlobalCommand
	public void openDialogForAdd(@ContextParam(ContextType.VIEW) Component view) {
		this.actionInEditDialog = "add";
		this.articleInEditDialog = BeanFactory.createArticle();
		tagsModel = createTagsModel();
		editDialog = Executions.createComponents("editDialog.zul", view.getFirstChild(), null);
	}

	@Command
	public void openDialogForReply(@ContextParam(ContextType.VIEW) Component view,
			@BindingParam("articleId") Integer articleId) {
		this.actionInEditDialog = "reply";
		this.articleInEditDialog = BeanFactory.createArticle();
		articleInEditDialog.setPid(articleId);
		tagsModel = createTagsModel();
		editDialog = Executions.createComponents("editDialog.zul", view.getFirstChild(), null);
	}

	@Command
	@NotifyChange({ "contactsModel" })
	public void openDialogForEdit(@ContextParam(ContextType.VIEW) Component view,
			@BindingParam("articleId") Integer articleId) {
		this.actionInEditDialog = "edit";
		this.articleInEditDialog = forumService.findArticleById(articleId);
		tagsModel = createTagsModel();
		tagsModel.setSelection(articleInEditDialog.getTags());
		editDialog = Executions.createComponents("editDialog.zul", view.getFirstChild(), null);
	}

	@Command
	@NotifyChange({ "selectedArticleInListView" })
	public void delete(@BindingParam("articleId") Integer id) {
		forumService.deleteArticle(id);
		logger.info("Article was deleted.");
		this.selectedArticleInListView = forumService.findArticleById(selectedArticleInListView.getId());
	}

	@GlobalCommand
	public void cancelArticle() {
		if (executionOfTask.cancel(false)) {
			BindUtils.postGlobalCommand(null, null, "hideMemo", null);
			logger.info("Article was cancelled.");
		}
	}

	@Command
	public void openDialog(@BindingParam("article") Article article) {
		Executions.createComponents("displayDialog.zul", null, Collections.singletonMap("article", article));
	}

	@Command
	public void newTag(@BindingParam("tagName") String tagName) {
		Tag tag = new Tag(tagName);
		tagsModel.add(tag);
		tagsModel.addToSelection(tag);
	}

	private ListModelList<Tag> createTagsModel() {
		ListModelList<Tag> tagsModel = new ListModelList<Tag>(forumService.getAllTag());
		tagsModel.setMultiple(true);
		return tagsModel;
	}

}
