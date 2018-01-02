
'use strict';

const applicationServerPublicKey = 'BKPaf4qsjD_NtdmcTh6tB39UaEe41QCALL-lP0FyH7oiFm9cVUYnwIjyzPUWdhr1XXXPRPIh1A7kbbXfva4ViPw';
// private key: cYGuJEo3AiE5ORH94J1tIoTDxp1Oqz1qZNHi0X1wnHc


var pushButton;

window.onload = function() {
	pushButton = document.querySelector('.js-push-btn');
	if ('serviceWorker' in navigator && 'PushManager' in window) {
		console.log('Service Worker and Push is supported');

		//TODO 1. service worker resgistration
		navigator.serviceWorker.register('sw.js')
			.then(function(swReg) {
				console.log('Service Worker is registered', swReg);

				swRegistration = swReg;
				initialiseUI();
			})
			.catch(function(error) {
				console.error('Service Worker Error', error);
			});
	} else {
		console.warn('Push messaging is not supported');
		pushButton.textContent = 'Push Not Supported';
	}
}

let isSubscribed = false;
let swRegistration = null;

function urlB64ToUint8Array(base64String) {
	const padding = '='.repeat((4 - base64String.length % 4) % 4);
	const base64 = (base64String + padding)
		.replace(/\-/g, '+')
		.replace(/_/g, '/');

	const rawData = window.atob(base64);
	const outputArray = new Uint8Array(rawData.length);

	for (let i = 0; i < rawData.length; ++i) {
		outputArray[i] = rawData.charCodeAt(i);
	}
	return outputArray;
}

function initialiseUI() {
	pushButton.addEventListener('click', function() {
		pushButton.disabled = true;
		if (isSubscribed) {
			unsubscribeUser();
		} else {
			subscribeUser();
		}
	});

	// check if user is subscribed
	// Set the initial subscription value
	swRegistration.pushManager.getSubscription()
		.then(function(subscription) {
			isSubscribed = !(subscription === null);

			if (isSubscribed) {
				console.log('User IS subscribed.');
			} else {
				console.log('User is NOT subscribed.');
			}

			updateBtn();
		});
}

function subscribeUser() {
	const applicationServerKey = urlB64ToUint8Array(applicationServerPublicKey);
	// permission is requested automatically
	// TODO 2. get subscription object
	swRegistration.pushManager.subscribe({
			//The userVisibleOnly parameter is basically
			//an admission that you will show a notification every time a push is sent.
			userVisibleOnly: true,
			applicationServerKey: applicationServerKey
		})
		.then(function(subscription) {
			console.log('User is subscribed.');

			// 3. update db
			updateSubscriptionOnServer(subscription);

			isSubscribed = true;

			updateBtn();
		})
		.catch(function(err) {
			 console.log('Failed to subscribe the user: ', err);
			updateBtn();
		});
}

function unsubscribeUser() {
	swRegistration.pushManager.getSubscription()
		.then(function(subscription) {
			if (subscription) {
			updateSubscriptionOnServer(subscription, 'delete');
			console.log('User is unsubscribed.');
			isSubscribed = false;
				// Tell application server to delete subscription
				return subscription.unsubscribe();
			}
		})
		.catch(function(error) {
			console.log('Error unsubscribing', error);
		})
		.then(function() {
			updateBtn();
		});
}


function updateSubscriptionOnServer(subscription, action) {
	//send subscription to application server
	sendSubscriptionToServer(subscription, action);

	const subscriptionJson = document.querySelector('.js-subscription-json');
	const subscriptionDetails =
		document.querySelector('.js-subscription-details');

	if (subscription && action != 'delete') {
		subscriptionJson.textContent = JSON.stringify(subscription);
	} else {
		subscriptionJson.textContent = 'X';
	}
}

function updateBtn() {
	if (Notification.permission === 'denied') {
		pushButton.textContent = 'Push Messaging Blocked.';
		pushButton.disabled = true;
		updateSubscriptionOnServer(null);
		return;
	}

	if (isSubscribed) {
		pushButton.textContent = 'Disable Push Messaging';
	} else {
		pushButton.textContent = 'Enable Push Messaging';
	}

	pushButton.disabled = false;
}

function push() {
	jq.ajax({
		url: 'profile/push',
		type: 'POST',
		success: function(data, textStatus, jqXHR) {
			console.log('push success');
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('push error');
			console.log('errorThrown: ' + errorThrown);
		}
	});
}

// TODO 3. update db
function sendSubscriptionToServer(subscription, action) {

	if (!subscription) {
		return;
	}
	// Get public key and user auth from the subscription object
	var key = subscription.getKey ? subscription.getKey('p256dh') : '';
	var auth = subscription.getKey ? subscription.getKey('auth') : '';

	// This example uses the new fetch API. This is not supported in all
	// browsers yet.
	return fetch('profile/subscription', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			endpoint: subscription.endpoint,
			// Take byte[] and turn it into a base64 encoded string suitable for
			// POSTing to a server over HTTP
			key: key ? btoa(String.fromCharCode.apply(null, new Uint8Array(key))) : '',
			auth: auth ? btoa(String.fromCharCode.apply(null, new Uint8Array(auth))) : '',
			action: action
		})
	});
}
