package test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

public class F01141LongOperation3VM {

	String result = "";

	EventQueue<Event> eventQueue;
	EventListener<Event> listener;
	String workingQueueName;

	@GlobalCommand("cancelArticle")
	@NotifyChange({ "value", "btnVisible" })
	public void cancel() {
		eventQueue.unsubscribe(listener);
		eventQueue = null;
		EventQueues.remove(workingQueueName);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", false);
		args.put("text", "cancel..");
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
	}

	@Command
	@NotifyChange({ "value", "btnVisible" })
	public void doLongOp() {
		if(eventQueue != null){
			return;
		}
		workingQueueName = "workingQueue" + System.currentTimeMillis();
		eventQueue = EventQueues.lookup(workingQueueName, EventQueues.APPLICATION, true);
		eventQueue.subscribe(listener = new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				System.out.println("update article");
				result = "It has done..";
			}
		}, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("memoVisible", false);
				args.put("text", result);
				BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
				eventQueue.unsubscribe(listener);
				eventQueue = null;
				EventQueues.remove(workingQueueName);
			}
		});

		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
		scheduledThreadPool.schedule(new Runnable() {
			@Override
			public void run() {
				eventQueue.publish(new Event("trigger"));
			}
		}, 3, TimeUnit.SECONDS);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("memoVisible", true);
		args.put("text", "Processing..");
		BindUtils.postGlobalCommand(null, EventQueues.DESKTOP, "updateMemo", args);
	}
}
