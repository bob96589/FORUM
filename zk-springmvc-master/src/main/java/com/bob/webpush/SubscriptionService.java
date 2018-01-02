package com.bob.webpush;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * An in memory dummy "database" for the example purposes. In a typical Java app
 * this class would be replaced by e.g. EJB or a Spring based service class.
 * <p>
 * In demos/tutorials/examples, get a reference to this service class with
 * {@link SubscriptionService#getInstance()}.
 */
public class SubscriptionService {
	private static SubscriptionService instance;
	private static final Logger LOGGER = Logger.getLogger(SubscriptionService.class.getName());
	private final HashMap<String, Subscription> contacts = new HashMap<>();

	private SubscriptionService() {
	}

	/**
	 * @return a reference to an example facade for Customer objects.
	 */
	public static SubscriptionService getInstance() {
		if (instance == null) {
			instance = new SubscriptionService();
		}
		return instance;
	}

	/**
	 * @return all available Customer objects.
	 */
	public synchronized List<Subscription> findAll() {
		ArrayList<Subscription> arrayList = new ArrayList<>();
		return new ArrayList<Subscription>(contacts.values());
	}

	/**
	 * Deletes a customer from a system
	 *
	 * @param value the Customer to be deleted
	 */
	public synchronized void delete(Subscription value) {
		contacts.remove(value.getKey());
	}

	/**
	 * Persists or updates customer in the system. Also assigns an identifier
	 * for new Customer instances.
	 *
	 * @param entry
	 */
	public synchronized void save(Subscription entry) {
		try {
			entry = (Subscription) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		contacts.put(entry.getKey(), entry);
	}

	/** The Time to live of GCM notifications */
	private static final int TTL = 255;

	public void sendPushMessage(Subscription sub, byte[] payload) throws InterruptedException, GeneralSecurityException, JoseException, ExecutionException, IOException {

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
		pushService.send(notification);
	}

	private boolean shouldUseGcm(Subscription sub) {
		return true;
	}


}