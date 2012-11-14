package edu.hm.vss.udp;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, IOException,
			InterruptedException {

		final int sourcePortSender = 6666;
		final int destinationPortSender = 6667;
		final int sourcePortReceiver = destinationPortSender;
		final int destinationPortReceiver = sourcePortSender;
		final int socketTimeout = 10;
		UDPManager managerSender = new UDPManager("localhost",
				sourcePortSender, destinationPortSender,socketTimeout) {
		};
		UDPManager managerReceiver = new UDPManager("localhost",
				sourcePortReceiver, destinationPortReceiver, socketTimeout) {
		};
		UDPSender sender = new UDPSender("ping", managerSender, 5);
		UDPReceiver receiver = new UDPReceiver("ping", "pong", managerReceiver);
		sender.start();
		receiver.start();
		sender.join();
		System.out.println("Sender terminated");
		receiver.interrupt();
	}
}
