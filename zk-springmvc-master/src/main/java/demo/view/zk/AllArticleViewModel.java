package demo.view.zk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Center;

import com.bob.model.Article;
import com.bob.service.ForumService;

import demo.data.bean.CartItem;
import demo.data.bean.Product;

public class AllArticleViewModel {
	
	@WireVariable
	private ForumService forumService;

	private List<CartItem> cartList = new ArrayList<CartItem>();
	
	List<Article> articleList;
	
	public List<CartItem> getCartList() {
		return cartList;
	}
	
	public int getTotal() {
		int total = 0;
		for (CartItem item : cartList) {
			total += item.getSubtotal();
		}
		return total;
	}
	
	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("centerArea") Center centerArea) {
		Selectors.wireComponents(view, this, false);
		forumService = (ForumService) SpringUtil.getBean("forumServiceImpl");
		articleList = forumService.getAllArticle();
	}
	
	@GlobalCommand @NotifyChange({"cartList", "total"})
	public void addCart(@BindingParam("product") Product prod) {
		int quantity = prod.getQuantity();
		prod.setQuantity(0);
		if (quantity <= 0)
			return;
		boolean isAddNew = true;
		if (!cartList.isEmpty()) {
			for (CartItem item : cartList) {
				if (item.getProduct().getId() == prod.getId()) {
					isAddNew = false;
					item.add(quantity);
					break;
				}
			}
		}
		if (isAddNew) {
			CartItem item = new CartItem(prod);
			item.add(quantity);
			cartList.add(item);
		}
	}
}
