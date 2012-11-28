package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;


public class PhilosopherTest {

	private TableImplementation t;
	private PhilosopherImplementation[] philosophers;
	
	@Before
	public void before() throws RemoteException {
		t = new TableImplementation();
		philosophers = new PhilosopherImplementation[4];
		for(int i = 0; i < 4; i++) {
			Fork f = new ForkImplementation();
			t.registerNewFork(f);
			SeatImplementation s = new SeatImplementation();
			s.setTable(t);
			t.registerNewSeat(s);
			new Thread(s).start();
			philosophers[i] = new PhilosopherImplementation();
			philosophers[i].setTable(t);
		}
		assertEquals(t.usableSeats.toString(),4,t.usableSeats.size());
	}
	
	@Test
	public void testPhilosopherEating() throws RemoteException {
		assertEquals(0,philosophers[0].getEatings());
	}
	
	@Test
	public void testRunOnePhilosopher() throws Exception {
		new Thread(philosophers[0]).start();
		Thread.sleep(100);
		philosophers[0].stop();
		Thread.sleep(5000);
		assertEquals(1, philosophers[0].getEatings());
	}
	
	@Test
	public void testTwoPhilosophers() throws Exception {
		new Thread(philosophers[0]).start();
		new Thread(philosophers[1]).start();
		Thread.sleep(100);
		philosophers[0].stop();
		philosophers[1].stop();
		Thread.sleep(5000);
		assertEquals(1, philosophers[0].getEatings());
		assertEquals(1, philosophers[1].getEatings());
		
	}
	
	@Test
	public void testFourPhilosophers() throws Exception {
		new Thread(philosophers[0]).start();
		new Thread(philosophers[1]).start();
		Thread.sleep(100);
		philosophers[0].stop();
		philosophers[1].stop();
		new Thread(philosophers[2]).start();
		new Thread(philosophers[3]).start();
		Thread.sleep(100);
		philosophers[2].stop();
		philosophers[3].stop();
		Thread.sleep(5000);
		assertEquals(1, philosophers[0].getEatings());
		assertEquals(1, philosophers[1].getEatings());
		assertEquals(1, philosophers[2].getEatings());
		assertEquals(1, philosophers[3].getEatings());
		
	}
	
	@Test
	public void testFivePhilosophers() throws Exception {
		new Thread(philosophers[0]).start();
		new Thread(philosophers[1]).start();
		Thread.sleep(100);
		philosophers[0].stop();
		philosophers[1].stop();
		new Thread(philosophers[2]).start();
		new Thread(philosophers[3]).start();
		Thread.sleep(100);
		philosophers[2].stop();
		philosophers[3].stop();
		Thread.sleep(5000);
		assertEquals(1, philosophers[0].getEatings());
		assertEquals(1, philosophers[1].getEatings());
		assertEquals(1, philosophers[2].getEatings());
		assertEquals(1, philosophers[3].getEatings());
		PhilosopherImplementation p = new PhilosopherImplementation();
		p.setTable(t);
		new Thread(p).start();
		Thread.sleep(100);
		p.stop();
		Thread.sleep(2000);
		assertEquals(1, p.getEatings());
	}
	
	@Test
	public void testManyPhilosophersAndLessSeats() throws RemoteException, InterruptedException {
		for(int i = 0; i < 20; i++) {
			PhilosopherImplementation p = new PhilosopherImplementation();
			p.setTable(t);
			new Thread(p).start();
		}
		Thread.sleep(10000);
	}
}
