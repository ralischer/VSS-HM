package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.ForkImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;

public class ForkMain {

	public static void main(String... args) throws RemoteException {
		LocateRegistry.createRegistry(1101);
		
		ForkImplementation forkImplementation = new ForkImplementation();
		Fork forkStub = (Fork) UnicastRemoteObject.exportObject(forkImplementation,0);
		
		Registry registry = LocateRegistry.getRegistry(1101);
		registry.rebind("fork", forkStub);
		System.out.println("fork running");
	}
	
}
