package edu.hm.vss.udp.dataStructure;

import java.net.InetAddress;

public class UDPMessage {
	public final String message;
	public final int id;
	public final InetAddress senderAdress;

	public UDPMessage(String message, int id, InetAddress inetAdress) {
		this.message = message;
		this.id = id;
		this.senderAdress = inetAdress;
	}
}
