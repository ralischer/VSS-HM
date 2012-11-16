package edu.hm.vss.prak.rmi.rms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.hm.vss.prak.rmi.rms.exceptions.ClassNotBindedException;

public class RemoteMethodService {

	
	private String serverAddress;
	private int serverPort;
	//private Client client;
	private static Server server = null;
	private final static RemoteMethodService instance = new RemoteMethodService();
	
	private RemoteMethodService() {};
	
	public static Server getServerInstance(int port) throws IOException {
		if(server == null) {
			server = new Server(port);
		}
		return server;
	}
	
	public static RemoteMethodService getInstance(String serverAddress, int serverPort) throws UnknownHostException, IOException {
		instance.serverAddress = serverAddress;
		instance.serverPort = serverPort;
		//instance.client = new Client(serverAddress, serverPort);
		return instance;
	}
	
	static class Client {
		
		private final Socket socket;
		private ObjectOutputStream objectOutputStream;
		private ObjectInputStream objectInputStream;
		
		public Client(String serverAddress, int serverPort) throws UnknownHostException, IOException {
			socket = new Socket(serverAddress,serverPort);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		}
		public boolean isClassAvailable(Class<?> clazz) {
			RequestWrapper request = new RequestWrapper();
			request.requestType = RequestType.CLASS;
			request.requestName = clazz.getName();
			request.params = new Integer[0];
			try {
				objectOutputStream.writeObject(request);
				objectOutputStream.flush();
				//byte[] buffer = new byte[16];
				//socket.getInputStream().read(buffer);
				//Arrays.toString(buffer);
				
				//for(int i = socket.getInputStream().read(); i != 1; i = socket.getInputStream().read()) {
				//	if(i == -1)
				//		return false;
				//}
				
				return socket.getInputStream().read() == 1;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public Object invokeMethod(Method method, Object[] args) throws IOException, ClassNotFoundException {
			RequestWrapper request = new RequestWrapper();
			request.requestType = RequestType.METHOD;
			request.requestName = method.getName();
			if(args == null)
				request.params = new Integer[0];
			else
				request.params = args;
			objectOutputStream.writeObject(request);
			objectOutputStream.flush();
			if(objectInputStream == null) {
				objectInputStream = new ObjectInputStream(socket.getInputStream());
			}
			
			Object respone = objectInputStream.readObject();
			System.out.println("Type of response "+ respone.getClass().getSimpleName());
			return respone;
		}
		
		public void terminate() {
			System.out.println("going to terminate");
			try {
				socket.close();
			} catch (IOException ignored) {
			}
		}
	}
	//Proxy.newProxyInstance(Foo.class.getClassLoader(),
    //new Class[] { Foo.class },
    //handler);
	private class ClientInvocationHandler implements InvocationHandler {

		private final Client client;
		
		public ClientInvocationHandler(Client client) {
			this.client = client;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if(method.getName().equals("finalize")) {
				client.terminate();
				method.invoke(proxy, args);
				return Void.TYPE;
			}
			
			return client.invokeMethod(method, args);
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getInstace(Class<T> clazz) throws ClassNotBindedException, UnknownHostException, IOException {
		Client client = new Client(serverAddress,serverPort);
		if(client.isClassAvailable(clazz))		
			return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new ClientInvocationHandler(client));
		else
			throw new ClassNotBindedException();
	}
}
