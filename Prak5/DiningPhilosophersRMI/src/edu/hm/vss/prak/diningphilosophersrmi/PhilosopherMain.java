package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.prak.diningphilosophersrmi.graphical.Viewer;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.PhilosopherImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;

public class PhilosopherMain {

	public static void main(String... args) throws RemoteException, NumberFormatException, NotBoundException {
		LocateRegistry.createRegistry(Integer.parseInt(args[0]));
		Viewer viewer = null;
		if(args.length == 3) {
			viewer = getViewer(args[1], Integer.parseInt(args[2]));
		}
		PhilosopherImplementation pi = new PhilosopherImplementation();
		if(viewer != null) {
			pi.setViewer(viewer);
		}
		Philosopher philosopherStub = (Philosopher) UnicastRemoteObject.exportObject(pi,0);
		Thread philosopherThread = new Thread(pi);
		philosopherThread.start();
		Registry registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
		registry.rebind("philosopher", philosopherStub);
		System.out.println("philosopher running");
	}
	
	private static Viewer getViewer(String host, int port) throws AccessException, RemoteException, NotBoundException {
		return (Viewer) LocateRegistry.getRegistry(host,port).lookup("viewer");
	}
	
}
