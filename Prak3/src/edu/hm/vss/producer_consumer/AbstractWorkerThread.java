package edu.hm.vss.producer_consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AbstractWorkerThread extends Thread{

	protected static final int MAX_ENTRIES = 25;
	private static final int MAX_WAIT_TIME = 1;
	private static final int MIN_WAIT_TIME = 0;
	private static int instanceCount = 0;
	
	protected static final Object MONITOR = new Object();
	protected static final List<Data> queue = new LinkedList<Data>();
	private boolean running = true;
	
	private static final Random random = new Random();
	
	{
		setName(getClass().getSimpleName() + "#" +instanceCount++);
		//setDaemon(true);
	}
	
	protected synchronized int getNextRandomWaitTime() {
		return (random.nextInt(MAX_WAIT_TIME)+MIN_WAIT_TIME)%MAX_WAIT_TIME;
	}
	
	@Override
	public void run() {
		while(running) {
			step();
		}
	}
	
	public void stopWorker() {
		running = false;
		interrupt();
	}
	
	protected abstract void step();
}
