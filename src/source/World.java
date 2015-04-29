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
	private State curr_state = null;
	private State pointer = null;
	
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
		
		curr_state = new State(board, null, 1, null);
		
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	public String selectAction()
	{
		availableMoves = new ArrayList<String>();
				
		if(myColor == 0)		// I am the white player
			this.whiteMoves();
		else					// I am the black player
			this.blackMoves();
		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		
		return this.selectRandomAction();
	}
	
	public void createTree(){
		
		ArrayList<int[]> availableMoves;
		
		if(pointer == null){
			
			availableMoves = whiteMoves(curr_state.getBoard());
			
			
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
	
	public String[][] makeMove(String[][] board, int x1, int y1, int x2, int y2)
	{
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		return board;
		
	}
	
	public void prizeAdded(int prizeX, int prizeY){
		
		curr_state.getBoard()[prizeX][prizeY] = "P";
	}
	
}

