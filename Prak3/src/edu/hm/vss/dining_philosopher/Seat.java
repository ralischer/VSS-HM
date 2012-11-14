package edu.hm.vss.dining_philosopher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Seat {
	private BlockingQueue<Philosopher> philosopher;
	private Object seat;

	public Seat() {
		this.philosopher = new LinkedBlockingQueue<Philosopher>();
		seat = new Object();
	}

	public int queueSize() {
		return philosopher.size();
	}

	public void register(Philosopher p) throws InterruptedException {
		philosopher.put(p);
	}

	public Object tryAndSit(Philosopher p) throws InterruptedException {
		synchronized (philosopher) {
			if (philosopher.peek().equals(p)) {
				return seat;
			}
			philosopher.wait();
		}
		return tryAndSit(p);
	}

	public void finishEating() {
		philosopher.poll();
		synchronized (philosopher) {
			philosopher.notifyAll();
		}

	}
}
