package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class PhilosopherImplementation implements Philosopher, Runnable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7856989448973511203L;
	private final int instanceNumber;
	private static int instanceCounter = 0;
	{
		instanceNumber = instanceCounter++;
	}
	
	private static final long GREEDY_TIME = 200;
	private static final long EATING_TIME = 900;
	private boolean running = true;
	private int eatings = 0;
	
	private final String MONITOR = new String();
	
	private Table table;
	private Seat seat;
	
	@Override
	public void run() {
		while(running) {
			while(table == null) {
				synchronized (MONITOR) {
					try {
						log(this+" waiting for table");
						MONITOR.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				log(this+" thinking...");
				Thread.sleep(GREEDY_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				seat = table.getBestSeat();
				log(this+" found "+seat.toString());
				seat.waitForSeat(this);
				/*synchronized(MONITOR) {
					try {
						log(this+" waiting for seat...");
						MONITOR.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}*/
				Fork[] forks = seat.getForks();
				synchronized(forks[0]){
					synchronized(forks[1]) {
						log(this+" eating...");
						try {
							Thread.sleep(EATING_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						eatings++;
					}
				}
				seat.leaveSeat();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public int getEatings() throws RemoteException {
		return eatings;
	}

	@Override
	public void seatAvailable(Seat s) throws RemoteException {
		if(s != seat) {
			//old seat is no longer available yet
			seat = s;
		}
		synchronized (MONITOR) {
			MONITOR.notify();
			log(this+" seat available");
		}
	}

	@Override
	public void setTable(Table t) throws RemoteException {
		this.table = t;
		synchronized (MONITOR) {
			MONITOR.notify();
		}
	}

	@Override
	public void stop() {
		running = false;
	}

	public static void log(String s) {
		long time = System.currentTimeMillis();
		System.out.println(time + ": " + s);
	}
	
	@Override
	public String toString() {
		return "Philosopher#"+instanceNumber;
	}

	@Override
	public void pause() throws RemoteException {
		synchronized(MONITOR){
			try {
				MONITOR.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
