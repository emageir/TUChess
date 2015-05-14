package source;

import java.util.ArrayList;

public class State {
	
	private int rows = 7;
	private int columns = 5;
	
	private String[][] board;
	private float scoreWhite=0;
	private float scoreBlack=0;
	private int lastPlayed=1;/// antistoixa me to color. arxikopoieitai sto 1 giati prwtos paizei o white
	private int[] lastMove;
	private ArrayList<State> children = null;
	private State father;
	private float evaluation;
	private double minmaxValue;
	private int depth;
	private int absoluteScoreWhite;
	private int absoluteScoreBlack;
	
	
	public double getMinmaxValue() {
		return minmaxValue;
	}

	public void setMinmaxValue(double bestVal) {
		this.minmaxValue = bestVal;
	}

	public State(String[][] board,State father,int lastPlayed,int[] lastMove,int myColor, int depth, int absoluteScoreWhite, int absoluteScoreBlack){
		this.board=board;
		this.father=father;
		this.lastPlayed=lastPlayed;
		this.lastMove=lastMove;
		
		if(father==null){
			scoreBlack=0;
			scoreWhite=0;
		}
		else{
			if (lastPlayed==0){
			setScoreWhite(father,lastMove);
			scoreBlack=father.getScoreBlack();
			}
			else{
			setScoreBlack(father,lastMove);
			scoreWhite=father.getScoreWhite();
			}
		}
		
		evaluation=evaluate(myColor);

		this.children = new ArrayList<State>();
		this.depth = depth;
		this.absoluteScoreWhite = absoluteScoreWhite;
		this.absoluteScoreBlack = absoluteScoreBlack;
	}
	
	//public State(){}///gia to arxiko state
	
	public boolean isTerminal(){//elegxei an ayto to state einai teliko state paixnidiou.diladi an leipei 
		int i,j; 				//kapoios  apo tous 2 vasiliades h an exoun meinei monoi tous sto board
		boolean Wking=false,Bking=false,OnlyKings=true;
		
		for (i=0;i<rows;i++) {
			for (j=0;j<columns;j++){
				if(board[i][j].equals("WK")) Wking=true;
				else if(board[i][j].equals("BK")) Bking=true;
				//else if (board[i][j].charAt(1)=='R'||board[i][j].charAt(1)=='P')OnlyKings=false;
				else if(board[i][j].compareTo(" ") != 0 && board[i][j].compareTo("P") != 0){
					
					OnlyKings = false;
				}
			}
		}
		
		if(Wking==false||Bking==false|| OnlyKings==true) return true; // synthikes termatismou
		else return false;
	}
				
	
	
	public void setScoreWhite(State father,int[] lastMove){
		
		float father_score=father.getScoreWhite();
		float sc_incr=0;
		
			//score++
			
			if(father.getBoard()[lastMove[0]][lastMove[1]].charAt(1)=='P'){
				
				if(board[lastMove[2]][lastMove[3]].equals(" "))
				{
					sc_incr++;
//					System.out.println("Out of board");
				}
			}
			
			if(father.getBoard()[lastMove[2]][lastMove[3]].equals("BP")){
				sc_incr+=1;
//				System.out.println("Killed pawn");
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("BR")){
				sc_incr+=3;
//				System.out.println("Killed rook");
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("BK")){
				if(absoluteScoreWhite+ 10 > absoluteScoreBlack){
					
					sc_incr+=1000;
				}
				else{
					
					sc_incr -= 1000;
				}
//				System.out.println("Killed King");
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("P")){
				sc_incr+=0.8;
//				System.out.println("Got present on (" + lastMove[2] + "," + lastMove[3]);
			}
		
		scoreWhite= father_score+sc_incr;
		
	}
	
