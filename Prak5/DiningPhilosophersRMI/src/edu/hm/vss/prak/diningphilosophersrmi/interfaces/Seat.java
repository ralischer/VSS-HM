package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface Seat extends Remote{

	public void waitForSeat(Philosopher p) throws RemoteException;
	public int getWaitingCount() throws RemoteException;
	public void leaveSeat() throws RemoteException;
	public void setForks(Fork left, Fork right) throws RemoteException;
	public Fork[] getForks() throws RemoteException;
	public Collection<Philosopher> getWaitingPhilosophers() throws RemoteException;
	public void setTable(Table t) throws RemoteException;
	public int getRating() throws RemoteException;
}
