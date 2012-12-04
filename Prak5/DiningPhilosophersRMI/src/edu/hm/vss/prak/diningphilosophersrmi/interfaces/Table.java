package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Table extends Remote{

	public Seat getBestSeat() throws RemoteException;
	public int getSeatAmount() throws RemoteException;
	public void registerNewSeat(Seat s) throws RemoteException;
	public void removeSeat(Seat s) throws RemoteException;
	public void registerNewFork(Fork f) throws RemoteException;
	public void removeFork(Fork f) throws RemoteException;
	public void findNewSeat(Philosopher p) throws RemoteException;
	public void updateQueueSize(Seat s, int waitingPhilosophers) throws RemoteException;
	public void readyToSync(Seat s) throws RemoteException;
}
