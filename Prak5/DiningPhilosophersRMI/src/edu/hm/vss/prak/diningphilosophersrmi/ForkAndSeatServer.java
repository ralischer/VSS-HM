package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.ForkImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.SeatImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class ForkAndSeatServer {

	public static void main(String... args) throws AccessException, RemoteException, NotBoundException {
		String tableHostAddress = args[0];
		int tableHostPort = Integer.parseInt(args[1]);
		int forkAmount = Integer.parseInt(args[2]);
		int seatAmount = Integer.parseInt(args[3]);
		int serverPort = Integer.parseInt(args[4]);
		Table table = (Table) LocateRegistry.getRegistry(tableHostAddress, tableHostPort).lookup("table");
		ForkImplementation[] forks = new ForkImplementation[forkAmount];
		SeatImplementation[] seats = new SeatImplementation[seatAmount];
		//Registry registry = LocateRegistry.createRegistry(serverPort);
		
		//for(int i = 0; i < forkAmount; i++) {
			//forks[i] = new ForkImplementation();
			//Fork forkStub = (Fork) UnicastRemoteObject.exportObject(forks[i],0);
			//registry.rebind("fork"+i, forkStub);
			//System.out.println("table seats " + table.getSeatAmount());
			//table.registerNewFork(forkStub);
		//}
		
		for(int i = 0; i < seatAmount; i++) {
			forks[i] = new ForkImplementation();
			seats[i] = new SeatImplementation();
			//Seat seatStub = (Seat) UnicastRemoteObject.exportObject(seats[i],0);
			//registry.rebind("seat"+i, seatStub);
			new Thread(seats[i]).start();
			seats[i].setTable(table);
			//table.registerNewSeat(seatStub);
			table.registerNewSeatAndFork(seats[i], forks[i]);
		}
		
		//System.out.println("server running");
	}
	
}
