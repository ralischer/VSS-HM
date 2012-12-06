package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;
import edu.hm.vss.prak.diningphilosophersrmi.util.Job;
import edu.hm.vss.prak.diningphilosophersrmi.util.Pair;

public class TableImplementation implements Table, Runnable {

	private static final Object SYNC_MONITOR = new Object();
	List<Philosopher> philosophers = new LinkedList<Philosopher>();
	List<Seat> unusedSeats = new LinkedList<Seat>();
	List<Fork> unusedForks = new LinkedList<Fork>();
	List<Seat> usableSeats = new LinkedList<Seat>();
	List<Fork> usedForks = new LinkedList<Fork>();

	TreeMap<Seat, Integer> suggestions = new TreeMap<Seat, Integer>(
			new SeatComparator());

	HashMap<Seat, Pair<Fork>> temp = new HashMap<Seat, Pair<Fork>>();

	@Override
	public void run() {
		// TODO heart-beat for clients...

	}

	@Override
	public Seat getBestSeat() throws RemoteException {
		System.out.println("get best seat requested");
		synchronized (suggestions) {
			return suggestions.firstKey();
		}
	}

	@Override
	public int getSeatAmount() throws RemoteException {
		return usableSeats.size();
	}

	@Override
	public void registerNewSeat(Seat s) throws RemoteException {
		Seat seatWithUnsharedFork = null;
		Seat nextSeat = null;
		int index = 0;
		for (int i = 0; i < usableSeats.size(); i++) {
			Seat currentSeat = usableSeats.get(i);
			if (!currentSeat.getForks()[1].isShared()) {
				seatWithUnsharedFork = currentSeat;
				index = i;
				break;
			}
		}
		if (seatWithUnsharedFork != null) {
			nextSeat = index == usableSeats.size() - 1 ? usableSeats.get(0)
					: usableSeats.get(index + 1);
			Fork left = seatWithUnsharedFork.getForks()[1];
			Fork right = nextSeat.getForks()[0];
			left.incrementUsageNumber();
			right.incrementUsageNumber();
			s.setForks(left, right);
			Pair<Fork> pair = new Pair<Fork>(left, right);
			temp.put(s, pair);
			usableSeats.add(index + 1, s);
			return;
		}

		if (unusedForks.size() > 1 && usableSeats.isEmpty()) {
			Fork left = unusedForks.remove(0);
			Fork right = unusedForks.remove(0);
			left.incrementUsageNumber();
			right.incrementUsageNumber();
			s.setForks(left, right);
			usableSeats.add(s);
			if (!unusedSeats.isEmpty())
				registerNewSeat(unusedSeats.remove(0));
		}
		// we are only adding new seats if there are forks available
		if (unusedForks.size() > 0 && !usableSeats.isEmpty()) {
			// if unused forks is greater than zero all current seats use 2
			// forks
			Fork f = unusedForks.remove(0);
			Fork lastFork = usableSeats.get(0).getForks()[1];
			lastFork.incrementUsageNumber();
			f.incrementUsageNumber();
			s.setForks(lastFork, f);
			usableSeats.add(s);
		} else
			unusedSeats.add(s);
	}

	@Override
	public void removeSeat(Seat s) throws RemoteException {
		// TODO implement this
		if (unusedSeats.contains(s)) {
			unusedSeats.remove(s);
			return;
		}

	}

	@Override
	public void registerNewFork(Fork f) throws RemoteException {
		Seat seat = null;
		for (Seat s : usableSeats) {
			if (s.getForks()[0].isShared() || s.getForks()[1].isShared()) {
				seat = s;
				break;
			}
		}
		if (seat != null) {
			Fork sharedFork;
			Fork otherFork;
			int sharedForkIndex;
			if (seat.getForks()[0].isShared()) {
				sharedFork = seat.getForks()[0];
				otherFork = seat.getForks()[1];
				sharedForkIndex = 0;
			} else {
				sharedFork = seat.getForks()[1];
				otherFork = seat.getForks()[0];
				sharedForkIndex = 1;
			}
			sharedFork.decrementUsageNumber();
			// TODO: check
			usedForks.add(f);
			f.incrementUsageNumber();
			if (sharedForkIndex == 0)
				seat.setForks(f, otherFork);
			else
				seat.setForks(otherFork, f);
		} else if (!unusedSeats.isEmpty() && !usableSeats.isEmpty()) {
			// for all seats are 2 forks available so we can grab the last seats
			// fork for sharing and adding a new seat
			Seat unsuedSeat = unusedSeats.remove(0);
			Fork lastFork = usableSeats.get(0).getForks()[1];
			lastFork.incrementUsageNumber();
			f.incrementUsageNumber();
			unsuedSeat.setForks(lastFork, f);
			usableSeats.add(unsuedSeat);
		} else {
			// no seats with a shared fork or any unused seats
			unusedForks.add(f);
			if (!unusedSeats.isEmpty())
				registerNewSeat(unusedSeats.remove(0));
		}
	}

