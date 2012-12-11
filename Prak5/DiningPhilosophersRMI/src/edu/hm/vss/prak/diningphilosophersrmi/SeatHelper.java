package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import edu.hm.vss.prak.diningphilosophersrmi.implementations.ForkImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.SeatImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Fork;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class SeatHelper {

	private static final String hostname = System.getProperty("java.rmi.server.hostname");
	
	private List<Seat> seats = new LinkedList<Seat>();
	private List<Fork> forks = new LinkedList<Fork>();
	private final Table table;
	
	public SeatHelper(Table table) {
		if(table == null) 
			throw new IllegalArgumentException("table must not be null!");
		this.table = table;
	}
	
	public synchronized void createSeats(int amount) throws RemoteException {
		for(int i = 0; i < amount; i++) {
			SeatImplementation s = new SeatImplementation();
			ForkImplementation f = new ForkImplementation();
			s.setTable(table);
			new Thread(s).start();
			table.registerNewSeatAndFork(s, f, hostname);
			seats.add(s);
			forks.add(f);
		}
	}
	
	public synchronized void removeSeats(int amount) throws RemoteException {
		if(seats.size() == 0 || seats.size() - amount < 0) {
			return;
		}
		for(int i = seats.size()-1; i >= seats.size()-amount; i--) {
			Seat s = seats.remove(i);
			Fork f = forks.remove(i);
			table.removeSeatAndFork(s, f, hostname);
		}
	}
	
}
