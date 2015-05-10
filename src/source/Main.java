package source;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) {
		
		String receivedData;
		int playerColor = 0; //0-lefka, 1-mavra
		int scoreWhite = 0;
		int scoreBlack = 0;
		boolean gotColor = false;
		String playerName = "GrandMaster Flash";
		int delay = 4000;
		int[] moves;
		int treeDepth = 0;
		
		//Dhmiourgoume object gia th syndesh
 		Connector conn = new Connector(9876, 200, playerName);
		
		World world = new World(delay, treeDepth);
		
		//Stelnoume to onoma sto server
		conn.sendName();
		
//		world.dumdumTree();
//		int[] movies = new int[4];+
//		movies = world.selectMinimaxMove();
//		
//		for(int i = 0; i < 4; i++){
//			
//			System.out.println(movies[i]);
//		}
		
		//Lamvanoume ta mhnymata kai ta epeksergazomaste
		while(true){
			
			receivedData = conn.receiveMessages();
			
			if(receivedData.substring(0, 2).compareToIgnoreCase("PW") == 0){
				
				playerColor = 0;
				gotColor = true;
				System.out.println("I am white");
				world.setMyColor(playerColor);
			}
			else if(receivedData.substring(0, 2).compareToIgnoreCase("PB") == 0){
				
				playerColor = 1;
				gotColor = true;
				System.out.println("I am black");
				world.setMyColor(playerColor);
			}
			else if (gotColor){
				
				if(receivedData.substring(0, 2).compareTo("GB") == 0){//Enarksh paixnidiou
					
					if(playerColor == 0){
						
						world.createTree(delay, treeDepth);
						moves = new int[4];
						moves = Arrays.copyOf(world.selectMinimaxMove(), 4);
						String action = Integer.toString(moves[0]) + Integer.toString(moves[1]) + Integer.toString(moves[2]) + Integer.toString(moves[3]);
//						System.out.println(action);
						conn.sendMessages(action);
					}
					else{
						
						continue;
					}
				}
				else if(receivedData.substring(0, 2).compareTo("GE") == 0){//Telos paixnidiou
					
					scoreWhite = Integer.parseInt(Character.toString(receivedData.charAt(2)) + Character.toString(receivedData.charAt(3)));

					scoreBlack = Integer.parseInt(Character.toString(receivedData.charAt(4)) + Character.toString(receivedData.charAt(5)));

					if(scoreWhite - scoreBlack > 0)
					{
						if(playerColor == 0)
							System.out.println("I won! " + scoreWhite + "-" + scoreBlack);
						else
							System.out.println("I lost. " + scoreWhite + "-" + scoreBlack);
	
						System.out.println("My average branch factor was : " + world.getAvgBFactor());
					}
					else if(scoreWhite - scoreBlack < 0)
					{
						if(playerColor == 0)
							System.out.println("I lost. " + scoreWhite + "-" + scoreBlack);
						else
							System.out.println("I won! " + scoreWhite + "-" + scoreBlack);
	
						System.out.println("My average branch factor was : " + world.getAvgBFactor());
					}
					else
					{
						System.out.println("It is a draw! " + scoreWhite + "-" + scoreBlack);
	
						System.out.println("My average branch factor was : " + world.getAvgBFactor());
					}
	
					break;
				}
				else	// firstLetter.equals("T") - a move has been made
				{
					// decode the rest of the message
					int nextPlayer = Integer.parseInt(Character.toString(receivedData.charAt(1)));
					moves = new int[4];
					moves[0] = Integer.parseInt(Character.toString(receivedData.charAt(2)));
					moves[1] = Integer.parseInt(Character.toString(receivedData.charAt(3)));
					moves[2] = Integer.parseInt(Character.toString(receivedData.charAt(4)));
					moves[3] = Integer.parseInt(Character.toString(receivedData.charAt(5)));
					
					int prizeX = Integer.parseInt(Character.toString(receivedData.charAt(6)));
					int prizeY = Integer.parseInt(Character.toString(receivedData.charAt(7)));
					
					scoreWhite = Integer.parseInt(Character.toString(receivedData.charAt(8)) 
							                      + Character.toString(receivedData.charAt(9)));
					
					scoreBlack = Integer.parseInt(Character.toString(receivedData.charAt(10)) 
												  + Character.toString(receivedData.charAt(11)));
					
					
					
					if(nextPlayer==playerColor)//Epaikse o antipalos, paizoume emeis
					{
						System.out.print("His move: ");
						world.changeRoot(moves);
						
						//Emfanisthke dwro
						if(prizeX != 9){ world.prizeAdded(prizeX, prizeY); }
						world.createTree(delay, treeDepth);
						moves = new int[4];
						moves = Arrays.copyOf(world.selectMinimaxMove(), 4);
						String action = Integer.toString(moves[0]) + Integer.toString(moves[1]) + Integer.toString(moves[2]) + Integer.toString(moves[3]);
						//System.out.println(action);
						conn.sendMessages(action);		
					}
					else//Paizei o antipalos
					{
//						System.out.println();
						if(prizeX != 9){ world.prizeAdded(prizeX, prizeY); }
						continue;
					}				
				}
			}
			else{
				
				System.out.println(playerName + ": Did not get a color assigned");
				break;
			}
		}

	}

}
