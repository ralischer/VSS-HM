package edu.hm.vss.prak.diningphilosophersrmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Philosopher;
import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Table;

public class DiningPhilosopherRMI {

	public static void main(String... args) throws Exception {
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
				String port;
				System.out.print("Which port? ");
				System.out.flush();
				port = br.readLine();
				PhilosopherMain.main(port);
				Philosopher p = (Philosopher) LocateRegistry.getRegistry(Integer.parseInt(port)).lookup("philosopher");
				p.setTable(table);
			} else if(input.equals("create Seats")) {
				System.out.print("tableHostAddress: ");
				String tableHostAddress = br.readLine();
				System.out.print("tableHostPort: ");
				String tableHostPort = br.readLine();
				System.out.print("forkAmount: ");
				String forkAmount = br.readLine();
				System.out.print("seatAmount: ");
				String seatAmount = br.readLine();
				System.out.print("serverPort: ");
				String serverPort = br.readLine();
				ForkAndSeatServer.main(tableHostAddress,tableHostPort,forkAmount,seatAmount,serverPort);
				
			} else if(input.equals("get Table")) {
				System.out.print("Host Address: ");
				System.out.flush();
				String host = br.readLine();
				System.out.print("Which Port? ");
				System.out.flush();
				String port = br.readLine();
				table = (Table) Naming.lookup("//"+host+":"+port+"/table");//LocateRegistry.getRegistry(host,Integer.parseInt(port)).lookup("table");
				System.out.println("table == null ? " + (table == null));
			}
		}
		br.close();
		System.exit(0);
	}
}
