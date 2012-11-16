package edu.hm.vss.prak.rmi.rms;

import java.io.Serializable;

public class RequestWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8307707390216434056L;
	
	public RequestType requestType;
	public String requestName;
	public Object[] params;
	
}
