package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;
import edu.hm.vss.prak.diningphilosophersrmi.util.Job;
import edu.hm.vss.prak.diningphilosophersrmi.util.JobType;

public class TableImplementation implements Table, Runnable {

	private static final Object SYNC_MONITOR = new Object();
	private List<Seat> unusedSeats = new LinkedList<Seat>();
	private List<Fork> unusedForks = new LinkedList<Fork>();
	private List<Seat> usableSeats = new LinkedList<Seat>();

	@Override
	public void run() {
		// TODO heart-beat for clients...

	}

	@Override
	public Seat getBestSeat(String host) throws RemoteException {
		synchronized (sug) {
			List<Seat> seats = sug.get(host);
			if (seats == null) {
				synchronized (usableSeats) {
					return usableSeats.get(0);
				}
			} else {
				return seats.get(0);
			}
		}
	}

	@Override
	public int getSeatAmount() throws RemoteException {
		synchronized (usableSeats) {
			return usableSeats.size();
		}
	}

	@Override
	public void findNewSeat(Philosopher p) throws RemoteException {
		//just give them the first seat....
		getBestSeat("null").waitForSeat(p);
	}

	@Override
	public void readyToSync(Seat s) throws RemoteException {
		System.out.println(s + " readyToSync");
		Job job = null;
		synchronized(link) {
			job = link.get(s);
		}
		if (job != null) {
			synchronized(link) {
				link.remove(s);
			}
			job.setSeatReady(s);
			switch (job.getType()) {
			case ADD:
				if (job.isReady()) {
					System.out.println(s + " job ready");
					addNewSeatAndFork(job);
				}
				break;
			case REMOVE:
				boolean jobNotReady;
				synchronized(link) {
					jobNotReady = link.containsValue(job);
				}
				if (!jobNotReady) {
					remove(job);
				}
				break;
			default:
				break;
			}
		}
	}

