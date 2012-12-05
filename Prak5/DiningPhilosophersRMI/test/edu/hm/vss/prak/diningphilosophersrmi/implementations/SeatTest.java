package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;


public class SeatTest {

	@Test
	public void testComparability() throws RemoteException{
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Fork f2 = new ForkImplementation();
		SeatImplementation s0 = new SeatImplementation();
		SeatImplementation s1 = new SeatImplementation();
		f0.incrementUsageNumber();
		f0.incrementUsageNumber();
		f1.incrementUsageNumber();
		s0.setForks(f0, f1);
		f1.incrementUsageNumber();
		f2.incrementUsageNumber();
		s1.setForks(f1, f2);
		assertEquals(-1,s1.compareTo(s0));
	}
	
	@Test
	public void testIsLast() throws Exception {
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		SeatImplementation s0 = new SeatImplementation();
		s0.setForks(f0, f1);
		assertEquals(f0, s0.getLeftFork());
		s0.setLast(true);
		assertEquals(f1, s0.getLeftFork());
	}
	
	//@Test
	public void testComparabilityWithPhilosophers() throws RemoteException{
		PhilosopherImplementation p = new PhilosopherImplementation();
		PhilosopherImplementation p0 = new PhilosopherImplementation();
		PhilosopherImplementation p1 = new PhilosopherImplementation();
		PhilosopherImplementation p2 = new PhilosopherImplementation();
		Fork f0 = new ForkImplementation();
		Fork f1 = new ForkImplementation();
		Fork f2 = new ForkImplementation();
		SeatImplementation s0 = new SeatImplementation();
		SeatImplementation s1 = new SeatImplementation();
		f0.incrementUsageNumber();
		f0.incrementUsageNumber();
		f1.incrementUsageNumber();
		s0.setForks(f0, f1);
		f1.incrementUsageNumber();
		f2.incrementUsageNumber();
		s1.waitForSeat(p);
		s1.waitForSeat(p0);
		s1.waitForSeat(p1);
		s1.waitForSeat(p2);
		s1.setForks(f1, f2);
		assertEquals(1,s1.compareTo(s0));
	}
}
