package source;

import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) {
		
		String receivedData;
		int playerColor = 0; //0-lefka, 1-mavra
		int scoreWhite = 0;
		int scoreBlack = 0;
		boolean gotColor = false;
		String playerName = "GrandMaster Flash";
		String shortName = "GMF";
		int delay = 4000;
		int[] moves;
		int treeDepth = 0; //An treeDepth = 0, to dentro de stamataei se sygekrimeno vathos, alliws ftanei se vathos treeDepth + 1
		int branchFactor = 10;
		
		System.out.println(playerName + " Chess Engine\nAI Project 2015\nVangelis Mageiropoulos, Dimitris Bousias");
		
		//Dhmiourgoume object gia th syndesh
 		Connector conn = new Connector(9876, 200, playerName, shortName);
		
		World world = new World(shortName, delay);
		
		//Stelnoume to onoma sto server
		conn.sendName();
		
		
		//Lamvanoume ta mhnymata kai ta epeksergazomaste
		while(true){
			
			receivedData = conn.receiveMessages();
			
			if(receivedData.substring(0, 2).compareToIgnoreCase("PW") == 0){
				
				playerColor = 0;
				gotColor = true;
				System.out.println(shortName + ": Playing as white");
				world.setMyColor(playerColor);
			}
			else if(receivedData.substring(0, 2).compareToIgnoreCase("PB") == 0){
				
				playerColor = 1;
				gotColor = true;
				System.out.println(shortName + ": Playing as black");
				world.setMyColor(playerColor);
			}
			else if (gotColor){
				
				if(receivedData.substring(0, 2).compareTo("GB") == 0){//Enarksh paixnidiou
					
					if(playerColor == 0){
						
//						world.createTree(delay, treeDepth, 0, 0);
						world.createFPTree(delay, treeDepth, 0, 0, branchFactor);
						moves = new int[4];
						moves = Arrays.copyOf(world.selectMinimaxMove(), 4);
						String action = Integer.toString(moves[0]) + Integer.toString(moves[1]) + Integer.toString(moves[2]) + Integer.toString(moves[3]);
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
							System.out.println(shortName + ": I won! " + scoreWhite + "-" + scoreBlack);
						else
							System.out.println(shortName + ": I lost. " + scoreWhite + "-" + scoreBlack);
					}
					else if(scoreWhite - scoreBlack < 0)
					{
						if(playerColor == 0)
							System.out.println(shortName + ": I lost. " + scoreWhite + "-" + scoreBlack);
						else
							System.out.println(shortName + ": I won! " + scoreWhite + "-" + scoreBlack);
	
					}
					else
					{
						System.out.println(shortName + ": It is a draw! " + scoreWhite + "-" + scoreBlack);
	
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
						System.out.print(shortName + ": Opponent's move: ");
						world.changeRoot(moves);
						
						//Emfanisthke dwro?
						if(prizeX != 9){ world.prizeAdded(prizeX, prizeY); }
						//Dhmiourgoume to dentro
//						world.createTree(delay, treeDepth, scoreWhite, scoreBlack);
						world.createFPTree(delay, treeDepth, 0, 0, branchFactor);
						moves = new int[4];
						moves = Arrays.copyOf(world.selectMinimaxMove(), 4);
						String action = Integer.toString(moves[0]) + Integer.toString(moves[1]) + Integer.toString(moves[2]) + Integer.toString(moves[3]);
						conn.sendMessages(action);		
					}
					else//Paizei o antipalos
					{
						//Emfanisthke dwro?
						if(prizeX != 9){ world.prizeAdded(prizeX, prizeY); }
						continue;
					}				
				}
			}
			else{
				
				System.out.println(shortName + ": Error: Did not get a color assigned");
				break;
			}
		}

	}

}
