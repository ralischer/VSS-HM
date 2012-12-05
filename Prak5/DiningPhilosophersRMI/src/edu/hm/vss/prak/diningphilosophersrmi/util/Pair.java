package edu.hm.vss.prak.diningphilosophersrmi.util;

import java.io.Serializable;

public class Pair<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8707236386622440593L;
	
	private final T first;
	private final T second;
	
	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}
	
	public T getFirst() {
		return first;
	}
	
	public T getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		return first.hashCode() * second.hashCode();
	}
	
	@Override
	public boolean equals(Object x) {
		if(x instanceof Pair) {
			Pair other = (Pair) x;
			return other.getFirst() == getFirst() && other.getSecond() == getSecond();
		}
		return false;
	}
}
