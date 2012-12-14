package edu.hm.vss.prak.rmi.rms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread {

	private final Socket client;
	private Object instanceObject;
	private final Map<Class<?>, Class<?>> bindingsTable;
	private int waitTime = 1;
	private final static int MAX_WAIT_TIME = 1000;

	public ClientHandler(Socket socket, Map<Class<?>, Class<?>> bindings) {
		client = socket;
		bindingsTable = bindings;
	}

	@Override
	public void run() {
		ObjectOutputStream objectOutputStream = null;
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
					client.getInputStream());
			while (client.isConnected()) {
				RequestWrapper request = null;

				try {
					request = (RequestWrapper) objectInputStream.readObject();
				} catch (IOException ex) {
					// ex.printStackTrace();
					try {
						Thread.sleep(waitTime);
						if (waitTime >= MAX_WAIT_TIME) {
							waitTime = MAX_WAIT_TIME;
						} else {
							waitTime *= 2;
						}
					} catch (InterruptedException ignored) {
					}
					continue;
				}
				waitTime /= 2;
				waitTime++;
				switch (request.requestType) {
				case CLASS:
					try {
						Class<?> clazz = bindingsTable.get(Class
								.forName(request.requestName));
						instanceObject = clazz.newInstance();
					} catch (ClassNotFoundException e) {
						client.getOutputStream().write(0);
						client.getOutputStream().flush();
					} catch (InstantiationException e) {
						client.getOutputStream().write(0);
						client.getOutputStream().flush();
					} catch (IllegalAccessException e) {
						client.getOutputStream().write(0);
						client.getOutputStream().flush();
					}
					System.out.println("all successful");
					client.getOutputStream().write(1);
					client.getOutputStream().flush();
					break;
				case METHOD:
					if (instanceObject != null) {
						if (objectOutputStream == null) {
							objectOutputStream = new ObjectOutputStream(
									client.getOutputStream());
						}
						Class<?>[] parameterTypes = new Class<?>[request.params.length];
						for (int i = 0; i < parameterTypes.length; i++) {
							parameterTypes[i] = request.params[i].getClass();
						}
						Method m = instanceObject.getClass().getMethod(
								request.requestName, parameterTypes);
						Object result = m.invoke(instanceObject,
								(Object[]) request.params);
						if (result == null)
							result = Void.TYPE;
						System.out.println("method invoked resultObject: "
								+ result.getClass().getSimpleName()
								+ " value: " + result);
						if (result instanceof Serializable) {
							System.out
									.println("instance of serializable found");
							objectOutputStream.writeObject(result);
							objectOutputStream.flush();
						}
					} else {
						System.out.println("write npe");
						objectOutputStream
								.writeObject(new NullPointerException());
						objectOutputStream.flush();
					}
					break;
				default:
					break;
				}
			}
			objectInputStream.close();
			objectOutputStream.close();
			client.close();
		} /*
		 * catch (IOException ex) { ex.printStackTrace(); } catch
		 * (ClassNotFoundException e1) { e1.printStackTrace(); } catch
		 * (NoSuchMethodException e) { e.printStackTrace(); } catch
		 * (SecurityException e) { e.printStackTrace(); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); } catch
		 * (IllegalArgumentException e) { e.printStackTrace(); } catch
		 * (InvocationTargetException e) { e.printStackTrace(); }
		 */
		catch (Exception e) {
			if (objectOutputStream != null) {
				//falls ObjectOutputStream verfügbar ist, leite die Exception weiter.
				try {
					objectOutputStream.writeObject(e);
					objectOutputStream.flush();
				} catch (IOException e1) {
					// ignored.....
					e1.printStackTrace();
				}
			} else {
				try {
					client.getOutputStream().write(0);
					client.getOutputStream().flush();
				} catch (Exception e2) {
					// ignored
					e2.printStackTrace();
				}
			}
		}
	}
}
