package com.bob.servlet;

import com.bob.webpush.Subscription;
import com.bob.webpush.SubscriptionService;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.jose4j.lang.JoseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PushServlet extends HttpServlet {

	private SubscriptionService service = SubscriptionService.getInstance();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Subscription> subs = service.findAll();
		System.out.println("push size: " + subs.size());
		Date now = new Date();
		for (Subscription sub : subs) {
			try {
				System.out.print("endpoint: " + sub.getEndpoint());
				byte[] payload = now.toString().getBytes();
				sendPushMessage(sub, payload);
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JoseException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * The Time to live of GCM notifications
	 */
	private static final int TTL = 24 * 60 * 60;

	public void sendPushMessage(Subscription sub, byte[] payload) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

		// Figure out if we should use GCM for this notification somehow
		boolean useGcm = shouldUseGcm(sub);
		Notification notification;
		PushService pushService;

		if (useGcm) {
			// Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
			notification = new Notification(
					sub.getEndpoint(),
					sub.getUserPublicKey(),
					sub.getAuthAsBytes(),
					payload
			);

			// Instantiate the push service, no need to use an API key for Push API
			pushService = new PushService();
		} else {
			// Or create a GcmNotification, in case of Google Cloud Messaging
			notification = new Notification(
					sub.getEndpoint(),
					sub.getUserPublicKey(),
					sub.getAuthAsBytes(),
					payload,
					TTL
			);

			// Instantiate the push service with a GCM API key
			pushService = new PushService("gcm-api-key");
		}

		// Send the notification
		pushService.setPublicKey(Utils.loadPublicKey("BKPaf4qsjD_NtdmcTh6tB39UaEe41QCALL-lP0FyH7oiFm9cVUYnwIjyzPUWdhr1XXXPRPIh1A7kbbXfva4ViPw"));
		pushService.setPrivateKey(Utils.loadPrivateKey("cYGuJEo3AiE5ORH94J1tIoTDxp1Oqz1qZNHi0X1wnHc"));
		pushService.send(notification);
	}

	private boolean shouldUseGcm(Subscription sub) {
		return true;
	}

}