	private void remove(final Job job) {
		new Thread() {
			public void run() {
				if (jobs.remove(job)) {
					try {
						Seat prev = job.getSeats()[0];
						Seat next = job.getSeats()[1];
						Seat toRemove = job.getTargetSeat();
						Collection<Philosopher> waitingPhilosophers = toRemove
								.getWaitingPhilosophers();
						for (Philosopher p : waitingPhilosophers) {
							findNewSeat(p);
						}
						int prevIndex = usableSeats.indexOf(prev);
						int nextIndex = usableSeats.indexOf(next);
						if (prevIndex == usableSeats.size() - 1) {
							prev.setLast(false);
						}
						if (nextIndex == usableSeats.size() - 1) {
							next.setLast(false);
						}
						synchronized (usableSeats) {
							usableSeats.remove(toRemove);
						}
						synchronized (sug) {
							List<Seat> hostlist = sug.get(job.getHost());
							hostlist.remove(toRemove);
							sug.put(job.getHost(), hostlist);
						}
						next.setForks(prev.getLeftFork(), next.getRightFork());
						prev.setNextSeat(next);
						next.setPrevious(prev);
						prevIndex = usableSeats.indexOf(prev);
						nextIndex = usableSeats.indexOf(next);
						if (prevIndex == usableSeats.size() - 1) {
							prev.setLast(true);
						}
						if (nextIndex == usableSeats.size() - 1) {
							next.setLast(true);
						}
						prev.continueAfterSync();
						next.continueAfterSync();
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

	private void addNewSeatAndFork(final Job job) {
		new Thread() {
			public void run() {

				if (jobs.remove(job)) {
					try {
						Seat next = job.getSeats()[0];
						Seat prev = job.getSeats()[1];
						System.out.println("Next: "+next.getIdentitifier());
						System.out.println("Prev: "+prev.getIdentitifier());
						synchronized(usableSeats) {
							int nextIndex = usableSeats.indexOf(next);
							int prevIndex = usableSeats.indexOf(prev);
							if(prevIndex == usableSeats.size()-1) {
								next.setLast(false);
							}
							if(nextIndex == usableSeats.size()-1) {
								prev.setLast(false);
							}
						}
						//next.setLast(false);
						Fork prevRightFork = prev.getRightFork();
						Seat newSeat = job.getTargetSeat();
						Fork newFork = job.getTargetFork();
						newSeat.setForks(prevRightFork, newFork);
						
						//newSeat.setLast(true);
						next.setForks(newFork, next.getRightFork());
						prev.setNextSeat(newSeat);
						newSeat.setNextSeat(next);
						newSeat.setPrevious(prev);
						next.setPrevious(newSeat);
						synchronized (usableSeats) {
							int prevIndex = usableSeats.indexOf(prev);
							usableSeats.add(prevIndex+1,newSeat);
							int newIndex = usableSeats.indexOf(newSeat);
							int nextIndex = usableSeats.indexOf(next);
							if(newIndex == usableSeats.size()-1) {
								newSeat.setLast(true);
							}
							if(nextIndex == usableSeats.size()-1) {
								next.setLast(true);
							}
						}

						List<Seat> hostList = null;
						synchronized (sug) {
							sug.get(job.getHost());
						}
						if (hostList == null) {
							hostList = new LinkedList<Seat>();
						}
						hostList.add(newSeat);
						synchronized (sug) {
							sug.put(job.getHost(), hostList);
						}
						System.out.println("job finished");
						next.continueAfterSync();
						prev.continueAfterSync();
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
	private HashMap<String, List<Seat>> sug = new HashMap<String, List<Seat>>();

	@Override
	public void registerNewSeatAndFork(Seat s, Fork f, String host)
			throws RemoteException {
		System.out.println("registering new seat and fork from host: " + host);
		synchronized (usableSeats) {
			if (usableSeats.isEmpty()) {
				if (unusedForks.isEmpty()) {
					unusedForks.add(f);
					unusedSeats.add(s);
				} else {
					Seat firstSeat = unusedSeats.remove(0);
					Fork firstFork = unusedForks.remove(0);
					firstSeat.setForks(f, firstFork);
					s.setForks(firstFork, f);
					s.setLast(true);
					firstSeat.setNextSeat(s);
					s.setNextSeat(firstSeat);
					firstSeat.setPrevious(s);
					s.setPrevious(firstSeat);
					synchronized (usableSeats) {
						usableSeats.add(firstSeat);
						usableSeats.add(s);
					}
					List<Seat> hostList = null;
					synchronized (sug) {
						hostList = sug.get(host);
					}
					if (hostList == null) {
						hostList = new LinkedList<Seat>();
					}
					hostList.add(firstSeat);
					hostList.add(s);
					synchronized (sug) {
						sug.put(host, hostList);
					}
					firstSeat.continueAfterSync();
					s.continueAfterSync();
				}
				return;
			}
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

			Seat firstSeat = usableSeats.get(0);
			Seat lastSeat = usableSeats.get(usableSeats.size() - 1);
			List<Seat> hostList = null;
			synchronized (sug) {
				hostList = sug.get(host);
			}
			//falls es bereits lokale Plätze gibt dann füge die neuen dor ein ..
			if(hostList != null) {
				lastSeat = hostList.get(hostList.size()-1);
				firstSeat = lastSeat.getNextSeat();
			}
			
			Job job = new Job(JobType.ADD, firstSeat, lastSeat, s, f, host);
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
	public void removeSeatAndFork(Seat s, Fork f, String host) throws RemoteException {
		synchronized (usableSeats) {
			if (!usableSeats.contains(s)) {
				return;
			}
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
		System.out.println("removing seat and fork");
		synchronized (SYNC_MONITOR) {
			s.pauseForSync();
			s.getNextSeat().pauseForSync();
			s.getPrevious().pauseForSync();
			Job job = new Job(JobType.REMOVE, s.getPrevious(), s.getNextSeat(),
					s, f, host);
			jobs.add(job);
			link.put(s.getPrevious(), job);
			link.put(s.getNextSeat(), job);
			link.put(s, job);
			syncing = true;
		}

	}

	@Override
	public void printUsableSeats() throws RemoteException {
		synchronized (usableSeats) {
			Seat start = usableSeats.get(0);
			System.out.println(start.getIdentitifier());
			Seat current = start.getNextSeat();
			while(!current.getIdentitifier().equals(start.getIdentitifier())) {
				System.out.println(current.getIdentitifier());
				current = current.getNextSeat();
			}
		}
		
	}

}
