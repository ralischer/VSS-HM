package edu.hm.vss.dining_philosopher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
 * A class to represent a table with a specified amount of forks
 */
public class Table {

	private Seat[] seats;
	private HashSet<Philosopher> philosophers;

	private Object[] forks;

	public Table(int seats, HashSet<Philosopher> philosophers) {
		forks = new Object[seats];
		this.philosophers = philosophers;

		// one reserved seat for the owner who never shows up
		// since he is on a very long vacation 
		// AKA deadlock-free :)
		this.seats = new Seat[seats - 1];
		for (int i = 0; i < forks.length; i++) {
			forks[i] = new Object();
		}
		for (int i = 0; i < this.seats.length; i++) {
			this.seats[i] = new Seat();
		}
	}

	public Set<Philosopher> getPhilosophers() {
		return Collections.unmodifiableSet(philosophers);
	}

	public final Object[] waitForSeat(int number, Philosopher p)
			throws InterruptedException {
		seats[number].register(p);
		Object token = null;
		while (token == null) {
			token = seats[number].tryAndSit(p);
		}
		return new Object[] { forks[number], forks[number + 1] };
	}

	public void leaveSeat(int number) {
		seats[number].finishEating();
	}

	public int getWaitingPhilosophers(int seatPosition) {
		return seats[seatPosition].queueSize();
	}

	public int size() {
		return seats.length;
	}
	
}
