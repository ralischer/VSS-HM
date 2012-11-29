package edu.hm.vss.prak.diningphilosophersrmi;

import static org.junit.Assert.*;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.PhilosopherImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class RemoteForkAndSeatServerTest {
	
	@BeforeClass
	public static void setUp() throws Exception {
		TableMain.main("1102");
		ForkAndSeatServer.main("localhost","1102","3","3","1103");
	}

	@Test
	public void testMain() throws AccessException, RemoteException, NotBoundException {
		Table table = (Table) LocateRegistry.getRegistry(1102).lookup("table");
		assertNotNull(table);
		assertEquals(3,table.getSeatAmount());
	}

	@Test
	public void testWithOnePhilosopher() throws AccessException, RemoteException, NotBoundException, InterruptedException {
		Table table = (Table) LocateRegistry.getRegistry(1102).lookup("table");
		assertNotNull(table);
		assertEquals(3,table.getSeatAmount());
		PhilosopherImplementation p = new PhilosopherImplementation();
		p.setTable(table);
		new Thread(p).start();
		Thread.sleep(100);
		p.stop();
		Thread.sleep(3000);
		assertEquals(1,p.getEatings());
		
	}
	
}
