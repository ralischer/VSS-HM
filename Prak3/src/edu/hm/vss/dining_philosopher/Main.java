package edu.hm.vss.dining_philosopher;

import java.util.HashSet;

public class Main {

	public static void main(String[] args) {

		final int seats = 5; // minimum 2 seats required, since 1 seat is
								// reserved
		final int totalPhilosphers = 30;
		final int hungryPhilosophers = 1;
		final int maximumMealDifference = 1;

		final int normalMeditationDuration = 1000;
		final int hungryMeditationDuration = 0;
		final int eatDuration = 30;

		HashSet<Philosopher> philosophers = new HashSet<>();
		Table table = new Table(seats, philosophers);

		for (int i = 0; i < totalPhilosphers - hungryPhilosophers; i++) {
			Philosopher p = new Philosopher(eatDuration,
					normalMeditationDuration, maximumMealDifference, table);
			philosophers.add(p);
		}
		for (int i = totalPhilosphers - hungryPhilosophers; i < totalPhilosphers; i++) {
			Philosopher p = new Philosopher(eatDuration,
					hungryMeditationDuration, maximumMealDifference, table);
			philosophers.add(p);
		}
		for (Philosopher p : philosophers) {
			p.start();
		}
	}
}
