package edu.hm.vss.prak.rmi.interfaces;

import java.io.Serializable;

public interface SimpleTasks extends Serializable{

	public Integer add(Integer a, Integer b);
	public String upperCase(String lowerCase);
}
