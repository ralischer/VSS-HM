package edu.hm.vss.dining_philosopher;

import java.util.ArrayList;
import java.util.Set;

/*
 * This class represents a philosopher, who can go to eat and go to meditate. 
 * The duration of eating and meditation can be specified
 * 
 */
public class Philosopher extends Thread {

	private int meditationDuration;
	private int eatDuration;
	private int maxEatingDiscrepance;
	private final int GREEDY_PENALTY = 10;
	private final int ID;
	private static int idCounter = 0;
	private final static Object PHILOSOPHER_LOCK = new Object();
	private int numberEatings = 0;
	private final Table table;

	public Philosopher(int eatDuration, int meditationDuration,
			int maxEatingDiscrepance, Table t) {
		this.meditationDuration = meditationDuration;
		this.eatDuration = eatDuration;
		this.ID = idCounter++;
		this.table = t;
		this.maxEatingDiscrepance = maxEatingDiscrepance;
	}

	public void setSleepDuration(int sleepDuration) {
		this.meditationDuration = sleepDuration;
	}

	public void setEatDuration(int eatDuration) {
		this.eatDuration = eatDuration;
	}

	public int getEatings() {
		return numberEatings;
	}

	@Override
	public void run() {

		// eat and meditate forever
		while (true) {
			boolean greedyWait = false;
			long timestamp = System.currentTimeMillis();
			while (hasToWait()) {
				greedyWait = true;
				try {
					Thread.sleep(GREEDY_PENALTY);
				} catch (InterruptedException e) {
					outputStatus("unexpected interrupt while penalty sleeping");
					e.printStackTrace();
				}
			}
			if (greedyWait) {
				outputStatus(
						"had to wait for %d msec because i am greedy !!!!",
						(System.currentTimeMillis() - timestamp));
			}
			int seatNr = getSeat();
			Object[] forks = null;
			try {
				forks = table.waitForSeat(seatNr, this);
			} catch (InterruptedException e) {
				outputStatus("unexpected interrupt while waiting for a seat");
				e.printStackTrace();
			}
			try {
				synchronized (forks[0]) {
					synchronized (forks[1]) {
						outputStatus("nom nom nom  (eating) at seat ID %d ",
								seatNr);
						Thread.sleep(this.eatDuration);
						outputStatus("that was tasty ! now i need to meditate (finished eating)");
					}
				}
			} catch (InterruptedException e) {
				outputStatus("unexpected interrupt while eating");
				e.printStackTrace();
			}
			numberEatings++;
			table.leaveSeat(seatNr);
			try {
				Thread.sleep(meditationDuration);
			} catch (InterruptedException e) {
				outputStatus("unexpected interrupt while meditating");
			}
		}
	}

	private boolean hasToWait() {
		Set<Philosopher> philosophers = table.getPhilosophers();
		int lowestEatAmount = Integer.MAX_VALUE;
		for (Philosopher p : philosophers) {
			int pEating = p.getEatings();
			if (pEating < lowestEatAmount) {
				lowestEatAmount = pEating;
			}
		}
		return this.getEatings() > lowestEatAmount + maxEatingDiscrepance;
	}

	private int getSeat() {
		int minAmount = Integer.MAX_VALUE;
		ArrayList<Integer> bestSeats = new ArrayList<>();
		int[] seatOccupancy = new int[table.size()];
		for (int i = 0; i < table.size(); i++) {
			seatOccupancy[i] = table.getWaitingPhilosophers(i);
			if (seatOccupancy[i] < minAmount) {
				bestSeats.clear();
				minAmount = seatOccupancy[i];
				bestSeats.add(i);
			} else if (seatOccupancy[i] == minAmount) {
				bestSeats.add(i);
			}
		}
		// only 1 optimal Seat found
		if (bestSeats.size() == 1) {
			return bestSeats.get(0);
		}

		int lowestHeuristic = Integer.MAX_VALUE;
		int bestSeatID = -1;

		for (int i = 0; i < bestSeats.size(); i++) {
			int currentSeat = bestSeats.get(i);

			// heuristic value : number of people waiting at the left and right
			// seat
			int heuristic = seatOccupancy[(i + 1) % seatOccupancy.length]
					+ seatOccupancy[(i - 1 + seatOccupancy.length)
							% seatOccupancy.length];
			if (heuristic < lowestHeuristic) {
				lowestHeuristic = heuristic;
				bestSeatID = currentSeat;
			}
		}
		return bestSeatID;
	}

	/*
	 * Synchronized console output of status messages
	 */
	public void outputStatus(String status, Object... args) {
		synchronized (PHILOSOPHER_LOCK) {
			System.out.printf("ID %2d : \t", ID);
			System.out.printf(status + "\n", args);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Philosopher other = (Philosopher) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

}
