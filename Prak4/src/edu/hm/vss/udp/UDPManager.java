package edu.hm.vss.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.hm.vss.udp.dataStructure.PayloadDatagramPackage;
import edu.hm.vss.udp.dataStructure.UDPMessage;

/**
 * With this class you can receive and send UDP packets containing a string and
 * an integer as payload.
 * 
 * @author IoI
 * 
 */
public class UDPManager {
	private DatagramSocket socket;
	private int destinationPort;
	private InetAddress destination;
	public final int UDP_LENGTH = 1024;

	protected UDPManager(String destinationIP, int sourcePort,
			int destinationPort, int timeout) throws SocketException,
			UnknownHostException {
		this.socket = new DatagramSocket(sourcePort);
		socket.setSoTimeout(timeout);
		this.destinationPort = destinationPort;
		this.destination = InetAddress.getByName(destinationIP);
	}

	/**
	 * Sends a string and an integer as payload to the inetadress destination
	 * 
	 * @param message
	 * @param id
	 * @throws IOException
	 */
	public void sendMessage(String message, int id) throws IOException {
		ArrayList<byte[]> payload = new ArrayList<byte[]>();
		payload.add(new byte[] { (byte) id });
		payload.add(message.getBytes());
		DatagramPacket data = PayloadDatagramPackage.createDatagramPacket(
				payload, UDP_LENGTH);
		data.setAddress(destination);
		data.setPort(destinationPort);
		socket.send(data);
	}

	public void sendMessage(String message) throws IOException {
		sendMessage(message, -1);
	}

	/**
	 * Listrting to the defined port in order to receive an UDP message
	 * containing a string and an integer value
	 * 
	 * @return contains an integeger and a string
	 * @throws IOException
	 */
	public UDPMessage receiveMessage() throws IOException {
		DatagramPacket packet = new DatagramPacket(new byte[UDP_LENGTH],
				UDP_LENGTH);
		try {
			socket.receive(packet);
			ArrayList<byte[]> content = PayloadDatagramPackage
					.getDatagramContent(packet);
			return new UDPMessage(new String(content.get(1)),
					(int) content.get(0)[0], packet.getAddress());
		} catch (SocketTimeoutException ex) {
			return null;
		}

	}

	/**
	 * closes the socket
	 */
	public void close() {
		socket.close();
	}
}
