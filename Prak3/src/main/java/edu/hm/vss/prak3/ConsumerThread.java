package edu.hm.vss.prak3;

public class ConsumerThread extends AbstractWorkerThread {

	public static final Object CONSUMER_MONITOR = new Object();

	@Override
	protected void step() {
		try {
			Thread.sleep(getNextRandomWaitTime());
		} catch (InterruptedException ex) {
			System.err.println("interrupt...");
			// just end the worker
			return;
		}
		synchronized (MONITOR) {
			while (queue.size() == 0) {
				//synchronized (CONSUMER_MONITOR) {
					try {
						MONITOR.wait();
					} catch (InterruptedException ex) {
						// ignored at this point
					}
				//}
				//return;
			}
			Data data = queue.remove(0);
			System.out.println(getName() + " read data " + data);
			
			if (queue.size() < MAX_ENTRIES) {
				// all producer should sleep -> notify them
				//synchronized (ProducerThread.PRODUCER_MONITOR) {
				//	ProducerThread.PRODUCER_MONITOR.notifyAll();
				//}
				MONITOR.notifyAll();
			}

		}
	}

}
