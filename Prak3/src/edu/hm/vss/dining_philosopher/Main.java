package edu.hm.vss.dining_philosopher;

public class Main{

	public static void main(String[] args) {

		final int seats = 30;
		final int totalPhilosphers = 50;
		final int hungryPhilosophers = 10;
		final int maximumMealDifference = 10;

		final int normalMeditationDuration = 50;
		final int hungryMeditationDuration = 10;
		final int eatDuration = 10;

		Philosopher[] philosopher = new Philosopher[totalPhilosphers];
		DinnerGuardian dinngerGuardian = new DinnerGuardian(seats,
				maximumMealDifference);

		for (int i = 0; i < totalPhilosphers - hungryPhilosophers; i++) {
			philosopher[i] = new Philosopher(eatDuration,
					normalMeditationDuration, dinngerGuardian);
			philosopher[i].start();
		}
		for (int i = totalPhilosphers - hungryPhilosophers; i < totalPhilosphers; i++) {
			philosopher[i] = new Philosopher(eatDuration,
					hungryMeditationDuration, dinngerGuardian);
			philosopher[i].start();
		}
	}

}
