package edu.hm.vss.prak.diningphilosophersrmi.graphical;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ViewerImplementation extends UnicastRemoteObject implements Viewer, Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7199958309819926473L;
	
	protected ViewerImplementation() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private final BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();

	@Override
	public void addEvent(Event event) throws RemoteException, InterruptedException {
		events.put(event);
	}

	long lastUpdate = 0;
	
	@Override
	public void run() {
		lastUpdate = System.currentTimeMillis();
		while(true) {
			try {
				Event event = events.take();
				philosophers.put(event.getIdentifier(), event);
				//do this max 60 times/s
				//TODO: in extra thread auslagern ... 
				//if(System.currentTimeMillis() - lastUpdate > 16) {
					print();
					//lastUpdate = System.currentTimeMillis();
				//}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private String header = String.format("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n" +
			"|\tPhilosopher\t\t|\tState\t\t\t|\tTarget\t|%n");
	
	private Map<String, Event> philosophers = new HashMap<String, Event>();
	
	private final String outputFormat = "|\t%s\t|\t%s\t\t|\t%s\t|%n";
	
	private void print() throws RemoteException {
		System.out.printf(header);
		for(Map.Entry<String, Event> entry: philosophers.entrySet()) {
			System.out.printf(outputFormat, entry.getKey(),entry.getValue().getState().toString(),entry.getValue().getTarget());
		}
	}
	
	public static void main(String... args) throws NumberFormatException, RemoteException {
		Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
		ViewerImplementation vi = new ViewerImplementation();
		new Thread(vi).start();
		registry.rebind("viewer", vi);
	}
	
}
