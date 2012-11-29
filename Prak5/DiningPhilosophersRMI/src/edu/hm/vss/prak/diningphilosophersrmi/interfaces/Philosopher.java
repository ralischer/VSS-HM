package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Philosopher extends Remote{

	public int getEatings() throws RemoteException;
	public void seatAvailable(Seat s) throws RemoteException;
	public void setTable(Table t) throws RemoteException;
	public void stop() throws RemoteException;
	public void pause() throws RemoteException;
	
}
