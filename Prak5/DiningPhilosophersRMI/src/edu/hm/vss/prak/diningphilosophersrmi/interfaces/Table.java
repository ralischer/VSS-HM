package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Table extends Remote{

	public Seat getBestSeat(String host) throws RemoteException;
	public int getSeatAmount() throws RemoteException;
	public void findNewSeat(Philosopher p) throws RemoteException;
	public void updateQueueSize(Seat s, int waitingPhilosophers) throws RemoteException;
	public void readyToSync(Seat s) throws RemoteException;
	
	public void registerNewSeatAndFork(Seat s, Fork f, String host) throws RemoteException;
	public void removeSeatAndFork(Seat s, Fork f, String host) throws RemoteException;
	public void printUsableSeats() throws RemoteException;
}
