package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.PhilosopherImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;

public class PhilosopherMain {

	public static void main(String... args) throws RemoteException {
		LocateRegistry.createRegistry(1100);
		
		PhilosopherImplementation pi = new PhilosopherImplementation();
		Philosopher philosopherStub = (Philosopher) UnicastRemoteObject.exportObject(pi,0);
		
		Registry registry = LocateRegistry.getRegistry(1100);
		registry.rebind("philosopher", philosopherStub);
		System.out.println("philosopher running");
	}
	
}
