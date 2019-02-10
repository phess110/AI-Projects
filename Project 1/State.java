public class State{
	private int[][] board;
	int player;

	State(int h, int w){
		board = new int[h][w];
		player = 1;
	}

	/* Returns deep copy of state */
	State copy(int h, int w){
		State newState = new State(h, w);
		newState.player = player;
		for(int i = 0; i < board.length; i++){
			newState.board[i] = board[i].clone(); 
		}
		return newState;
	}

	int[][] getBoard(){
		return board;
	}

	/* Check if column is full */
	boolean isFull(int col){
		return board[0][col] != 0;
	}

	int entry(int i, int j){
		return board[i][j];
	}

	void setEntry(int i, int j, int p){
		board[i][j] = p;
	}

	void setPlayer(int p){
		player = p;
	}

	/*
	void display(){
		for(int[] row: board){
			System.out.print("|");
			for(int entry: row){
				if(entry == 1){ System.out.print("X "); }
				else if(entry == 2){ System.out.print("O "); }
				else{ System.out.print("_ "); }
			}
			System.out.println("|");
		}
		System.out.print("\n");
	}
	*/
}