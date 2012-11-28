package edu.hm.vss.prak.diningphilosophersrmi;

import static org.junit.Assert.*;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class RemoteForkAndSeatServerTest {
	
	@Before
	public void setUp() throws Exception {
		TableMain.main();
		ForkAndSeatServer.main("localhost","1099","3","3","1100");
	}

	@Test
	public void testMain() throws AccessException, RemoteException, NotBoundException {
		Table table = (Table) LocateRegistry.getRegistry(1099).lookup("table");
		assertNotNull(table);
		assertEquals(3,table.getSeatAmount());
	}

}
