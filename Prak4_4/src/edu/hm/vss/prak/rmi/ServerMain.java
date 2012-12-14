package edu.hm.vss.prak.rmi;

import java.io.IOException;

import edu.hm.vss.prak.rmi.implementations.FooImpl;
import edu.hm.vss.prak.rmi.implementations.SimpleTaskImpl;
import edu.hm.vss.prak.rmi.interfaces.Foo;
import edu.hm.vss.prak.rmi.interfaces.SimpleTasks;
import edu.hm.vss.prak.rmi.rms.RemoteMethodService;
import edu.hm.vss.prak.rmi.rms.Server;
import edu.hm.vss.prak.rmi.rms.exceptions.BindingException;

public class ServerMain {

	public static void main(String... args) throws IOException, BindingException {
		Server server = RemoteMethodService.getServerInstance(12000);
		server.bind(Foo.class, FooImpl.class);
		server.bind(SimpleTasks.class, SimpleTaskImpl.class);
		server.start();
	}
	
}
