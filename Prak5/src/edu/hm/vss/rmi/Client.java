package edu.hm.vss.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import edu.hm.vss.rmi.interfaces.ICalculator;

public class Client {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		ICalculator calc = (ICalculator) Naming.lookup("//localhost:1099/server");
		System.out.println(calc.add(1, 2));
		System.out.println(calc.subtract(1, 2));
		System.out.println(calc.multiply(1, 2));
		System.out.println(calc.divide(1, 2));
	}
}



