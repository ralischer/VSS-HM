package edu.hm.vss.udp.protocol;

public class UdpState {
	/*
	responder :
	 
	Error : packet  number too big
		- prev. packet was lost or prev packet will arive
		later -> send 2 pongs
		
	Error : packet number too small
		- check if a packet drop / delay was assumed with that id
			if so, everything is ok - otherwise protocol error
		 
	Error : same packet number again
		-> sender didnt receive the prev pong, resend
		
		
	sender:
	
	
		
	
		*/
}
