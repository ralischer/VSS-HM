package edu.hm.vss.prak.diningphilosophersrmi;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;

public class RemoteForkTest {

	private Fork fork;
	
	@Before
	public void setUp() throws Exception {
		ForkMain.main();
		Registry registry = LocateRegistry.getRegistry(1101);
		fork = (Fork) registry.lookup("fork");
		assertNotNull(fork);
	}

	@Test
	public void testMain() throws RemoteException {
		assertFalse(fork.isShared());
		fork.incrementUsageNumber();
		assertFalse(fork.isShared());
		fork.incrementUsageNumber();
		assertTrue(fork.isShared());
	}

}
