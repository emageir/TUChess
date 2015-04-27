package source;

import java.net.DatagramSocket;

public class Main {
	
	public static void main(String[] args) {
		
		//Dhmiourgoume object gia th syndesh
		Connector conn = new Connector(9876, 200, "Chesster");
		
		//Stelnoume to onoma sto server
		conn.sendName();
		
		//Lamvanoume ta mhnymata kai ta epeksergazomaste
		while(true){
			
			
		}

	}

}
