package edu.hm.vss.dining_philosopher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class DinnerGuardian {

	private final Table table;
	private final boolean[] seatInUse;
	private final HashSet<Philosopher> activeDiningPhilosophers;
	private final int maximumDiningTreshold;

	private TreeMap<Integer, HashSet<Philosopher>> sortedDiningCounter;
	private HashMap<Philosopher, Integer> diningCounter;

	public DinnerGuardian(int seats, int maximumDiningTreshold) {
		this.table = new Table(seats);
		seatInUse = new boolean[table.size()];
		activeDiningPhilosophers = new HashSet<>();
		sortedDiningCounter = new TreeMap<>();
		diningCounter = new HashMap<>();
		this.maximumDiningTreshold = maximumDiningTreshold;

	}

	public boolean enterDiningRoom(Philosopher philosopher) {
		synchronized (this) {
			// table.size()-1 --> one seat remains free so a deadlock can't
			// happen
			if (activeDiningPhilosophers.size() >= table.size() - 1) {
				return false;
			}
			// greedy philosophers shall not pass
			if (diningCounter.containsKey(philosopher)
					&& (sortedDiningCounter.firstKey() + maximumDiningTreshold < diningCounter
							.get(philosopher))) {
				philosopher.outputStatus("greedy philosophers have to wait !");
				return false;
			}
			return activeDiningPhilosophers.add(philosopher);
		}

	}

	public Object[] getForks(int seatID, Philosopher philosopher) {
		if (seatID < 0 || seatID >= table.size()
				|| !activeDiningPhilosophers.contains(philosopher))
			return null;
		synchronized (seatInUse) {
			// seatID must be a free seat
			if (seatInUse[seatID]) {
				return null;
			}
			seatInUse[seatID] = true;
		}

		// 2 forks as result
		Object[] result = new Object[2];
		result[0] = table.getFork(seatID);
		result[1] = table.getFork((seatID + 1) % table.size());
		return result;

	}

	public void leaveDiningRoom(int seatID, Philosopher philosopher) {
		synchronized (seatInUse) {
			seatInUse[seatID] = false;
		}
		synchronized (this) {
			activeDiningPhilosophers.remove(philosopher);
			increaseMealCount(philosopher);
			this.notifyAll();
		}
	}

	private void increaseMealCount(Philosopher philosopher) {
		int meals = 0;
		if (diningCounter.containsKey(philosopher)) {
			meals = diningCounter.get(philosopher);
			HashSet<Philosopher> tmp = sortedDiningCounter.get(meals);
			tmp.remove(philosopher);
			if (tmp.size() == 0) {
				sortedDiningCounter.remove(meals);
			}
		}
		meals++;
		if (!sortedDiningCounter.containsKey(meals)) {
			sortedDiningCounter.put(meals, new HashSet<Philosopher>());
		}
		sortedDiningCounter.get(meals).add(philosopher);
		diningCounter.put(philosopher, meals);
	}

	public List<Integer> getFreeSeats() {
		LinkedList<Integer> result = new LinkedList<>();
		synchronized (seatInUse) {
			for (int i = 0; i < seatInUse.length; i++) {
				if (!seatInUse[i]) {
					result.push(i);
				}
			}
		}
		return result;
	}
}
