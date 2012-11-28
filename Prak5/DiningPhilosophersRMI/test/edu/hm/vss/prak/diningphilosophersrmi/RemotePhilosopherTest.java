package edu.hm.vss.prak.diningphilosophersrmi;

import static org.junit.Assert.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;


public class RemotePhilosopherTest {

	private Philosopher philosopher;
	
	@Before
	public void before() throws RemoteException, NotBoundException {
		PhilosopherMain.main();
		
		Registry registry = LocateRegistry.getRegistry(1100);
		philosopher = (Philosopher) registry.lookup("philosopher");
	}
	
	@Test
	public void test() throws RemoteException {
		assertEquals(0,philosopher.getEatings());
	}
}
