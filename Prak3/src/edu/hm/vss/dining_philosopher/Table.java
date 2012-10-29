package edu.hm.vss.dining_philosopher;

/*
 * A class to represent a table with a specified amount of forks
 */
public class Table{

	private Object[]	forks;

	public Table(int seats) {
		forks = new Object[seats];
		for (int i = 0; i < seats; i++) {
			forks[i] = new Object();
		}
	}

	public final Object getFork(int number) {
		return forks[number];
	}

	public int size() {
		return forks.length;
	}

}
