package com.bob.composer;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.East;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.North;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.South;
import org.zkoss.zul.West;

import com.bob.model.Article;
import com.bob.service.ForumService;

public class ArticleDetailComposer extends GenericForwardComposer {

	private Grid detailGrid;

	@WireVariable
	private ForumService forumService;
	List<Article> articleList;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		articleList = forumService.getArticleForDetail();

		ListModelList detailModel = new ListModelList(articleList);

		detailGrid.setModel(detailModel);
		detailGrid.setRowRenderer(new ArticleRowRenderer());
	}

	private class ArticleRowRenderer implements RowRenderer {

		/* (non-Javadoc)
		 * @see org.zkoss.zul.RowRenderer#render(org.zkoss.zul.Row, java.lang.Object, int)
		 */
		@Override
		public void render(Row row, Object data, int index) throws Exception {
			
			Grid mainGrid = new Grid();
			mainGrid.setParent(row);
			
			Rows rows = new Rows();
			rows.setParent(mainGrid);
			
			Row row1 = new Row();
			row1.setParent(rows);
			Row row2 = new Row();
			row2.setParent(rows);
			Row row3 = new Row();
			row3.setParent(rows);
			Row row4 = new Row();
			row4.setParent(rows);
			Row row5 = new Row();
			row5.setParent(rows);
			Row row6 = new Row();
			row6.setParent(rows);
			
			final Article article = (Article) data;
			new Label(Integer.toString(article.getId())).setParent(row1);
			new Label(article.getPid() != null ? Integer.toString(article.getPid()) : "").setParent(row2);
			new Label(article.getTitle()).setParent(row3);
			new Label(article.getContent()).setParent(row4);
			new Label(article.getCreateTime().toString()).setParent(row5);

			Set<Article> children = article.getChildren();
			if (!CollectionUtils.isEmpty(children)) {
				Grid childGrid = new Grid();
				ListModelList childrenModel = new ListModelList(children);
				childGrid.setModel(childrenModel);
				childGrid.setRowRenderer(new ArticleRowRenderer());
				row6.appendChild(childGrid);
			}

		}
	}
	
	
	
	private class TableLayoutRowRenderer implements RowRenderer {

		/* (non-Javadoc)
		 * @see org.zkoss.zul.RowRenderer#render(org.zkoss.zul.Row, java.lang.Object, int)
		 */
		@Override
		public void render(Row row, Object data, int index) throws Exception {
			
			Borderlayout borderlayout = new Borderlayout();
			borderlayout.setHeight("500px");
			borderlayout.setParent(row);
			
			
			North north = new North();
			West west = new West();
			East east = new East();
			Center center = new Center();
			South south = new South();
			north.setParent(borderlayout);
			west.setParent(borderlayout);
			east.setParent(borderlayout);
			center.setParent(borderlayout);
			south.setParent(borderlayout);
			
			north.setHeight("100px");
			west.setWidth("100px");
			east.setWidth("100px");
			south.setHeight("200px");
			
			final Article article = (Article) data;
			new Label(Integer.toString(article.getId())).setParent(north);
//			new Label(article.getPid() != null ? Integer.toString(article.getPid()) : "").setParent(west);
			new Label(article.getTitle()).setParent(west);
			new Label(article.getContent()).setParent(center);
			new Label(article.getCreateTime().toString()).setParent(east);

			Set<Article> children = article.getChildren();
			if (!CollectionUtils.isEmpty(children)) {
				Grid childGrid = new Grid();
				childGrid.setHeight("200px");
				ListModelList childrenModel = new ListModelList(children);
				childGrid.setModel(childrenModel);
				childGrid.setRowRenderer(new TableLayoutRowRenderer());
				south.appendChild(childGrid);
			}

		}
	}

}
