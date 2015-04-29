package source;

public class State {
	
	private String[][] board;
	private int scoreWhite=0;
	private int scoreBlack=0;
	private int lastPlayed=1;/// antistoixa me to color. arxikopoieitai sto 1 giati prwtos paizei o white
	private int[] lastMove;
	
	private State father;
	
	
	public State(String[][] board,State father,int lastPlayed,int[] lastMove,int myColor){
		this.board=board;
		this.father=father;
		this.lastPlayed=lastPlayed;
		this.lastMove=lastMove;
		
		//setScoreWhite();
		//setScoreBlack();
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
				sc_incr+=3;
			}
			
			
			
			
		}
		
		scoreWhite= father_score+sc_incr;
		
		
	}
	
	public void setScoreBlack(State father,int lastPlayed,int LastMove){
		
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
	
	public String[][] getBoard(){
		
		return board;
	}
	
	public int evaluate(State board){
		int value=0;
		
		return value;
	}
	
}
