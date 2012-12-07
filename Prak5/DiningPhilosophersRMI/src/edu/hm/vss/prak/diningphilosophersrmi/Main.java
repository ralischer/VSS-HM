package edu.hm.vss.prak.diningphilosophersrmi;

import java.util.LinkedList;
import java.util.List;

public class Main {

	public static void main(String... args) {
		System.out.println(System.getProperty("java.rmi.server.hostname"));
		List<Integer> list = new LinkedList<Integer>();
		list.add(0);
		list.add(2);
		list.add(3);
		System.out.println(list);
		list.add(1,1);
		System.out.println(list);
		list.add(4,4);
		System.out.println(list);
	}
	
}
