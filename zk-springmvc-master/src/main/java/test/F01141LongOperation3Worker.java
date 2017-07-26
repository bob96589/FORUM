package test;


/**
 * this is a passive working thread, it should be isolated with ViewModel
 * 
 * @author dennis
 * 
 */
public class F01141LongOperation3Worker {

	String result;

	public String getResult() {
		return result;
	}

	public void doLongOp() {
		try {
			Thread.sleep(3000);// simulate long op
			result = "It has done..";
		} catch (InterruptedException e) {
		}
	}

}
