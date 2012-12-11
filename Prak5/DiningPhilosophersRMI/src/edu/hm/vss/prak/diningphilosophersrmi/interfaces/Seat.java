package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface Seat extends Remote{

	public void waitForSeat(Philosopher p) throws RemoteException;
	public int getWaitingCount() throws RemoteException;
	public void leaveSeat() throws RemoteException;
	public void setForks(Fork left, Fork right) throws RemoteException;
	public Collection<Philosopher> getWaitingPhilosophers() throws RemoteException;
	public void setTable(Table t) throws RemoteException;
	public void pauseForSync() throws RemoteException;
	public void continueAfterSync() throws RemoteException;
	public void setLast(boolean isLast) throws RemoteException;
	public Fork getLeftFork() throws RemoteException;
	public Fork getRightFork() throws RemoteException;
	public Seat getNextSeat() throws RemoteException;
	public void setNextSeat(Seat next) throws RemoteException;
	public Seat getPrevious() throws RemoteException;
	public void setPrevious(Seat previous) throws RemoteException;
	public String getIdentitifier() throws RemoteException;
}
