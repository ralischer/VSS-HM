package edu.hm.vss.udp;

import java.io.IOException;

import edu.hm.vss.udp.dataStructure.UDPMessage;

public class UDPSender extends Thread {

	private String message;
	private UDPManager udpManager;
	private int sendCount;

	public UDPSender(String message, UDPManager manager, int sendCount) {
		this.message = message;
		this.udpManager = manager;
		this.sendCount = sendCount;
	}

	@Override
	public void run() {
		System.out.println("running");
		try {
			int currentId = 0;
			for (int i = 0; i < sendCount; i++) {
				udpManager.sendMessage(message, currentId++);
				UDPMessage answer = udpManager.receiveMessage();
				if(answer == null){ //socket timeout, resend ping
					currentId--;
					i--;
					continue;
				}
				if (answer.id != currentId) {
					System.out.println("an error occured");
					if (answer.id == currentId - 2) {
						currentId--;
					} else {
						udpManager.close();
						System.exit(-1);
					}
				} else {
					System.out.printf(
							"Sender : received message %s with id %d%n",
							answer.message, answer.id);
					currentId++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			udpManager.close();
		}
	}
}
