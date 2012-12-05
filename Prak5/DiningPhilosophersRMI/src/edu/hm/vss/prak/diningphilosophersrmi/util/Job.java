package edu.hm.vss.prak.diningphilosophersrmi.util;

import java.io.Serializable;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;

public class Job implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1357160966089290992L;
	
	private int seatsReady = 0;
	private final Seat[] seats = new Seat[2];
	private final Seat newSeat;
	private final Fork newFork;
	
	public Job(Seat first, Seat second, Seat newSeat, Fork newFork) {
		seats[0] = first;
		seats[1] = second;
		this.newSeat = newSeat;
		this.newFork = newFork;
	}
	
	public void setSeatReady(Seat s) {
		if(seats[0].equals(s) || seats[1].equals(s)) {
			seatsReady++;
		}
	}
	
	public boolean isReady() {
		return seatsReady >= 2;
	}
	
	public Seat[] getSeats() {
		return seats;
	}
	
	public Seat getNewSeat() {
		return newSeat;
	}
	
	public Fork getNewFork() {
		return newFork;
	}
}