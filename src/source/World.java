package source;

import java.util.ArrayList;
import java.util.Random;


public class World
{
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private ArrayList<String> availableMoves = null;
	private int rookBlocks = 2;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	private State root = null;
	ArrayList<State> curr_list = null;
	ArrayList<State> fathers_list = null;
	
	public World()
	{
		String[][] board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		root = new State(board, null, 1, null,myColor);
		
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	public String selectAction()
	{
		availableMoves = new ArrayList<String>();
				
//		if(myColor == 0)		// I am the white player
//			this.whiteMoves();
//		else					// I am the black player
//			this.blackMoves();
		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		
		return this.selectRandomAction();
	}
	
	public void createTree(){
		
		ArrayList<int[]> availableMoves;
		int i, j, prev_play;
		long startTime;
		
		if(curr_list == null){
			
			availableMoves = whiteMoves(root.getBoard());
			
			for(i = 0; i < availableMoves.size(); i++){
				
				State child = new State(makeMove(root.getBoard(), availableMoves.get(i)), root, nextPlayer(root), availableMoves.get(i), myColor);
				root.getChildren().add(child);
			}
			
			curr_list = root.getChildren();
		}
		
		startTime = System.currentTimeMillis();
		
		while(true){
			
			if(System.currentTimeMillis() - startTime < 4000){
				
				prev_play = curr_list.get(1).getlastPlayed();
				
				if(prev_play == 0){//Prohgoumenws eixan paiksei ta lefka
					
					for(i = 0; i < curr_list.size(); i++){
						
						availableMoves = blackMoves(curr_list.get(i).getBoard());
						
						for(j = 0; j < availableMoves.size(); j++){
							
							State child = new State(makeMove(curr_list.get(i).getBoard(), availableMoves.get(j)), curr_list.get(i), nextPlayer(curr_list.get(i)), availableMoves.get(j), myColor);
							curr_list.get(i).getChildren().add(child);
						}
					}
				}
				else{//Prohgoumenws eixan paiksei ta mavra
					
					for(i = 0; i < curr_list.size(); i++){
						
						availableMoves = whiteMoves(curr_list.get(i).getBoard());
						
						for(j = 0; j < availableMoves.size(); j++){
							
							State child = new State(makeMove(curr_list.get(i).getBoard(), availableMoves.get(j)), curr_list.get(i), nextPlayer(curr_list.get(i)), availableMoves.get(j), myColor);
							curr_list.get(i).getChildren().add(child);
						}
					}
				}
				
				//Dialegoume thn epomenh lista komvwn gia na prosthesoume paidia
				//Apofasizoume an h epomenh lista einai sto idio vathos
				//h ena epipedo pio katw
				
			}
			else{
				
				break;
			}
		}
	}
	
	private ArrayList<int[]> whiteMoves(String[][] board)
	{
		String firstLetter = "";
		String secondLetter = "";
//		String move = "";
		int[] move = new int[4];
		ArrayList<int[]> availableMoves = new ArrayList<int[]>();
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					// check if it can move towards the last row
					if(i-1 == 0 && (Character.toString(board[i-1][j].charAt(0)).equals(" ") 
							         || Character.toString(board[i-1][j].charAt(0)).equals("P")))
					{
//						move = Integer.toString(i) + Integer.toString(j) + 
//						       Integer.toString(i-1) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j;
						
						availableMoves.add(move);
						continue;
					}
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i-1) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j;
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));
						
						if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i-1) + Integer.toString(j-1);
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j - 1;
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						
						if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i-1) + Integer.toString(j+1);
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j + 1;
						
						availableMoves.add(move);
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i-(k+1)) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i - (k + 1);
						move[3] = j;
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i+(k+1)) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i + (k + 1);
						move[3] = j;
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i) + Integer.toString(j-(k+1));
						move[0] = i;
						move[1] = j;
						move[2] = i;
						move[3] = (j - (k + 1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i) + Integer.toString(j+(k+1));
						move[0] = i;
						move[1] = j;
						move[2] = i;
						move[3] = (j + (k + 1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i-1) + Integer.toString(j);
							move[0] = i;
							move[1] = j;
							move[2] = i - 1;
							move[3] = j;
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i+1) + Integer.toString(j);
							move[0] = i;
							move[1] = j;
							move[2] = i + 1;
							move[3] = j;
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i) + Integer.toString(j-1);
							move[0] = i;
							move[1] = j;
							move[2] = i;
							move[3] = j - 1;
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i) + Integer.toString(j+1);
							move[0] = i;
							move[1] = j;
							move[2] = i;
							move[3] = j + 1;
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
		
		return(availableMoves);
	}
	
	private ArrayList<int[]> blackMoves(String board[][])
	{
		String firstLetter = "";
		String secondLetter = "";
		//String move = "";
		int[] move = new int[4];
		ArrayList<int[]> availableMoves = new ArrayList<int[]>();
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					// check if it is at the last row
					if(i+1 == rows-1 && (Character.toString(board[i+1][j].charAt(0)).equals(" ")
										  || Character.toString(board[i+1][j].charAt(0)).equals("P")))
					{
//						move = Integer.toString(i) + Integer.toString(j) + 
//						       Integer.toString(i+1) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i + 1;
						move[3] = j;
						
						availableMoves.add(move);
						continue;
					}
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i+1) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i + 1;
						move[3] = j;
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i+1) + Integer.toString(j-1);
						move[0] = i;
						move[1] = j;
						move[2] = i + 1;
						move[3] = j - 1;
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
							continue;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i+1) + Integer.toString(j+1);
						move[0] = i;
						move[1] = j;
						move[2] = i + 1;
						move[3] = j + 1;
						
						availableMoves.add(move);
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i-(k+1)) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i - (k + 1);
						move[3] = j;
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i+(k+1)) + Integer.toString(j);
						move[0] = i;
						move[1] = j;
						move[2] = i + (k + 1);
						move[3] = j;
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i) + Integer.toString(j-(k+1));
						move[0] = i;
						move[1] = j;
						move[2] = i;
						move[3] = j - (k + 1);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i) + Integer.toString(j+(k+1));
						move[0] = i;
						move[1] = j;
						move[2] = i;
						move[3] = j + (k + 1);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i-1) + Integer.toString(j);
							move[0] = i;
							move[1] = j;
							move[2] = i - 1;
							move[3] = j;
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i+1) + Integer.toString(j);
							move[0] = i;
							move[1] = j;
							move[2] = i + 1;
							move[3] = j;
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i) + Integer.toString(j-1);
							move[0] = i;
							move[1] = j;
							move[2] = i;
							move[3] = j - 1;
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
//							move = Integer.toString(i) + Integer.toString(j) + 
//								   Integer.toString(i) + Integer.toString(j+1);
							move[0] = i;
							move[1] = j;
							move[2] = i;
							move[3] = j + 1;
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
		
		return(availableMoves);
	}
	
	private String selectRandomAction()
	{		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	
	//int x1, int y1, int x2, int y2
	public String[][] makeMove(String[][] board, int[] moves)
	{
		String chesspart = Character.toString(board[moves[0]][moves[1]].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (moves[0]==rows-2 && moves[2]==rows-1) || (moves[0]==1 && moves[2]==0) )
			{
				board[moves[2]][moves[3]] = " ";	// in case an opponent's chess part has just been captured
				board[moves[0]][moves[1]] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[moves[2]][moves[3]] = board[moves[0]][moves[2]];
			board[moves[0]][moves[1]] = " ";
		}
		
		return board;
		
	}
	
	public void prizeAdded(int prizeX, int prizeY){
		
		root.getBoard()[prizeX][prizeY] = "P";
	}

	public int getMyColor() {
		return myColor;
	}
	
	public int nextPlayer(State state){
		
		if(state.getlastPlayed() == 0){
			
			return 1;
		}
		else{
			
			return 0;
		}
	}
	
	
}

