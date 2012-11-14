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


	private void sendAnswer() throws IOException{
		manager.sendMessage(response, lastReceivedId+1);
	}

	@Override
	public void run() {
		while (true) {
			try {
				UDPMessage udp = manager.receiveMessage();
				if (udp == null ) {
					System.out.println("receive-Timeout");
					continue;
				}
				if(!udp.message.equals(request)){
					System.out.println("wrong message");
					continue;
				}
				if(		(lastReceivedId < 0)//first message
					|| 	(lastReceivedId + 2 == udp.id)) {//correct id
					lastReceivedId = udp.id; 
				}
				
				else if(lastReceivedId == udp.id){ // previous id, prolly this packet was lost
					//resend with old answer id
				}
				else if(lastReceivedId > udp.id){ //package loss happened, don't send anything since
					continue;						// the next rule should have already sent it
				}
				else{
					int discrepancy = udp.id - (lastReceivedId + 2);
					discrepancy /= 2;
					for(int i = 0; i< discrepancy; i++){
						sendAnswer();
						lastReceivedId+=2;
					}
				}
				sendAnswer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
