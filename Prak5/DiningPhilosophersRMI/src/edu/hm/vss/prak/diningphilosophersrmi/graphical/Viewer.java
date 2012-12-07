package edu.hm.vss.prak.diningphilosophersrmi.graphical;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Viewer extends Remote{
	public void addEvent(Event event) throws RemoteException, InterruptedException;
}
