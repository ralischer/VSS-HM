package edu.hm.vss.prak.diningphilosophersrmi.graphical;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EventImplementation extends UnicastRemoteObject implements Event{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3194091395532148998L;
	private String identifier;
	private State state;
	private String target = "";

	public EventImplementation() throws RemoteException {
		super();
	}


	@Override
	public String getIdentifier() throws RemoteException {
		return this.identifier;
	}

	@Override
	public void setIdentifier(String identifier) throws RemoteException {
		this.identifier = identifier;
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}


	@Override
	public String getTarget() throws RemoteException {
		return this.target;
	}


	@Override
	public void setTarget(String target) throws RemoteException {
		this.target  = target;
	}

}
