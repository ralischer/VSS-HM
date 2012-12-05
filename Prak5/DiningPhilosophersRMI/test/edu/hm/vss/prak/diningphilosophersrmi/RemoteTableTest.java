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
		TableMain.main("1099");
		Registry registry = LocateRegistry.getRegistry(1099);
		table = (Table)registry.lookup("table");
		
	}
	
	//@Test
	public void testTable() throws RemoteException {
		table = new TableImplementation();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Seat s0 = new SeatImplementation();
		Seat s1 = new SeatImplementation();
		try {
			s0.setTable(table);
			s1.setTable(table);
			table.registerNewSeatAndFork(s0, f0);
			table.registerNewSeatAndFork(s1, f1);
			//table.registerNewFork(f0);
			//table.registerNewFork(f1);
			//table.registerNewSeat(s0);
			//table.registerNewSeat(s1);
			assertEquals(2, table.getSeatAmount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testRegisterNewForkAndSeat() throws RemoteException {
		table = new TableImplementation();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		SeatImplementation s0 = new SeatImplementation();
		SeatImplementation s1 = new SeatImplementation();
		Fork newFork = new ForkImplementation();
		SeatImplementation newSeat = new SeatImplementation();
		try {
			s0.setTable(table);
			s1.setTable(table);
			newSeat.setTable(table);
			Thread t0 = new Thread(s0);
			Thread t1 = new Thread(s1);
			Thread t2 = new Thread(newSeat);
			t0.start();
			t1.start();
			t2.start();
			table.registerNewSeatAndFork(s0, f0);
			table.registerNewSeatAndFork(s1, f1);
			//table.registerNewFork(f0);
			//table.registerNewFork(f1);
			//table.registerNewSeat(s0);
			//table.registerNewSeat(s1);
			assertEquals(2, table.getSeatAmount());
			table.registerNewSeatAndFork(newSeat, newFork);
			Thread.sleep(5000);
			assertEquals(3, table.getSeatAmount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
}
