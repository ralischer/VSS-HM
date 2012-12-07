package edu.hm.vss.prak.diningphilosophersrmi.graphical;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Event extends Remote{
	public String getIdentifier() throws RemoteException;
	public void setIdentifier(String identifier) throws RemoteException;
	public State getState() throws RemoteException;
	public void setState(State state) throws RemoteException;
	public String getTarget() throws RemoteException;
	public void setTarget(String target) throws RemoteException;
}
