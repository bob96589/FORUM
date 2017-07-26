package test;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * this is a passive working thread, it should be isolated with ViewModel
 * 
 * @author dennis
 * 
 */
public class F01141LongOperation3Asyhcnronizer {

	String queueName;
	String gcmdName;

	public F01141LongOperation3Asyhcnronizer(String queueName, String globalCommandName) {
		this.queueName = queueName;
		this.gcmdName = globalCommandName;
	}

	public void startLongOperation(final F01141LongOperation3Worker worker) {
		final String workingQueueName = "workingQueue" + System.currentTimeMillis();
		EventQueue eq = EventQueues.lookup(workingQueueName); // create a queue
		eq.subscribe(new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				worker.doLongOp();
			}
		}, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(queueName, null, gcmdName, null);
				EventQueues.remove(workingQueueName);
			}
		});
		eq.publish(new Event("trigger"));
	}
}
