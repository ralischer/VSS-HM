package edu.hm.vss.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.rmi.interfaces.ICalculator;

public class Server implements ICalculator {

	protected Server() {
		super();
	}

	@Override
	public double add(double a, double b) {
		return a + b;
	}

	@Override
	public double multiply(double a, double b) {
		return a * b;
	}

	@Override
	public double subtract(double a, double b) throws RemoteException {
		return a - b;
	}

	@Override
	public double divide(double a, double b) throws RemoteException {
		return a / b;
	}

	public static void main(String args[]) throws RemoteException {
		LocateRegistry.createRegistry(1099);
		Server server = new Server();

		ICalculator calcStub = (ICalculator) UnicastRemoteObject.exportObject(
				server, 0);

		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("server", calcStub);
		System.out.println("server waiting for requests");
	}

}
