package edu.hm.vss.aufgabe4.teil3.pingpong.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import edu.hm.vss.aufgabe4.teil3.pingpong.Constants;

public class Server extends Thread {
	private final DatagramSocket serverSocket;
	private byte[] receivingBuffer = new byte[Constants.BUFFER_SIZE];
	private boolean running;

	public Server(int port, int timeout) throws SocketException {
		serverSocket = new DatagramSocket(port);
		serverSocket.setSoTimeout(timeout);
		running = true;
	}

	public void run() {
		while (running) {
			DatagramPacket receivePacket = new DatagramPacket(receivingBuffer,
					receivingBuffer.length);
			DatagramPacket sendPacket;
			try {
				serverSocket.receive(receivePacket);
			} catch (SocketTimeoutException ex) {
				// TODO: error log
				continue;
			} catch (IOException ex) {
				// TODO: error log
				continue;
			}
			String request = new String(receivePacket.getData());
			System.out.println("requested: " + request);
			sendPacket = new DatagramPacket(Constants.PONG.getBytes(),
					Constants.PONG.getBytes().length,
					receivePacket.getAddress(), receivePacket.getPort());
			if (request.contains(Constants.PING)) {
				//receivePacket.setData(Constants.PONG.getBytes());
				try {
					serverSocket.send(sendPacket);
				} catch(IOException ex) {
					//TODO: error log
				}
			} else {
				// TODO: error log
				continue;
			}
			
			try {
				serverSocket.receive(receivePacket);
			} catch (SocketTimeoutException ex) {
				// TODO: error log
				// TODO: pong nochmals schicken
			} catch (IOException ex) {
				// TODO: error log
			}
			request = new String(receivePacket.getData());
			System.out.println("requested: " + request);
			if (request.contains(Constants.PING)) {
				
				//alles glatt gelaufen 
				receivePacket.setData(Constants.PONG.getBytes());
				try {
					serverSocket.send(sendPacket);
				} catch(IOException ex) {
					//TODO: error log
				}
			} else {
				// TODO: error log
				// TODO: pong nochmal schicken
				continue;
			}
		}
	}

}
