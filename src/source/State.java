package source;

import java.util.ArrayList;

public class State {
	
	private int rows = 7;
	private int columns = 5;
	
	private String[][] board;
	private int scoreWhite=0;
	private int scoreBlack=0;
	private int lastPlayed=1;/// antistoixa me to color. arxikopoieitai sto 1 giati prwtos paizei o white
	private int[] lastMove;
	private ArrayList<State> children = null;
	private State father;
	private int evaluation;
	
	
	public State(String[][] board,State father,int lastPlayed,int[] lastMove,int myColor){
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
	}
	
	//public State(){}///gia to arxiko state
	
	public boolean isTerminal(){//elegxei an ayto to state einai teliko state paixnidiou.diladi an leipei 
		int i,j; 				//kapoios  apo tous 2 vasiliades h an exoun meinei monoi tous sto board
		boolean Wking=false,Bking=false,OnlyKings=true;
		
		for (i=0;i<rows;i++) {
			for (j=0;j<columns;j++){
				if(board[i][j].equals("WK")) Wking=true;
				else if(board[i][j].equals("BK")) Bking=true;
				else if (board[i][j].charAt(1)=='R'||board[i][j].charAt(1)=='P')OnlyKings=false;
			}
		}
		
		if(Wking==false||Bking==false|| OnlyKings==true) return true; // synthikes termatismou
		else return false;
	}
				
	
	
	public void setScoreWhite(State father,int[] lastMove){
		
		int father_score=father.getScoreWhite();
		int sc_incr=0;
		
			//score++
			
			if(father.getBoard()[lastMove[0]][lastMove[1]].charAt(1)=='P'){
				
				if(board[lastMove[2]][lastMove[3]].equals(" "))
				{
					sc_incr++;
				}
			}
			
			if(father.getBoard()[lastMove[2]][lastMove[3]].equals("BP")){
				sc_incr+=1;
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("BR")){
				sc_incr+=3;
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("BK")){
				sc_incr+=10;
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("P")){
				sc_incr+=0.8;
			}
		
		scoreWhite= father_score+sc_incr;
		
	}
	
	public void setScoreBlack(State father,int[] lastMove){
		
		int father_score=father.getScoreBlack();
		int sc_incr=0;
		
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
				sc_incr+=10;
			}
			else if (father.getBoard()[lastMove[2]][lastMove[3]].equals("P")){
				sc_incr+=0.8;
			}	
		
		scoreBlack= father_score+sc_incr;
		
	}


	public int getScoreWhite() { 
		return scoreWhite;
	}

	public int getScoreBlack() {
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
	
	
	
	public int evaluate(int myColor){//first simple evaluation
		int value=0,i,j;
		int blackPieces=0,whitePieces=0;
		// count pieces
		
			for (i=0;i<rows;i++) {
				for (j=0;j<columns;j++){
					
					if(board[i][j].charAt(0)=='B'){
						blackPieces++;
					}
					else if(board[i][j].charAt(0)=='W'){
						whitePieces++;
					}
					
				}
			}
			
			if (myColor==0){//whites perspective
				value=Math.abs(scoreWhite-whitePieces)-Math.abs(scoreBlack-blackPieces);
			}
			else{//blacks perspective
				value=Math.abs(scoreBlack-blackPieces)-Math.abs(scoreWhite-whitePieces);
			}
		return value;
	}
	
	
	
	public State getFather(){
		 return father;
	}
	
}
