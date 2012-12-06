package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.SeatImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;

public class SeatMain {

	public static void main(String... args) throws RemoteException {
		LocateRegistry.createRegistry(10099);
		
		SeatImplementation seat = new SeatImplementation();
		//Seat seatStub = (Seat) UnicastRemoteObject.exportObject(seat,0);
		Registry registry = LocateRegistry.getRegistry(10099);
		registry.rebind("seat", seat);
		System.out.println("seat running ..");
	}
	
}
