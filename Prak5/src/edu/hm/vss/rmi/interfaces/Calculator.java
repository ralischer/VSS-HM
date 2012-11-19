package edu.hm.vss.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote{

	public Double add(double a, double b) throws RemoteException;
	public Double multiply(double a, double b) throws RemoteException;

}
