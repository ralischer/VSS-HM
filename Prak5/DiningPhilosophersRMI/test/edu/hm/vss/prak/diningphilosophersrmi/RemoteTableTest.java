package edu.hm.vss.prak.diningphilosophersrmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.ForkImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.SeatImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.TableImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class RemoteTableTest {

	private Table table;
	
	@Before
	public void before() throws RemoteException, NotBoundException {
		TableMain.main();
		Registry registry = LocateRegistry.getRegistry(1099);
		table = (Table)registry.lookup("table");
		
	}
	
	@Test
	public void testTable() {
		table = new TableImplementation();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Seat s0 = new SeatImplementation();
		Seat s1 = new SeatImplementation();
		try {
			s0.setTable(table);
			s1.setTable(table);
			table.registerNewFork(f0);
			table.registerNewFork(f1);
			table.registerNewSeat(s0);
			table.registerNewSeat(s1);
			assertEquals(2, table.getSeatAmount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
}
