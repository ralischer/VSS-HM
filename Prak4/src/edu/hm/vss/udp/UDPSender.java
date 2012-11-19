package edu.hm.vss.udp;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.xml.ws.handler.MessageContext;

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
			for (int i = 0; i < sendCount;) {
				UDPMessage udp = null;
				udpManager.sendMessage(request);
				udp = udpManager.receiveMessage();
				if(udp==null){ // socket timeout
					output("Socket timeout, redo request", udp);
					continue;
				}
				if (!response.equals(udp.message)) {
					output("Illegal response", udp);
					continue;
				}
				else{
					output("received response ",udp);
				}
				udpManager.sendMessage(request);
				i++;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void output(String msg, UDPMessage udp, Object... args) {
		String udpContent;
		if (udp != null) {
			udpContent = String.format(" \"%s\"", udp.message);
		} else
			udpContent = "";
		System.out.printf("%d [Sender] : ",System.nanoTime());
		System.out.printf(msg + udpContent + "%n", args);
	}
}
