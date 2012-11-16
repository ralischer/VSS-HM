package edu.hm.vss.prak.rmi.implementations;

import edu.hm.vss.prak.rmi.interfaces.SimpleTasks;

public class SimpleTaskImpl implements SimpleTasks{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4511917056407897106L;

	@Override
	public Integer add(Integer a, Integer b) {
		return a+b;
	}

	@Override
	public String upperCase(String lowerCase) {
		return lowerCase.toUpperCase();
	}

}
