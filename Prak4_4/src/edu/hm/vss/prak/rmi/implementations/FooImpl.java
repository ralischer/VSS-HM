package edu.hm.vss.prak.rmi.implementations;

import edu.hm.vss.prak.rmi.interfaces.Foo;

public class FooImpl implements Foo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2716047211061654064L;

	public void foo() {
		System.out.println("foo called");		
	}


	public void hello() {
		System.out.println("hello called");		
	}

}
