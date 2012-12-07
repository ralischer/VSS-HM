package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;

public class ForkImplementation extends UnicastRemoteObject implements Fork, Serializable{
	
	public ForkImplementation() throws RemoteException {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3354796157642802189L;
	
	private Semaphore semaphore = new Semaphore(1);
	
	private final int instanceNumber;
	private static int instanceCounter = 0;
	{
		instanceNumber = instanceCounter++;
	}

	@Override
	public boolean isShared() throws RemoteException{
		return true;
	}

	
	@Override
	public String toString() {
		return "Fork#"+instanceNumber;
	}


	@Override
	public void request() throws RemoteException, InterruptedException {
		semaphore.acquire();
	}


	@Override
	public void release() throws RemoteException {
		semaphore.release();
	}

}
