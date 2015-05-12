//prizeAdded -> new tree


package source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class World
{
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private int rookBlocks = 2;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	private int treeDepth = 0;
	int delay;
	private State root = null;
	private State groot = null;
	ArrayList<State> curr_list = null;
	ArrayList<State> fathers_list = null;
	ArrayList<State> expand_list = new ArrayList<State>();
	Random rand = new Random();
	
	public World(int delay, int treeDepth)
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
		
		root = new State(board, null, 1, null,myColor, 0, 0, 0);
		expand_list.add(root);
		
		this.delay = delay;
		this.treeDepth = treeDepth;
		
//		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}

	
	public void createTree(int delay, int depth, int absoluteScoreWhite, int absoluteScoreBlack){
		
		ArrayList<int[]> availableMoves;
		int i, j = 0;
		State toBeExpanded;
		
		long time = System.currentTimeMillis();
		
		boolean depthReached = false;
		
		while(!expand_list.isEmpty() && (System.currentTimeMillis() - time < delay) && !depthReached){
//		while(!expand_list.isEmpty() && !depthReached){
//			System.out.println(expand_list.size());
			toBeExpanded = expand_list.remove(0);
			
			if(toBeExpanded.getlastPlayed() == 0){
				
				availableMoves = blackMoves(toBeExpanded.getBoard());
			}
			else{
				
				availableMoves = whiteMoves(toBeExpanded.getBoard());
			}
			
			for(i = 0; i < availableMoves.size(); i++){
				
				State child = new State(makeMove(toBeExpanded.getBoard(), availableMoves.get(i)), toBeExpanded, nextPlayer(toBeExpanded), availableMoves.get(i), myColor, toBeExpanded.getDepth() + 1, absoluteScoreWhite, absoluteScoreBlack);
				toBeExpanded.getChildren().add(child);
				if(!child.isTerminal()){
					
					expand_list.add(child);
				}
				
				if(depth > 0 && (child.getDepth() - root.getDepth()) > depth){
					
					depthReached = true;
					
				}
			}
			
		j++;
		
		}
		
//		expand_list.clear();
	}

	
	public int[] selectMinimaxMove(){
		int[] move = null;
		float value;
		int i;
		ArrayList<State> possibleMoves = new ArrayList<State>();//Edw apothikevoume tis kinhseis me idio evaluation
			
//		value=MiniMaxing(2, root,true);
		value = abPrunning(root, true, -Float.MAX_VALUE, Float.MAX_VALUE, 0);
		System.out.println("value = " + value);
		
		for(i=0;i<root.getChildren().size();i++){
			if(root.getChildren().get(i).getMinmaxValue()==value)
			{ possibleMoves.add(root.getChildren().get(i)); }
		}
		
		int selection = rand.nextInt(possibleMoves.size());
		
		move=possibleMoves.get(selection).getlastMove(); 
		
		System.out.print("My move: ");
		if(move==null){
			System.out.println("ALERT");
		}
		try {
			changeRoot(move);
		} catch (NullPointerException e) {
			
			System.out.println("children size: " + root.getChildren().size() + "\nTried to find value " + value + " but children had values:");
			
			for(i = 0; i < root.getChildren().size(); i++){
				
				System.out.println(root.getChildren().get(i).getMinmaxValue());
			}
			
			e.printStackTrace();
		}
		return move;
	}
	
	
	private float MiniMaxing(State node,boolean MaximizingPlayer){
		float val=0;
		float bestVal=0;
		int i=0;
		
		if (node.getChildren().isEmpty() || node.isTerminal()) return node.getEvaluation();
		
		if (MaximizingPlayer){
			bestVal=Integer.MIN_VALUE;// praktika meiwn apeiro
			
			for(i=0;i<node.getChildren().size();i++){
				
				val=MiniMaxing(node.getChildren().get(i),false);
				if (val>bestVal)bestVal=val;	
			}
			
		}
		else
		{
			bestVal=Integer.MAX_VALUE;// praktika apeiro
			
			for(i=0;i<node.getChildren().size();i++){
				
				val=MiniMaxing(node.getChildren().get(i),true);
				if (val<bestVal)  bestVal=val;
			}
		}
		
		node.setMinmaxValue(bestVal);
		return bestVal;
		
	}
	
	private float MiniMaxing(int depth,State node,boolean MaximizingPlayer){
		float val=0;
		float bestVal=0;
		int i=0;
		
		if (depth==0||node.getChildren().isEmpty() || node.isTerminal()) return node.getEvaluation();
		
		if (MaximizingPlayer){
			bestVal=Integer.MIN_VALUE;// praktika meiwn apeiro
			
			for(i=0;i<node.getChildren().size();i++){
				
				val=MiniMaxing(depth-1,node.getChildren().get(i),false);
				if (val>bestVal)bestVal=val;	
			}
			
		}
		else
		{
			bestVal=Integer.MAX_VALUE;// praktika apeiro
			
			for(i=0;i<node.getChildren().size();i++){
				
				val=MiniMaxing(depth-1,node.getChildren().get(i),true);
				if (val<bestVal)  bestVal=val;
			}
		}
		
		node.setMinmaxValue(bestVal);
		return bestVal;
		
	}
	

	
	private float abPrunning(State node,boolean MaximizingPlayer,float a, float b, float percentage){
		float val=0;
		int i=0;
		
		if (node.getChildren().isEmpty() || node.isTerminal()) {
			
//			System.out.println("leaf : Possible value: " + node.getEvaluation());
			node.setMinmaxValue(((10 - percentage) / 10) * node.getEvaluation());
			return ((10 - percentage) / 10) * node.getEvaluation();
		}
		if (MaximizingPlayer){
			
			val=Integer.MIN_VALUE;// praktika meiwn apeiro
			
			for(i=0;i<node.getChildren().size();i++){
				
				val=Math.max(val, abPrunning(node.getChildren().get(i),false,a,b, (percentage + (float)1.5)));
				a=Math.max(a, val);
				if (b<=a) break;
			}
		}
		else{
			val=Integer.MAX_VALUE;// praktika apeiro

			for(i=0;i<node.getChildren().size();i++){
				
				val=Math.min(val,abPrunning(node.getChildren().get(i),true,a,b, percentage));
				b=Math.min(b,val);
				if (b<=a) break;
			}
			
		}
//		System.out.println("node : Possible value: " + val);
		System.out.println("Ahoy " + ((10 - percentage) / 10) * val);
		node.setMinmaxValue(((10 - percentage) / 10) * val);
		return ((10 - percentage) / 10) * val ;
	}

	
	private float abPrunning(int depth,State node,boolean MaximizingPlayer,float a, float b){
		float val=0;
		int i=0;
		
		if (depth==0||node.getChildren().isEmpty() || node.isTerminal()) return node.getEvaluation();
		if (MaximizingPlayer){
			
			val=Integer.MIN_VALUE;// praktika meiwn apeiro
			
			for(i=0;i<node.getChildren().size();i++){
				
				val=Math.max(val, abPrunning(depth-1,node.getChildren().get(i),false,a,b));
				a=Math.max(a, val);
				if (b<=a) break;
			}
		}
		else{
			val=Integer.MAX_VALUE;// praktika apeiro

			for(i=0;i<node.getChildren().size();i++){
				
				val=Math.min(val,abPrunning(depth-1,node.getChildren().get(i),true,a,b));
				b=Math.min(b,val);
				if (b<=a) break;
			}
			
		}
		
		node.setMinmaxValue(val);
		return val;
	}
	
	
	
	public void changeRoot(int[] move){
		
		int i;
		if(move.equals(null)){
			System.out.println("ALERT");
		}
//		System.out.println(move.length);
		for(i = 0; i < root.getChildren().size(); i++){
			
			if(Arrays.equals(move, root.getChildren().get(i).getlastMove())){
				System.out.println("Changed root for move " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
				root = root.getChildren().get(i);
				root.removeFather();
				root.getChildren().clear(); 
				root.clearScores();
				expand_list.clear();
				expand_list.add(root);
//				createTree(delay, treeDepth);
				return;
			}
		}
		
		root.setBoard(makeMove(root.getBoard(), move));
		root.getChildren().clear();
		root.setlastPlayed(nextPlayer(root));
		root.clearScores();
		expand_list.clear();
		expand_list.add(root);
//		createTree(delay, treeDepth);
		
		System.out.println("(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")" + ": Panic mode forced root!");
	}
	
 	private ArrayList<int[]> whiteMoves(String[][] board)
	{
		String firstLetter = "";
		String secondLetter = "";
//		String move = "";
		int[] move;
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j;
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
						availableMoves.add(move);
						continue;
					}
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
//						move = Integer.toString(i) + Integer.toString(j) + 
//							   Integer.toString(i-1) + Integer.toString(j);
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j;
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j - 1;
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i - 1;
						move[3] = j + 1;
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i - (k + 1);
						move[3] = j;
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i + (k + 1);
						move[3] = j;
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i;
						move[3] = (j - (k + 1));
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
						move  = new int[4];
						move[0] = i;
						move[1] = j;
						move[2] = i;
						move[3] = (j + (k + 1));
//						System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
							move  = new int[4];
							move[0] = i;
							move[1] = j;
							move[2] = i - 1;
							move[3] = j;
//							System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
							move  = new int[4];
							move[0] = i;
							move[1] = j;
							move[2] = i + 1;
							move[3] = j;
//							System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
							move  = new int[4];
							move[0] = i;
							move[1] = j;
							move[2] = i;
							move[3] = j - 1;
//							System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
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
							move  = new int[4];
							move[0] = i;
							move[1] = j;
							move[2] = i;
							move[3] = j + 1;
							
//							System.out.println("Added " + "(" + move[0] + "," + move[1] + ")->(" + move[2] + "," + move[3] + ")");
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
		int i;
//		for(i = 0; i < availableMoves.size(); i++){ System.out.println(Arrays.toString(availableMoves.get(i))); }
		return(availableMoves);
	}
	
	private ArrayList<int[]> blackMoves(String board[][])
	{
		String firstLetter = "";
		String secondLetter = "";
		//String move = "";
		int[] move;
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
						move  = new int[4];
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
						move  = new int[4];
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
						move  = new int[4];
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
						move  = new int[4];
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
						move  = new int[4];
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
						move  = new int[4];
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
						move  = new int[4];
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
						move  = new int[4];
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
							move  = new int[4];
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
							move  = new int[4];
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
							move  = new int[4];
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
							move  = new int[4];
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
	
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	
	//int x1, int y1, int x2, int y2
	public String[][] makeMove(String[][] board, int[] moves)
	{
		String chesspart;
		try {
			chesspart = Character.toString(board[moves[0]][moves[1]].charAt(1));
//		System.out.println(Arrays.toString(moves));
		boolean pawnLastRow = false;
		String[][] newBoard = new String[rows][columns];
		int i, j;
		
		for(i = 0; i < rows; i++){
			
			for(j = 0; j < columns; j++){
				
				newBoard[i][j] = board[i][j];
			}
		}
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (moves[0]==rows-2 && moves[2]==rows-1) || (moves[0]==1 && moves[2]==0) )
			{
				newBoard[moves[2]][moves[3]] = " ";	// in case an opponent's chess part has just been captured
				newBoard[moves[0]][moves[1]] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			newBoard[moves[2]][moves[3]] = board[moves[0]][moves[1]];
			newBoard[moves[0]][moves[1]] = " ";
		}
		
		return newBoard;
		}catch (StringIndexOutOfBoundsException e) {
			
			System.out.println("Tried to read from string \"" + board[moves[0]][moves[1]] + "\"");
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void prizeAdded(int prizeX, int prizeY){
		
		expand_list.clear();
		System.out.println("Dropped tree, added prize");
		root.getBoard()[prizeX][prizeY] = "P";
		root.getChildren().clear();
		expand_list.add(root);
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

