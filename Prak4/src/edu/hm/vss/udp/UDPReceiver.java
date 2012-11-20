package edu.hm.vss.udp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import edu.hm.vss.udp.dataStructure.UDPMessage;

public class UDPReceiver extends Thread {

	private String response;
	private String request;
	private UDPManager manager;
	private int lastReceivedId = -1;
	private int lastRespondedId = -1;

	public UDPReceiver(String request, String response, UDPManager manager)
			throws IOException {
		this.response = response;
		this.request = request;
		this.manager = manager;
	}

	private void sendAnswer() throws IOException {
		manager.sendMessage(response, lastReceivedId + 1);
	}

	@Override
	public void run() {
		while (!interrupted()) {
			try {
				UDPMessage udp = manager.receiveMessage();
				if (udp == null) { // socket timeout
					continue;
				}

				if (!udp.message.equals(request)) {
					output("invalid request ", udp);
					continue;
				} else {
					output("received request", udp);
				}
				manager.sendMessage(response);
				udp = null;
				while (udp == null) {
					udp = manager.receiveMessage();
					if (!udp.message.equals(request)) {
						output("invalid response prior to request", udp);
						udp = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		manager.close();

	}

	private void output(String msg, UDPMessage udp, Object... args) {
		String udpContent;
		if (udp != null) {
			udpContent = String.format("  \"%s\"", udp.message);
		} else
			udpContent = "";
		System.out.printf("%d [Receiver] : ", System.nanoTime());
		System.out.printf(msg + udpContent + "%n", args);
	}

	public static void main(String[] args) throws IOException {
		UDPManager manager = new UDPManager("localhost",
				IPingPongConstants.RECEIVER_LISTENING_PORT,
				IPingPongConstants.SENDER_LISTENING_PORT, 0);
		UDPReceiver receiver = new UDPReceiver(IPingPongConstants.SENDER_MSG,
				IPingPongConstants.RECEIVER_MSG, manager);
		receiver.run();
	}
}
