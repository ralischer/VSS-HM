package edu.hm.vss.aufgabe4.teil3.pingpong;

import java.net.SocketException;

import edu.hm.vss.aufgabe4.teil3.pingpong.client.Client;

public class ClientMain {
	public static void main(String... args) throws SocketException {
		Client c = new Client("localhost", 8888, 8889, 5000);
		c.start();
	}
}
