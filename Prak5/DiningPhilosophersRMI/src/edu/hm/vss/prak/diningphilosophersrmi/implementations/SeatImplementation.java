package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class SeatImplementation implements Seat, Runnable, Comparable<Seat>{
	
	private final int instanceNumber;
	private static int instanceCounter = 0;
	{
		instanceNumber = instanceCounter++;
	}
	
	private Fork leftFork;
	private Fork rightFork;
	private Fork[] forks;
	
	private BlockingDeque<Philosopher> waitingPhilosophers = new LinkedBlockingDeque<Philosopher>();
	
	private Table table;
	
	private final Object MONITOR = new Object();
	private boolean running = true;
	
	private int newPhilosophers = 0;
	private int maxNewPhilosophers = 1;
	
	@Override
	public void run() {
		while(running ) {
			try {
				waitingPhilosophers.take().seatAvailable(this);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (MONITOR) {
				try {
					MONITOR.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void waitForSeat(Philosopher p) throws RemoteException {
		try {
			waitingPhilosophers.put(p);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		p.pause();
		newPhilosophers++;
		if(newPhilosophers > maxNewPhilosophers ){
			if(table != null)
				table.updateQueueSize(this, waitingPhilosophers.size());
		}
//		try {
//			waitingPhilosophers.take().seatAvailable(this);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public int getWaitingCount() throws RemoteException {
		return waitingPhilosophers.size();
	}

	@Override
	public void leaveSeat() throws RemoteException {
		table.updateQueueSize(this, waitingPhilosophers.size());
		synchronized(MONITOR) {
			MONITOR.notify();
		}
	}

	@Override
	public void setForks(Fork left, Fork right) throws RemoteException {
		this.leftFork = left;
		this.rightFork = right;
		forks = new Fork[]{left,right};
		if(table != null)
			table.updateQueueSize(this, waitingPhilosophers.size());
	}

	@Override
	public Fork[] getForks() throws RemoteException {
		//TODO: maybe hold own array
		return forks;//new Fork[]{leftFork, rightFork};
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
			rating += s.getForks()[0].isShared() ? 0 : 1;
			rating += s.getForks()[1].isShared() ? 0 : 1;
			//TODO: test this
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
			return ((SeatImplementation) x).instanceNumber == this.instanceNumber;
		}
		System.err.println("not an instance of seat");
		return false;
	}
	
	@Override
	public int hashCode() {
		return instanceNumber;
	}

	@Override
	public int getRating() throws RemoteException {
		return computeRating(this);
	}
}
