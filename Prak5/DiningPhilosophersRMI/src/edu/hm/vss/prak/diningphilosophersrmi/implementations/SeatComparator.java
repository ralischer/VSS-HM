package edu.hm.vss.prak.diningphilosophersrmi.implementations;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Comparator;

import edu.hm.vss.prak.diningphilosophersrmi.interfaces.Seat;

public class SeatComparator implements Comparator<Seat>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4331854824715416419L;

	@Override
	public int compare(Seat arg0, Seat arg1) {
		if(arg0.hashCode() == arg1.hashCode())
			return 0;
		try {
			return arg1.getRating()-arg0.getRating();
		} catch (RemoteException e) {
			return 1;
		}
	}

}
