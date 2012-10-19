package edu.hm.vss.prak3;

public class ProducerThread extends AbstractWorkerThread{

	private static int dataCount = 0;
	public  static final Object PRODUCER_MONITOR = new Object();
	
	@Override
	protected void step() {
		try {
			Thread.sleep(getNextRandomWaitTime());
		} catch(InterruptedException ex) {
			System.err.println("interrupt...");
			//just end the worker
			return;
		}
		int currentDataCount;
		synchronized(PRODUCER_MONITOR) {
			currentDataCount = dataCount++;
		}
		Data data = new Data(getName(),currentDataCount);
		synchronized(MONITOR) {
			if(queue.size() == MAX_ENTRIES) {
				synchronized(PRODUCER_MONITOR) {
					try {
						PRODUCER_MONITOR.wait();
					} catch (InterruptedException ex) {
						//ignored at this point
					}
				}
			}
			queue.add(data);
			System.out.println(getName()+" added data to queue "+data);
			if(queue.size() == 1) {
				//all consumer should sleep -> notify them
				synchronized(ConsumerThread.CONSUMER_MONITOR) {
					ConsumerThread.CONSUMER_MONITOR.notifyAll();
				}
			}
			
		}
		
		//stopWorker();
	}

}
