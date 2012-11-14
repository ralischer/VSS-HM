package edu.hm.vss.aufgabe4.teil3.pingpong;

import java.net.SocketException;

import edu.hm.vss.aufgabe4.teil3.pingpong.server.Server;

public class ServerMain {

	public static void main(String... args) throws SocketException {
		Server s = new Server(8888, 5000);
		s.start();
	}
	
}