	public void setScoreBlack(State father,int[] lastMove){
		
		float father_score=father.getScoreBlack();
		float sc_incr=0;
		
			//score++
			
			if(father.getBoard()[lastMove[0]][lastMove[1]].charAt(1)=='P'){
				
				if(board[lastMove[2]][lastMove[3]].equals(" "))
				{
					sc_incr++;
				}
			}
			
			if(father.getBoard()[lastMove[2]][lastMove[3]].equals("WP")){
				sc_incr+=1;
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("WR")){
				sc_incr+=3;
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("WK")){
				if(absoluteScoreBlack + 10 > absoluteScoreWhite){
					
					sc_incr+=1000;
				}
				else{
					
					sc_incr -= 1000;
				}
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("P")){
				sc_incr+=0.8;
			}	
		
		scoreBlack= father_score+sc_incr;
		
	}


	public float getScoreWhite() { 
		return scoreWhite;
	}

	public float getScoreBlack() {
		return scoreBlack;
	}
	
	public String[][] getBoard(){
		return board;
	}
	
	public int getlastPlayed(){
		
		return lastPlayed;
	}
	
	public ArrayList<State> getChildren(){
		
		return children;
	}
	
	
	
	public float evaluate(int myColor){//first simple evaluation
		float value=0;
		int i,j;
		int blackPieces=0,whitePieces=0;
		
		int DoubledW=0,DoubledB=0,IsolW=0,IsolB=0,BlockW=0,BlockB=0;
		boolean emptycolW;
		int posibleIsolW=0;
		boolean emptycolB;
		int posibleIsolB=0;
		int pawnStructW=0;
		int pawnStructB=0;
		
			for (i=0;i<columns;i++) {
				emptycolW=true;
				emptycolB=true;
				
				for (j=0;j<rows;j++){
					
					if(board[j][i].charAt(0)=='B'){
						blackPieces++;
						
						if (board[j][i].equals("BP"))
							{
							if(emptycolB==false)DoubledB+=1;
							emptycolB=false;
							if (board[j+1][i].charAt(0)=='W') BlockB+=1;
							}
					}
					else if(board[j][i].charAt(0)=='W'){
						whitePieces++;
								
						if (board[j][i].equals("WP"))
							{
							if(emptycolW==false)DoubledW+=1;
							emptycolW=false;
							if (board[j-1][i].charAt(0)=='B') BlockW+=1;
							}
				    }
				}
				
				if(emptycolW==true) {
					if (posibleIsolW==2) IsolW+=1;// an einai keno kai prin synanthses pioni
					posibleIsolW=1;	
				}
				else{
					
					if (posibleIsolW==1)posibleIsolW=2;
					else{
						posibleIsolW=0;
					}
				}
				
				if(emptycolB==true) {
					if (posibleIsolB==2) IsolB+=1;// an einai keno kai prin synanthses pioni
					posibleIsolB=1;	
				}
				else{
					if (posibleIsolB==1)posibleIsolB=2;
					else{
						posibleIsolB=0;
					}
				}

			}// end of columns loop
	
			pawnStructW=DoubledW+IsolW+BlockW;
			pawnStructB=DoubledB+IsolB+BlockB;
			
			
			if (myColor==0){//whites perspective
				
				value=Math.abs(scoreWhite+whitePieces)-Math.abs(scoreBlack+blackPieces)-(float)0.5*(pawnStructW-pawnStructB);
			}
			else{//blacks perspective
				value=Math.abs(scoreBlack+blackPieces)-Math.abs(scoreWhite+whitePieces)-(float)0.5*(pawnStructB-pawnStructW);
			}
		return value;
	}
	
	
	
	public State getFather(){
		 return father;
	}

	public int[] getlastMove() {
		return lastMove;
	}

	public float getEvaluation() {
		return evaluation;
	}
	
	public void removeFather(){
		
		this.father = null;
	}
	
	public void setBoard(String[][] board){
		
		this.board = board;
	}
	
	public int getDepth(){
		
		return depth;
	}
	
	public void setlastPlayed(int lastPlayed){
		
		this.lastPlayed = lastPlayed;
	}
	
	public void clearScores(){
		
		this.scoreBlack = 0;
		this.scoreWhite = 0;
	}
	
	
}
