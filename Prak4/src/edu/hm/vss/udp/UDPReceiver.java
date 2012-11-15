package edu.hm.vss.udp;

import java.io.IOException;

import edu.hm.vss.udp.dataStructure.UDPMessage;

public class UDPReceiver extends Thread {

	private String response;
	private String request;
	private UDPManager manager;
	private int lastReceivedId = -1;

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
				if (!udp.message.equals(request)) {
					output("invalid message ", udp);
					continue;
				}
				if ((lastReceivedId < 0)// first message
						|| (lastReceivedId + 2 == udp.id)) {// correct id
					lastReceivedId = udp.id;
					output("valid request", udp);
				}
				// previous id, prolly this packet was lost
				else if (lastReceivedId == udp.id) {
					output("previous id, resend previous answer", udp);
				}
				// package loss happened don't send anything since the next
				// rule should have already sent it
				else if (lastReceivedId > udp.id) {
					output("old id, i should already have sent it", udp);
					continue;
				}
				// received a higher id than expeceted, the other requests
				// should be on the way so i send all packets until this id
				else {
					int discrepancy = udp.id - (lastReceivedId + 2);
					discrepancy /= 2;
					for (int i = 0; i < discrepancy; i++) {
						sendAnswer();
						lastReceivedId += 2;
					}
				}
				sendAnswer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		manager.close();

	}

	private void output(String msg, UDPMessage udp, Object... args) {
		String udpContent;
		if (udp != null) {
			udpContent = String.format("  \"%s\" [%d]", udp.message, udp.id);
		} else
			udpContent = "";
		System.out.print("Receiver : ");
		System.out.printf(msg + udpContent + "%n", args);
	}
}
