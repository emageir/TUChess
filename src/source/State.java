package source;

public class State {
	
	private String board[][];
	private int scoreWhite;
	private int scoreBlack;
	private int lastPlayed=1;/// antistoixa me to colour. arxikopoieitai sto 1 giati prwtos paizei o white
	private int lastMove[];
	
	private State father;
	
	
	public State(String board[][],State father,int lastPlayed,int lastMove[]){
		this.board=board;
		this.father=father;
		this.lastPlayed=lastPlayed;
		this.lastMove=lastMove;
		
		
		setScoreWhite();
		setScoreBlack();
		
	}
	
	
	public void setScoreWhite(){}
	
	public void setScoreBlack(){}
	
	
	public int evaluate(State board){
		int value=0;
		
		return value;
	}
	
}