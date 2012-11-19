package edu.hm.vss.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.vss.rmi.interfaces.Calculator;

public class Server implements Calculator {

	protected Server() {
		super();
	}

	public Double add(double a, double b) {
		return new Double(a + b);
	}

	public Double multiply(double a, double b) {
		return new Double(a * b);
	}

	public static void main(String args[]) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "Calc6";
			Calculator calc = new Server();
			Calculator stub = (Calculator) UnicastRemoteObject.exportObject(
					calc, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("Calc bound");
		} catch (Exception e) {
			System.err.println("Calc exception:");
			e.printStackTrace();
		}

	}

}
