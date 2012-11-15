package edu.hm.vss.udp;

import java.io.IOException;

import edu.hm.vss.udp.dataStructure.UDPMessage;

public class UDPSender extends Thread {

	private String request;
	private String response;
	private UDPManager udpManager;
	private int sendCount;

	public UDPSender(String request, String response, UDPManager manager,
			int sendCount) {
		this.request = request;
		this.response = response;
		this.udpManager = manager;
		this.sendCount = sendCount;
	}

	@Override
	public void run() {
		System.out.println("running");
		try {
			int currentId = 0;
			boolean wrongeMessageIdReceived = false;
			for (int i = 0; i < sendCount;) {
				if (!wrongeMessageIdReceived) {
					udpManager.sendMessage(request, currentId);
				}
				wrongeMessageIdReceived = false;
				UDPMessage udp = udpManager.receiveMessage();
				if (udp == null) {
					output("socket Timeout, resending the previous request",
							null);
				}
				else if (!udp.message.equals(response)) {
					output("received invalid response, resending previous request",
							udp);					
				}
				else if (udp.id == currentId + 1) {
					output("valid response", udp);
					currentId += 2;
					i++;
				}
				else {
					output("invalid id, ignore this message", udp);
					wrongeMessageIdReceived = true;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			udpManager.close();
		}
	}

	private void output(String msg, UDPMessage udp, Object... args) {
		String udpContent;
		if (udp != null) {
			udpContent = String.format("  \"%s\" [%d]", udp.message, udp.id);
		} else
			udpContent = "";
		System.out.print("Sender : ");
		System.out.printf(msg + udpContent + "%n", args);
	}
}
