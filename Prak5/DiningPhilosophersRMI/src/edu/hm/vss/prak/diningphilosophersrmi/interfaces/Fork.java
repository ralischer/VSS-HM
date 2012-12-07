package edu.hm.vss.prak.diningphilosophersrmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Fork extends Remote{
	public boolean isShared() throws RemoteException;
	public void request() throws RemoteException, InterruptedException;
	public void release() throws RemoteException;
}
