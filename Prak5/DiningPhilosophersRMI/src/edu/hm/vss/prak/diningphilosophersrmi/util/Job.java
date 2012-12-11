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
	private final Seat targetSeat;
	private final Fork targetFork;
	private final String hostname;
	private final JobType type;
	
	public Job(JobType type, Seat next, Seat prev, Seat targetSeat, Fork targetFork, String hostname) {
		seats[0] = next;
		seats[1] = prev;
		this.targetSeat = targetSeat;
		this.targetFork = targetFork;
		this.hostname = hostname;
		this.type = type;
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
	
	public Seat getTargetSeat() {
		return targetSeat;
	}
	
	public Fork getTargetFork() {
		return targetFork;
	}
	
	public String getHost() {
		return hostname;
	}
	
	public JobType getType() {
		return type;
	}
}
