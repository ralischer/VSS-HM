package edu.hm.vss.aufgabe4.teil3.pingpong.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import edu.hm.vss.aufgabe4.teil3.pingpong.Constants;

public class Client extends Thread{
	private final DatagramSocket socket;
	private final String serverIp;
	private final int serverPort;
	private final byte[] receiveBuffer = new byte[Constants.BUFFER_SIZE];
	
	public Client(String serverIp, int serverPort, int port, int timeout) throws SocketException {
		socket = new DatagramSocket(port);
		socket.setSoTimeout(timeout);
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}
	
	public void run() {
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(serverIp);
		} catch (UnknownHostException ex) {
			//TODO: exception handling
		}
		DatagramPacket packet = new DatagramPacket(Constants.PING.getBytes(), Constants.PING.getBytes().length, inetAddress, serverPort);
		try {
			socket.send(packet);
		} catch(SocketTimeoutException ex) {
			//TODO: error handling
		} catch (IOException ex) {
			//TODO: error handling
		}
		DatagramPacket receivingPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		try {
			socket.receive(receivingPacket);
		} catch(SocketTimeoutException ex) {
			//TODO: error handling
		} catch(IOException ex) {
			//TODO: error handling
		}
		String string = new String(receivingPacket.getData());
		System.out.println("received: "+string);
		if(string.contains(Constants.PONG)) {
			try {
				socket.send(packet);
			} catch(SocketTimeoutException ex) {
				//TODO: error handling
			} catch (IOException ex) {
				//TODO: error handling
			}
		} else {
			//TODO: what to do when received something other?
		}
	}
}
