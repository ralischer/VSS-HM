package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Table extends Remote{

	public Seat getBestSeat() throws RemoteException;
	public int getSeatAmount() throws RemoteException;
	@Deprecated
	public void registerNewSeat(Seat s) throws RemoteException;
	@Deprecated
	public void removeSeat(Seat s) throws RemoteException;
	@Deprecated
	public void registerNewFork(Fork f) throws RemoteException;
	@Deprecated
	public void removeFork(Fork f) throws RemoteException;
	public void findNewSeat(Philosopher p) throws RemoteException;
	public void updateQueueSize(Seat s, int waitingPhilosophers) throws RemoteException;
	public void readyToSync(Seat s) throws RemoteException;
	
	public void registerNewSeatAndFork(Seat s, Fork f) throws RemoteException;
	public void removeSeatAndFork(Seat s, Fork f) throws RemoteException;
}
