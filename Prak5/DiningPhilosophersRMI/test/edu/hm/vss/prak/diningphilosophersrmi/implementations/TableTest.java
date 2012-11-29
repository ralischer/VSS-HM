package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class TableTest {

	private TableImplementation t;

	
	@Before
	public void testTable() {
		t = new TableImplementation();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Seat s0 = new SeatImplementation();
		Seat s1 = new SeatImplementation();
		try {
			s0.setTable(t);
			s1.setTable(t);
			t.registerNewFork(f0);
			t.registerNewFork(f1);
			t.registerNewSeat(s0);
			t.registerNewSeat(s1);
			//System.out.println(t.usableSeats.toString());
			assertEquals(2, t.usableSeats.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testInsertNewForks() {
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		try {
			t.registerNewFork(f0);
			t.registerNewFork(f1);
			System.out.println(t.usableSeats.toString());
			assertEquals(t.usableSeats.toString(),2,t.usableSeats.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testInsertNewSeat() {
		testInsertNewForks();
		Seat s0 = new SeatImplementation();	
		Seat s1 = new SeatImplementation();		
		try {
			s0.setTable(t);
			s1.setTable(t);
			t.registerNewSeat(s0);
			System.out.println(t.usableSeats.toString());
			assertEquals(t.usableSeats.toString(),3,t.usableSeats.size());
			t.registerNewSeat(s1);
			System.out.println(t.usableSeats.toString());
			assertEquals(t.usableSeats.toString(),4,t.usableSeats.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testInsertSeatsFirst() {
		TableImplementation t = new TableImplementation();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Seat s0 = new SeatImplementation();
		Seat s1 = new SeatImplementation();
		try {
			s0.setTable(t);
			s1.setTable(t);
			t.registerNewSeat(s0);
			t.registerNewSeat(s1);
			t.registerNewFork(f0);
			t.registerNewFork(f1);
			assertEquals(t.usableSeats.toString(),2,t.usableSeats.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testInsert() {
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Seat s0 = new SeatImplementation();
		Seat s1 = new SeatImplementation();
		try {
			s0.setTable(t);
			s1.setTable(t);
			t.registerNewSeat(s0);
			t.registerNewFork(f0);
			t.registerNewFork(f1);
			t.registerNewSeat(s1);
			assertEquals(t.usableSeats.toString(), 3, t.usableSeats.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testGetBestSeat() throws RemoteException {
		assertNotNull(t.getBestSeat());
	}
}
