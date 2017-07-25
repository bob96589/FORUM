package demo.controller.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bob.service.ForumService;

import demo.data.bean.Order;
import demo.data.service.OrderDAO;

@Controller
@RequestMapping("/forum")
public class FormController {

	@Autowired
	private OrderDAO orderDao;
	@Autowired
	private ForumService forumService;
	public static Logger logger = Logger.getLogger(FormController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginForm(ModelMap model, HttpServletRequest request, HttpSession session) {
		//Set<Tag> tags = forumService.doSomething();
//		List<Article> articleList = forumService.getAllArticle();
//		Article a = articleList.get(0);
//		Set<Article> set = a.getChildren();
//		System.out.println(set);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		logger.debug("aaa");
		
		if ("zk".equals(username) && "zk".equals(password)) {
			session.setAttribute("logged", true);
			return "redirect:main";
		}
		return "redirect:index";
	}
	
	@RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
	public String confirmForm(@ModelAttribute Order order, HttpSession session, ModelMap model) {
		if (session.getAttribute("logged") != null) {
			int id = orderDao.findMaxId();
			order.setId(id);
			orderDao.saveOrder(order);
			return "redirect:confirm/" + id;
		}
		return "redirect:index";
	}
}
