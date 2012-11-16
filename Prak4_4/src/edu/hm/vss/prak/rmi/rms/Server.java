package edu.hm.vss.prak.rmi.rms;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.hm.vss.prak.rmi.rms.exceptions.BindingException;

public class Server extends Thread{
	private final ServerSocket serverSocket;
	private final Map<Class<?>, Class<?>> bindings = new HashMap<Class<?>, Class<?>>();
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public <T extends Serializable> void bind(Class<T> c, Class<? extends T> i) throws BindingException {
		if(isAlive())
			throw new BindingException("Cannot bind new classes while running");
		bindings.put(c,i);
	}
	
	public void run() {
		while(true)
		try {
			Socket client = serverSocket.accept();
			new ClientHandler(client,Collections.unmodifiableMap(bindings)).start();
		} catch (IOException e) {
			//TODO: exception handling
		}
	}
}
