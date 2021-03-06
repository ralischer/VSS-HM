package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class SeatImplementation extends UnicastRemoteObject implements Seat, Runnable, Comparable<Seat>, Serializable{
	
	public SeatImplementation() throws RemoteException {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5639426373498801933L;
	private static final String hostname = System.getProperty("java.rmi.server.hostname");
	private final int instanceNumber;
	private static int instanceCounter = 0;
	{
		instanceNumber = instanceCounter++;
	}
	
	private Fork leftFork;
	private Fork rightFork;
	
	private BlockingDeque<Philosopher> waitingPhilosophers = new LinkedBlockingDeque<Philosopher>();
	
	private Table table;
	
	private final String MONITOR = new String();
	private boolean running = true;
	
	private boolean sync = false;
	private boolean isLast = false;
	
	@Override
	public void run() {
		while(running ) {
			try {
				while(sync) {
					//System.out.println(this+" ready to sync going to tell the table");
					table.readyToSync(this);
					synchronized (MONITOR) {
						MONITOR.wait();
					}
				}
				Philosopher p = waitingPhilosophers.poll(1000, TimeUnit.MILLISECONDS);
				if(p != null) {
					//System.out.println("philosopher ready");
					p.seatAvailable(this);
				} else {
					continue;
				}
				//waitingPhilosophers.take().seatAvailable(this);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (MONITOR) {
				try {
					//System.out.println(this+" waiting for philosopher ready...");
					MONITOR.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void waitForSeat(final Philosopher p) throws RemoteException {
		Thread waitingThread = new Thread() {
			public void run() {
				try {
					p.pause();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		};
		waitingThread.start();
		//p.pause();
		//System.out.println(this+"adding philosopher to queue");
		try {
			waitingPhilosophers.put(p);
			waitingThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		
	}

	@Override
	public int getWaitingCount() throws RemoteException {
		return waitingPhilosophers.size();
	}

	int count = 0;
	private Seat nextSeat;
	private Seat previousSeat;
	
	@Override
	public void leaveSeat() throws RemoteException {
		synchronized(MONITOR) {
			MONITOR.notify();
		}
	}

	@Override
	public void setForks(Fork left, Fork right) throws RemoteException {
		this.leftFork = left;
		this.rightFork = right;
	}
	
	@Override
	public Fork getLeftFork() throws RemoteException {
		if(isLast) {
			return rightFork;
		} else {
			return leftFork;
		}
	}	
	
	@Override
	public Fork getRightFork() throws RemoteException {
		if(isLast) {
			return leftFork;
		} else {
			return rightFork;
		}
	}

	@Override
	public Collection<Philosopher> getWaitingPhilosophers()
			throws RemoteException {
		return waitingPhilosophers;
	}

	@Override
	public void setTable(Table t) throws RemoteException {
		this.table = t;		
	}
	
	@Override
	public String toString(){
		return "Seat#"+instanceNumber+"{"+leftFork+", "+rightFork+"}";
	}
	
	private static int computeRating(Seat s) {
		int rating = 1;
		try {
			rating += s.getLeftFork().isShared() ? 0 : 1;
			rating += s.getRightFork().isShared() ? 0 : 1;
			rating -= ((SeatImplementation)s).waitingPhilosophers.size()/rating;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return rating;
	}

	@Override
	public int compareTo(Seat o) {
		if(this.instanceNumber == ((SeatImplementation)o).instanceNumber) {
			return 0;
		}
		int diff = computeRating(o)-computeRating(this);
		if(diff == 0) {
			return 1;
		}
		return diff;
	}

	@Override
	public boolean equals(Object x) {
		if(x instanceof SeatImplementation) {
			return ((SeatImplementation) x).hashCode() == this.hashCode();
		}
		System.err.println("not an instance of seat");
		return false;
	}
	
	@Override
	public int hashCode() {
		return hostname.hashCode()+instanceNumber;
	}

	@Override
	public void pauseForSync() throws RemoteException {
		sync  = true;
		/*synchronized (MONITOR) {
			MONITOR.notify();
		}*/
	}

	@Override
	public void continueAfterSync() throws RemoteException {
		sync = false;
		synchronized (MONITOR) {
			MONITOR.notify();
		}
	}

	@Override
	public void setLast(boolean isLast) throws RemoteException {
		this.isLast = isLast;		
	}

	@Override
	public Seat getNextSeat() throws RemoteException {
		return nextSeat;
	}

	@Override
	public void setNextSeat(Seat next) throws RemoteException {
		this.nextSeat = next;
	}

	@Override
	public Seat getPrevious() throws RemoteException {
		return previousSeat;
	}

	@Override
	public void setPrevious(Seat previous) throws RemoteException {
		this.previousSeat = previous;
	}

	@Override
	public String getIdentitifier() throws RemoteException {
		return hostname+"-Seat#"+instanceNumber;
	}

	@Override
	public void addPhilosopherToQueue(Philosopher p) throws RemoteException, InterruptedException {
		waitingPhilosophers.put(p);
	}
}
