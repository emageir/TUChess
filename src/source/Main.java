package source;

import java.net.DatagramSocket;

public class Main {
	
	public static void main(String[] args) {
		
		String receivedData;
		char playerColor; //w-lefka, b-mavra
		boolean gotColor = false;
		String playerName = "GrandMaster Flash";
		
		//Dhmiourgoume object gia th syndesh
		Connector conn = new Connector(9876, 200, playerName);
		
		//Stelnoume to onoma sto server
		conn.sendName();
		
		//Lamvanoume ta mhnymata kai ta epeksergazomaste
		while(true){
			
			receivedData = conn.receiveMessages();
			
			if(receivedData.substring(0, 2).compareToIgnoreCase("PW") == 0){
				
				playerColor = 'w';
				gotColor = true;
				System.out.println("I am white");
			}
			else if(receivedData.substring(0, 2).compareToIgnoreCase("PB") == 0){
				
				playerColor = 'b';
				gotColor = true;
				System.out.println("I am black");
			}
			else if (gotColor){
				
				
			}
			else{
				
				System.out.println(playerName + ": Did not get a color assigned");
				break;
			}
		}

	}

}
