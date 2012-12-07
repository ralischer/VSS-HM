package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class PhilosopherImplementation implements Philosopher, Runnable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7856989448973511203L;
	private static final String hostname = System.getProperty("java.rmi.server.hostname");
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
				seat = table.getBestSeat(hostname);
				//TODO: überarbeiten..
				Seat currentBest = seat;
				Seat currentSeat = seat.getNextSeat();
				while(!currentSeat.getIdentitifier().equals(seat.getIdentitifier())) {
					log("searching...s");
					if(currentSeat.getWaitingCount() < currentBest.getWaitingCount()) {
						currentBest = currentSeat;
					}
						if(currentBest.getWaitingCount() == 0) {
							break;
						}
					currentSeat = currentSeat.getNextSeat();
				}
				
				
				//log(this+" found "+seat.toString());
				currentBest.waitForSeat(this);
				/*
				synchronized(MONITOR) {
					try {
						log(this+" waiting for seat...");
						MONITOR.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				*/
				//Fork[] forks = seat.getForks();
				synchronized(seat.getLeftFork()){
					synchronized(seat.getRightFork()) {
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
				seatAvailable = false;
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
		seatAvailable = true;
		synchronized (MONITOR) {
			MONITOR.notify();
			log(this+" seat available");
		}
	}

	private boolean seatAvailable = false;
	
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
		if(seatAvailable) {
			seatAvailable = false;
			return;
		}
		synchronized(MONITOR){
			try {
				MONITOR.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() throws RemoteException {
		return hostname+"-Philosopher#"+instanceNumber;
	}
}
