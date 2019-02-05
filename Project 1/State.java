public class State{
	int[][] board;
	int player;
	private int[] heur;  // heuristic values for the person and computer players respectively

	State(int h, int w){
		board = new int[h][w];
		heur = new int[2];
		player = 1;
	}

	/* Returns deep copy of state */
	State copy(int h, int w){
		State newState = new State(h, w);
		newState.heur = heur.clone();
		newState.player = player;
		for(int i = 0; i < board.length; i++){
			newState.board[i] = board[i].clone(); 
		}
		return newState;
	}

	void adjustHeuristic(int p){
		heur[p-1] += 1;
	}

	/* Heuristic used by hMinimax */
	double heuristic(){
		int m = Math.max(1, heur[0] + heur[1]);
		return player == 1 ? heur[0]/(double)m : -heur[1]/(double)m;
	} 

	/* Check if column is full */
	boolean isFull(int col){
		return board[0][col] != 0;
	}
}