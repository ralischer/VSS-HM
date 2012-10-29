package edu.hm.vss.dining_philosopher;

import java.util.Collections;
import java.util.List;

/*
 * This class represents a philosopher, who can go to eat and go to meditate. 
 * The duration of eating and meditation can be specified
 * 
 */
public class Philosopher extends Thread{

	private int						meditationDuration;
	private int						eatDuration;
	private final DinnerGuardian	controller;
	private final int				id;
	private static int				idCounter			= 0;
	private final static Object		PHILOSOPHER_LOCK	= new Object();

	public Philosopher(int eatDuration, int meditationDuration,
			DinnerGuardian controller) {
		this.meditationDuration = meditationDuration;
		this.eatDuration = eatDuration;
		this.controller = controller;
		this.id = idCounter++;
	}

	public void setSleepDuration(int sleepDuration) {
		this.meditationDuration = sleepDuration;
	}

	public void setEatDuration(int eatDuration) {
		this.eatDuration = eatDuration;
	}

	@Override
	public void run() {

		// eat and meditate forever
		while (true) {

			// enter dining room
			synchronized (controller) {
				while (!controller.enterDiningRoom(this)) {
					try {

						// wait until an other philosopher leaves the table,
						// then try again to enter
						controller.wait();
					}
					catch (InterruptedException e) {
						outputStatus("unexpected interrupt while waiting for a seat");
						System.exit(-1);
					}
				}

			}

			// get a free seat
			Object[] forks = null;
			int seatId = -1;
			while (forks == null) {
				List<Integer> freeSeats = controller.getFreeSeats();
				Collections.shuffle(freeSeats);
				for (Integer testSeat : freeSeats) {

					// sit down if seat hasn't been occupied by another
					// philosopher
					forks = controller.getForks(testSeat, this);
					if (forks != null) {
						seatId = testSeat;
						outputStatus("took place at seat id %d", seatId);
						break;
					}
				}
			}

			// eat
			try {
				eat(forks[0], forks[1], eatDuration);
			}
			catch (InterruptedException e1) {
				outputStatus("unexpected interrupt while eating");
				System.exit(-1);
			}

			// meditate
			controller.leaveDiningRoom(seatId, this);
			try {
				Thread.sleep(meditationDuration);
			}
			catch (InterruptedException e) {
				outputStatus("unexpected interrupt while meditating");
				System.exit(-1);
			}
		}
	}

	/*
	 * Synchronized console output of status messages
	 */
	public void outputStatus(String status, Object... args) {
		synchronized (PHILOSOPHER_LOCK) {
			System.out.printf("ID %2d : \t", id);
			System.out.printf(status + "\n", args);
		}
	}

	/*
	 * get an exclusive handle for the left and right fork, then eat and
	 * implicitly release the forks afterward
	 */
	private void eat(Object leftFork, Object rightFork, int eatTime)
			throws InterruptedException {
		synchronized (leftFork) {
			synchronized (rightFork) {
				outputStatus("nom nom nom  (eating)");
				Thread.sleep(eatTime);
				outputStatus("that was tasty ! now i need to meditate (finished eating)");
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}

}
