package edu.hm.vss.udp;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, IOException,
			InterruptedException {

		final int sourcePortSender = 6666;
		final int destinationPortSender = 6667;
		final int sourcePortReceiver = destinationPortSender;
		final int destinationPortReceiver = sourcePortSender;
		final int socketTimeout = 1;
		UDPManager managerSender = new UDPManager("localhost",
				sourcePortSender, destinationPortSender,socketTimeout) {
		};
		UDPManager managerReceiver = new UDPManager("localhost",
				sourcePortReceiver, destinationPortReceiver, 0) {
		};
		UDPSender sender = new UDPSender("ping","pong", managerSender, 5);
		UDPReceiver receiver = new UDPReceiver("ping", "pong", managerReceiver);
		receiver.start();
		sender.start();		
		sender.join();
		System.out.println("Sender terminated");
		receiver.interrupt();
		receiver.join();
	}
}
