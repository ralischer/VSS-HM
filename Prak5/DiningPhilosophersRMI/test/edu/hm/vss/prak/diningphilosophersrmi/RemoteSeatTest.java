package edu.hm.vss.prak.diningphilosophersrmi;

import static org.junit.Assert.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.ForkImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.SeatImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;


public class RemoteSeatTest {

	private Seat seat;
	
	@Before
	public void before() throws RemoteException, NotBoundException {
		SeatMain.main();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		
		Registry registry = LocateRegistry.getRegistry(10099);
		seat = (Seat) registry.lookup("seat");
		seat.setForks(f0, f1);
	}
}
