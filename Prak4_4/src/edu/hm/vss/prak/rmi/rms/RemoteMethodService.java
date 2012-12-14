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
	private static Server server = null;
	private final static RemoteMethodService instance = new RemoteMethodService();

	private RemoteMethodService() {
	};

	/**
	 * Factory Methode um einen Server zu erstellen.
	 * 
	 * @param port
	 *            Port auf dem der Server laufen soll.
	 * @return die Serverinstanz.
	 * @throws IOException
	 *             falls Port bereits belegt oder andere IOExceptions auftreten.
	 */
	public static Server getServerInstance(int port) throws IOException {
		if (server == null) {
			server = new Server(port);
		}
		return server;
	}

	/**
	 * Liefert die Instanz des RemoteMethodService.
	 * 
	 * @param serverAddress
	 *            die Adresse auf dem Server erreichbar ist.
	 * @param serverPort
	 *            der Port auf dem Server läuft.
	 * @return die Instanz des RemoteMethodServices.
	 * @throws UnknownHostException
	 *             falls Host nicht gefunden wurde.
	 * @throws IOException
	 */
	public static RemoteMethodService getInstance(String serverAddress,
			int serverPort) throws UnknownHostException, IOException {
		instance.serverAddress = serverAddress;
		instance.serverPort = serverPort;
		return instance;
	}

	/**
	 * Ist für die Kommunikation mit dem Server zuständig. Jedes Object hat eine
	 * eigene Client-Instanz.
	 * 
	 * @author Reinhard Alischer
	 * 
	 */
	static class Client {

		private final Socket socket;
		private ObjectOutputStream objectOutputStream;
		private ObjectInputStream objectInputStream;

		public Client(String serverAddress, int serverPort)
				throws UnknownHostException, IOException {
			socket = new Socket(serverAddress, serverPort);
			objectOutputStream = new ObjectOutputStream(
					socket.getOutputStream());
		}

		/**
		 * Prüft ob bei Server eine Implementierung zu einem bestimmten
		 * Interface gefunden wurde.
		 * 
		 * @param clazz
		 *            interface das verwendet werden soll.
		 * @return true falls auf dem Server eine implementierung existiert,
		 *         false andernfalls.
		 */
		public boolean isClassAvailable(Class<?> clazz) {
			RequestWrapper request = new RequestWrapper();
			request.requestType = RequestType.CLASS;
			request.requestName = clazz.getName();
			request.params = new Integer[0];
			try {
				objectOutputStream.writeObject(request);
				objectOutputStream.flush();

				return socket.getInputStream().read() == 1;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		/**
		 * Leitet einen Methodenaufruf zum Server weiter und liefert das
		 * Ergebnis zurück.
		 * 
		 * @param method
		 *            Methode die ausgeführt werden soll.
		 * @param args
		 *            die Pramater der Methode.
		 * @return das Ergebnis der Methode - kann Void.Type liefern falls die
		 *         Methode keinen Rückgabewert hat.
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		public Object invokeMethod(Method method, Object[] args)
				throws IOException, ClassNotFoundException {
			RequestWrapper request = new RequestWrapper();
			request.requestType = RequestType.METHOD;
			request.requestName = method.getName();
			if (args == null)
				request.params = new Integer[0];
			else
				request.params = args;
			objectOutputStream.writeObject(request);
			objectOutputStream.flush();
			if (objectInputStream == null) {
				objectInputStream = new ObjectInputStream(
						socket.getInputStream());
			}

			Object respone = objectInputStream.readObject();
			return respone;
		}

		/**
		 * Terminiert den Client - auch Serverseitig.
		 */
		public void terminate() {
			System.out.println("going to terminate");
			try {
				socket.close();
			} catch (IOException ignored) {
			}
		}
	}

	/**
	 * Klasse die die Methoden aufrufe der Client-Stubs entgegen nimmt.
	 * 
	 * @author reini
	 * 
	 */
	private class ClientInvocationHandler implements InvocationHandler {

		private final Client client;

		public ClientInvocationHandler(Client client) {
			this.client = client;
		}

		/**
		 * Wird für jede Methode die auf dem Client-Stub aufgerufen wird
		 * ausgeführt.
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			/**
			 * Die Methode finalize sollte vom Garbage-Collector aufgerufen
			 * werden falls ein Objekt nicht mehr referenziert wird und dann aus
			 * dem Speicher gelöscht wird.
			 */
			if (method.getName().equals("finalize")) {
				client.terminate();
				method.invoke(proxy, args);
				return Void.TYPE;
			}

			/**
			 * Alle Methoden werden zum Server weitergeleitet.
			 */
			return client.invokeMethod(method, args);
		}

	}

	/**
	 * Erstellt Client-seitig einen Stub des Interfaces das übergeben wird.
	 * 
	 * @param clazz
	 *            das interface das auf dem Server Verfügbar sein soll.
	 * @return ein Proxy/Stub Objekt welches die Methodenaufrufe des Client zum
	 *         Server weiterleitet.
	 * @throws ClassNotBindedException
	 *             falls zum Interface auf dem Server keine Implementierung
	 *             existiert.
	 * @throws UnknownHostException
	 *             - falls der angegebene Host der bei der Erstellung des
	 *             RemoteMethodService angegeben wurde nicht gefunden wurde.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstace(Class<T> clazz) throws ClassNotBindedException,
			UnknownHostException, IOException {
		Client client = new Client(serverAddress, serverPort);
		if (client.isClassAvailable(clazz))
			return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
					new Class[] { clazz }, new ClientInvocationHandler(client));
		else
			throw new ClassNotBindedException();
	}
}
