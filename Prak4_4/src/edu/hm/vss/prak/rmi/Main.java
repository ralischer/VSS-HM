package edu.hm.vss.prak.rmi;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.hm.vss.prak.rmi.interfaces.Foo;
import edu.hm.vss.prak.rmi.interfaces.SimpleTasks;
import edu.hm.vss.prak.rmi.rms.RemoteMethodService;
import edu.hm.vss.prak.rmi.rms.exceptions.ClassUnboundException;

public class Main {

	public static void main(String... args) throws UnknownHostException, IOException, ClassUnboundException {
		RemoteMethodService rms = RemoteMethodService.getInstance("localhost", 8888);
		Foo foo = rms.getInstace(Foo.class);
		foo.hello();
		foo.foo();
		foo = null;
		System.gc();
		SimpleTasks st = rms.getInstace(SimpleTasks.class);
		System.out.println(st.add(1, 3));
		System.out.println(st.upperCase("hello world"));
	}
	
}
