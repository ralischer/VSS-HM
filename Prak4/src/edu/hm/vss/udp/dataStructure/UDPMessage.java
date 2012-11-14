package edu.hm.vss.udp.dataStructure;

public class UDPMessage {
	public final String message;
	public final int id;

	public UDPMessage(String message, int id) {
		this.message = message;
		this.id = id;
	}
}
