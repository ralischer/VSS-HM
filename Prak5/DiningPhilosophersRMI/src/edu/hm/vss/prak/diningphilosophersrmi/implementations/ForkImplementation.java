package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;

public class ForkImplementation implements Fork, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3354796157642802189L;
	
	private final int instanceNumber;
	private static int instanceCounter = 0;
	{
		instanceNumber = instanceCounter++;
	}
	
	private int usageNumber = 0;

	@Override
	public boolean isShared() throws RemoteException{
		synchronized(this) {
			return usageNumber > 1;
		}
	}

	@Override
	public void incrementUsageNumber() throws RemoteException {
		synchronized(this) {
			usageNumber++;
		}
	}

	@Override
	public void decrementUsageNumber() throws RemoteException {
		synchronized(this) {
			usageNumber--;
		}
	}
	
	@Override
	public String toString() {
		return "Fork#"+instanceNumber;
	}

}
