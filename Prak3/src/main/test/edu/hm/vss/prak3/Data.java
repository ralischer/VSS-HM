package edu.hm.vss.prak3;

public class Data {

	private final String indicator;
	private final int number;
	
	public Data(String indicator, int number) {
		this.indicator = indicator;
		this.number = number;
	}
	
	@Override
	public String toString() {
		return "Data{"+indicator+", "+number+"}";
	}
}