	@Override
	public void removeFork(Fork f) throws RemoteException {
		// TODO: implement this
		if (unusedForks.contains(f)) {
			unusedForks.remove(f);
			return;
		}
	}

	@Override
	public void findNewSeat(Philosopher p) throws RemoteException {
		getBestSeat().waitForSeat(p);
	}

	@Override
	public void updateQueueSize(Seat s, int waitingPhilosophers)
			throws RemoteException {
		System.err.println("updating suggestions");
		synchronized (suggestions) {
			Iterator<Seat> i = suggestions.keySet().iterator();
			while (i.hasNext()) {
				Seat curr = i.next();
				if (curr.hashCode() == s.hashCode())
					i.remove();
			}
			suggestions.put(s, waitingPhilosophers);
			System.err.println("new best "+suggestions.firstKey());
		}

	}

	@Override
	public void readyToSync(Seat s) throws RemoteException {
		System.out.println(s + " readyToSync");
		Job job = link.get(s);
		if (job != null) {
			link.remove(s);
			job.setSeatReady(s);
			if (job.isReady()) {
				System.out.println(s + " job ready");
				addNewSeatAndFork(job);
			}
		}
	}

	private void addNewSeatAndFork(final Job job) {
		new Thread() {
			public void run() {

				if (jobs.remove(job)) {
					try {
						Seat first = job.getSeats()[0];
						Seat last = job.getSeats()[1];
						last.setLast(false);
						Fork oldLast = last.getRightFork();
						Seat newSeat = job.getNewSeat();
						Fork newFork = job.getNewFork();
						newSeat.setForks(oldLast, newFork);
						newSeat.setLast(true);
						first.setForks(newFork, first.getRightFork());
						usableSeats.add(newSeat);
						System.out.println("job finished");
						first.continueAfterSync();
						last.continueAfterSync();
						newSeat.continueAfterSync();
					} catch (RemoteException e) {
					} finally {
						// wartenden sync freigeben
						syncing = false;
						synchronized (SYNC_MONITOR) {
							SYNC_MONITOR.notify();
						}
					}
				}
			}
		}.start();
	}

	private Set<Job> jobs = new HashSet<Job>();
	private HashMap<Seat, Job> link = new HashMap<Seat, Job>();
	private boolean syncing;

	// TODO: wenn ein schnell zwei seats erstellt werden könnten probleme
	// auftreten ...
	@Override
	public void registerNewSeatAndFork(Seat s, Fork f) throws RemoteException {
		// TODO: sync nur wenn ein sync abgeschlossen ist ... :-)
		if(usableSeats.isEmpty()) {
			if(unusedForks.isEmpty()) {
				unusedForks.add(f);
				unusedSeats.add(s);
			} else {
				Seat firstSeat = unusedSeats.remove(0);
				Fork firstFork = unusedForks.remove(0);
				firstSeat.setForks(f, firstFork);
				s.setForks(firstFork, f);
				s.setLast(true);
				usableSeats.add(firstSeat);
				usableSeats.add(s);
				firstSeat.continueAfterSync();
				s.continueAfterSync();
			}
			return;
		}
		while (syncing) {
			synchronized (SYNC_MONITOR) {
				try {
					System.out.println("waiting to sync complete...");
					SYNC_MONITOR.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("registering new seat and fork");
		synchronized (SYNC_MONITOR) {

			final Seat firstSeat = usableSeats.get(0);
			final Seat lastSeat = usableSeats.get(usableSeats.size() - 1);
			Job job = new Job(firstSeat, lastSeat, s, f);
			jobs.add(job);
			link.put(firstSeat, job);
			link.put(lastSeat, job);
			firstSeat.pauseForSync();
			lastSeat.pauseForSync();
			s.pauseForSync();
			syncing = true;
		}
	}

	@Override
	public void removeSeatAndFork(Seat s, Fork f) throws RemoteException {

	}

}
