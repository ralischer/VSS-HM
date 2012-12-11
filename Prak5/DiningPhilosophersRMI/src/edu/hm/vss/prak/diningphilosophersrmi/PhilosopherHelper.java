package edu.hm.vss.prak.diningphilosophersrmi;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import edu.hm.vss.prak.diningphilosophersrmi.graphical.Viewer;
import edu.hm.vss.prak.diningphilosophersrmi.implementations.PhilosopherImplementation;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class PhilosopherHelper {

	private final Table table;
	private Viewer viewer;
	private List<Philosopher> philosophers = new LinkedList<Philosopher>();
	
	public PhilosopherHelper(Table table) {
		this.table = table;
	}
	
	public synchronized void setViewer(Viewer viewer) {
		this.viewer = viewer;
	}
	
	public synchronized void addNewPhilosopher(int amount) throws RemoteException {
		if(amount < 1) {
			throw new IllegalArgumentException("amount must be positive");
		}
		for(int i = 0; i < amount; i++) {
			PhilosopherImplementation philosopher = new PhilosopherImplementation();
			philosopher.setTable(table);
			if(viewer != null) {
				philosopher.setViewer(viewer);
			}
			new Thread(philosopher).start();
			philosophers.add(philosopher);
		}
	}
	
	public synchronized void stopPhilosophers(int amount) throws RemoteException {
		if(amount > philosophers.size() || amount < 1) {
			return;
		}
		for(int i = 0; i < amount; i++) {
			Philosopher p = philosophers.remove(0);
			p.stop();
		}
	}
}
