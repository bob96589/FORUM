package com.bob.servlet;

import com.bob.webpush.Subscription;
import com.bob.webpush.SubscriptionService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TestServlet extends HttpServlet {

	private SubscriptionService service = SubscriptionService.getInstance();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JsonObject jsonObject = (new JsonParser()).parse(test).getAsJsonObject();
		String endpoint = jsonObject.get("endpoint").getAsString();
		String key = jsonObject.get("key").getAsString();
		String auth = jsonObject.get("auth").getAsString();
		String action = jsonObject.get("action") != null ? jsonObject.get("action").getAsString() : "";

		Subscription sub = new Subscription();
		sub.setAuth(auth);
		sub.setEndpoint(endpoint);
		sub.setKey(key);
		if ("delete".equals(action)) {
			service.delete(sub);
		} else {
			service.save(sub);
		}
		System.out.println("list size: " + service.findAll().size());
	}

}
