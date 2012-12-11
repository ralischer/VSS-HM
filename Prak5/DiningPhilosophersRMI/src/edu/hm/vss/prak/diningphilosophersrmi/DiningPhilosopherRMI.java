package edu.hm.vss.prak.diningphilosophersrmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;

import edu.hm.vss.prak.diningphilosophersrmi.graphical.Viewer;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class DiningPhilosopherRMI {

	public static void main(String... args) throws Exception {
		SeatHelper seatHelper = null;
		PhilosopherHelper philosopherHelper = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input;
		Table table = null;
		while (!(input = br.readLine()).equals("exit")) {
			if (input.equals("create Table")) {
				String port;
				System.out.print("Which port? ");
				System.out.flush();
				port = br.readLine();
				TableMain.main(port);
				table = (Table) LocateRegistry.getRegistry(Integer.parseInt(port)).lookup("table");
			} else if (input.equals("create Philosopher")) {
				if(philosopherHelper == null) {
					if(table == null) {
						table = getTable(br);
					}
					philosopherHelper = new PhilosopherHelper(table);
					String viewerAvailable;
					System.out.print("Viewer available? (J/n)");
					System.out.flush();
					viewerAvailable = br.readLine();
					String viewerHost;
					String viewerPort;
					if(viewerAvailable.isEmpty() || viewerAvailable.toUpperCase().equals("J")) {
						System.out.print("Viewer Host: ");
						System.out.flush();
						viewerHost = br.readLine();
						System.out.print("Viewer port: ");
						System.out.flush();
						viewerPort = br.readLine();
						philosopherHelper.setViewer((Viewer)LocateRegistry.getRegistry(viewerHost,Integer.parseInt(viewerPort)).lookup("viewer"));
					}
					
				}
				int amount;
				System.out.print("Anzahl: ");
				System.out.flush();
				amount = Integer.parseInt(br.readLine());
				philosopherHelper.addNewPhilosopher(amount);
			} else if(input.equals("create Seats")) {
				if(seatHelper == null) {
					if(table == null) {
						table = getTable(br);
					}
					seatHelper = new SeatHelper(table);
				}
				int amount;
				System.out.print("Anzahl: ");
				System.out.flush();
				amount = Integer.parseInt(br.readLine());
				seatHelper.createSeats(amount);	
			} else if(input.equals("get Table")) {
				table = getTable(br);//(Table) Naming.lookup("//"+host+":"+port+"/table");//LocateRegistry.getRegistry(host,Integer.parseInt(port)).lookup("table");
				System.out.println("table == null ? " + (table == null));
			} else if(input.equals("show usable seats")) {
				if(table != null) {
					table.printUsableSeats();
				}
			} else if(input.equals("remove Seats")) {
				if(seatHelper == null) {
					System.out.println("No Seats to remove.");
					continue;
				}
				int amount;
				System.out.print("Anzahl: ");
				System.out.flush();
				amount = Integer.parseInt(br.readLine());
				seatHelper.removeSeats(amount);
			} else if(input.equals("remove Philosophers")) {
				if(philosopherHelper == null) {
					System.out.println("No Philosophers to remove.");
					continue;
				}
				int amount;
				System.out.print("Anzahl: ");
				System.out.flush();
				amount = Integer.parseInt(br.readLine());
				philosopherHelper.stopPhilosophers(amount);
			}
		}
		br.close();
		System.exit(0);
	}
	
	private static Table getTable(BufferedReader br) throws IOException, NotBoundException {
		System.out.print("Host Address: ");
		System.out.flush();
		String host = br.readLine();
		System.out.print("Which Port? ");
		System.out.flush();
		String port = br.readLine();
		return (Table) Naming.lookup("//"+host+":"+port+"/table");
	}
}
