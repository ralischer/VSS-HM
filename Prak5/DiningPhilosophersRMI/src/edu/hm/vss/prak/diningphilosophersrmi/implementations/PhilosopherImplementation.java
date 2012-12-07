package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Random;

import edu.hm.vss.prak.diningphilosophersrmi.graphical.Event;
import edu.hm.vss.prak.diningphilosophersrmi.graphical.EventImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.graphical.State;
import edu.hm.vss.prak.diningphilosophersrmi.graphical.Viewer;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
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
		EATING_TIME = 100+random.nextInt(3000);
		GREEDY_TIME = 100+random.nextInt(3000);
	}
	
	private static final Random random = new Random();
	
	private final long GREEDY_TIME;
	private final long EATING_TIME;
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
						if(viewer != null) {
							try {
								Event event = new EventImplementation();
								event.setIdentifier(getName());
								event.setState(State.WAITING);
								event.setTarget("Table");
								viewer.addEvent(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						MONITOR.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				if(viewer != null) {
					try {
						Event event = new EventImplementation();
						event.setIdentifier(getName());
						event.setState(State.THINKING);
						event.setTarget("-");
						viewer.addEvent(event);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
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
					if(currentSeat.getWaitingCount() < currentBest.getWaitingCount()) {
						currentBest = currentSeat;
					}
						if(currentBest.getWaitingCount() == 0) {
							break;
						}
					currentSeat = currentSeat.getNextSeat();
				}
				
				seat = currentBest;
				
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
				//synchronized(seat.getLeftFork()){
					//synchronized(seat.getRightFork()) {
				Fork left = seat.getLeftFork();
				Fork right = seat.getRightFork();
				left.request();
				right.request();
						if(viewer != null) {
							Event event = new EventImplementation();
							event.setIdentifier(getName());
							event.setState(State.EATING);
							event.setTarget(seat.getIdentitifier());
							try {
								viewer.addEvent(event);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						try {
							Thread.sleep(EATING_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						eatings++;
					//}
				//}
						right.release();
						left.release();
				if(viewer != null) {
					Event event = new EventImplementation();
					event.setIdentifier(getName());
					event.setState(State.WAITING);
					event.setTarget("next round...");
					try {
						viewer.addEvent(event);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				seat.leaveSeat();
				seatAvailable = false;
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
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
			if(viewer != null) {
				Event event = new EventImplementation();
				event.setIdentifier(getName());
				event.setState(State.WAITING);
				event.setTarget(seat.getIdentitifier());
				try {
					viewer.addEvent(event);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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
	
	private Viewer viewer;
	
	public void setViewer(Viewer viewer) throws RemoteException{
		this.viewer = viewer;
	}
}
