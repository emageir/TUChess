package source;

import java.util.ArrayList;

public class State {
	
	private String[][] board;
	private int scoreWhite=0;
	private int scoreBlack=0;
	private int lastPlayed=1;/// antistoixa me to color. arxikopoieitai sto 1 giati prwtos paizei o white
	private int[] lastMove;
	private ArrayList<State> children = null;
	private State father;
	
	
	public State(String[][] board,State father,int lastPlayed,int[] lastMove,int myColor){
		this.board=board;
		this.father=father;
		this.lastPlayed=lastPlayed;
		this.lastMove=lastMove;
		
		setScoreWhite(father,lastPlayed,lastMove);
		setScoreBlack(father,lastPlayed,lastMove);

		this.children = new ArrayList<State>();
	}
	
	//public State(){}///gia to arxiko state
	
	
	public void setScoreWhite(State father,int lastPlayed,int[] lastMove){
		
		int father_score=father.getScoreWhite();
		int sc_incr=0;
		
		if (lastPlayed==0){
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
			
		}
		
		scoreWhite= father_score+sc_incr;
		
	}
	
	public void setScoreBlack(State father,int lastPlayed,int[] lastMove){
		
		int father_score=father.getScoreBlack();
		int sc_incr=0;
		
		if (lastPlayed==1){
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
	
	public int evaluate(State board){
		int value=0;
		
		return value;
	}
	
}
