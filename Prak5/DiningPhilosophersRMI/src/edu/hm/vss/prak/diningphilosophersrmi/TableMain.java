package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.TableImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class TableMain {

	public static void main(String... args) throws RemoteException {
		LocateRegistry.createRegistry(Integer.parseInt(args[0]));
		TableImplementation table = new TableImplementation();
		
		Table tableStub = (Table) UnicastRemoteObject.exportObject(table,0);
		
		Registry registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
		registry.rebind("table", tableStub);
		System.out.println("table waiting for requests..");
	}
}
