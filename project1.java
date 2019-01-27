/*
interface gameBoard{

	void displayBoard();

}
*/

class Game implements gameBoard{

	class State{
		int[][] board;
		int player; //whose turn is it

		State(int w, int h){
			board = new int[w][h];
			player = 1;
		}

		State copy(State s, int w, int h){
			State newState = new State(w, h);
			newState.player = s.player;
			for(int i = 0; i < s.board.length; i++){
				newState.board[i] = s.board[i].clone(); 
			}
			return newState;
		}
	}

	/* Adding a game piece to column col */
	class Action{
		int col;
		Action(int j){col = j;}
	}

	class Opponent{
	// random, minimax, minimax with alpha-beta pruning, H-minimax with a fixed depth cutoff

		Action getMove(){} //returns action of opponent in state s0

		class Node{
		Action a;
		int val;

		Node(Action act, int v){
			a = act;
			val = v;
		}
		}

		Node maxVal(Node s, int alpha, int beta){
			int v = -1;
			Action optimal;
			for(Action a: applicableActions(s)){
				if( terminalTest(s, getRow(a.col), a.col) ){ return new Node(a, 1); }

				Node n = maxVal(result(a, s.player), alpha, beta);

				if(n.val > v){
					v = n.val; 
					optimal = n.a;
				}

				if(v >= beta){ break; }
				alpha = max(alpha, v);
			}
			return new Node(optimal, v);
		}

		Node minVal(Node s, int alpha, int beta){
			int v = 1;
			Action optimal;
			for(Action a: applicableActions(s)){

				if( terminalTest(s, getRow(a.col), a.col) ){ return new Node(a, -1); }

				Node n = minVal(result(a, s.player), alpha, beta);

				if(n.val < v){
					v = n.val; 
					optimal = n.a;
				}

				if(v <= alpha){ break; }
				beta = min(beta, v);
			}
			return new Node(optimal, v);
		}

		Node minVal(Node s){
			int v = 1;
			Action optimal;
			for(Action a: applicableActions(s)){
				if( terminalTest(s, getRow(a.col), a.col) ){ return new Node(a, -1); }

				Node n = maxVal(result(a, s.player));

				if(n.val < v){
					v = n.val; 
					optimal = n.a;
				}
			}
			return new Node(optimal, v);
		}

		Node maxVal(Node s){
			int v = -1;
			Action optimal;
			State r;
			for(Action a: applicableActions(s)){
				r = result(a, s.player);
				if( terminalTest(r, getRow(a.col), a.col) ){ return new Node(a, 1); }

				Node n = minVal(r, alpha, beta);

				if(n.val > v){
					v = n.val; 
					optimal = n.a;
				}
			}
			return new Node(optimal, v);
		}

		Action minimax(Node s, Action a){
			Set<Action> actSet = applicable_Actions();

			if(s.player == 1){

			}else{

			
		}

		Action alphaBetaPrune(State s){
			return maxVal(s, -1, 1).a ; //or whoevers turn it is, idk???
		}

		Action hminimax(State s){

		}
	}

	State s0;
	int m, n, k;
	Action[] actions;

	Game(int width, int height, int in_a_row){
		m = height;
		n = width;
		k = in_a_row;
		s = new State(width, height);
		actions = new Action[width];
		for(int i = 0; i < width; i++){
			actions[i] = new Action(i);
		} 
	}

	void displayBoard(){
		/* iterate through board - X's for P1 and O's for P2 */
		for(int[] row: s.board){
			for(int entry: row){
				if(entry == 1){
					System.out.print("X");
				}else if(entry == 2){
					System.out.print("O");
				}else{
					System.out.print(" ");
				}
			}
		}
	}

	/* Returns set of applicable actions in current state s */
	Set<Action> applicableActions(State s)
	{
		Set<Action> applicable = new HashSet<Action>()
		for (Action a: actions){
			if(a.player == s.player && s.board[0][a.col] == 0){
				applicable.add(a);
			}
		}
		return applicable;
	}

	/* Computes the state which would result from applying action a in s0 */
	State result(Action a, int player)
	{
		int c = a.col;
		int r = getRow(c);

		State newState = copy(s0);
		newState.board[r][c] = player;
		return newState;
	}

	int getRow(int col){
		int i = 0;
		while(i < m-1 && s0.board[i+1][c] == 0){
			i++;
		}
		return i;
	}

	/* Modifies state s of the game given action a (assumes a is applicable in current state) */
	void applyAction(Action a, int player){
		int c = a.col;
		int r = getRow(c);
		s0.board[r][c] = player;
	}

	/* Check if move (i,j) in state s makes k in-a-row for s.player */
	boolean terminalTest(State s, int i, int j){
		int player = s.board[i][j];
		int length = 1;

		//test for ties for(i = 0; i < s[0].length; i++){ if all are not zeros, return tie}

		//check horizontal
		boolean left = true, right = true;
		int c1 = j, c2 = j;

		while((left || right) && length <= k){
			if(left){ 
				if(c1 > 0 & s.board[i][c1--] == player){length++;}
				else{left = false;} 
			}
			if(right){
				if(c2 < n-1 & s.board[i][c2++] == player){length++;}
				else{right = false;}
			}
		}

		if(length == k){return true;}

		//check vertical
		length = 1;
		boolean up = true, down = true;
		int r1 = i, r2 = i;

		while((up || down) && length <= k){
			if(down){ 
				if(r < m-1 & s.board[r++][j] == player){length++;}
				else{down = false;} 
			}
			if(up){
				if(r > 0 & s.board[r--][j] == player){length++;}
				else{up = false;}
			}
		}

		if(length == k){return true;}
		
		//check diagonals
		length = 1;
		boolean nW = true, sW = true, sE = true, nE = true;
		r1 = i, r2 = i, c1 = j, c2 = j;
		while((nW || sE) && length <= k){
			if(nW){ 
				if(r1 > 0 & c1 > 0 & s.board[r1--][c1--] == player){length++;}
				else{nW = false;} 
			}
			if(sE){
				if(r2 < m-1 & c2 < n-1 & s.board[r2++][c2++] == player){length++;}
				else{sE = false;}
			}
		}

		if(length == k){return true;}

		length = 1;
		r1 = i, r2 = i, c1 = j, c2 = j;
		while((nE || sW) && length <= k){
			if(nE){ 
				if(r1 > 0 & c1 < n-1 & s.board[r1--][c1++] == player){length++;}
				else{nE = false;} 
			}
			if(sW){
				if(r2 < m-1 & c2 > 0  & s.board[r2++][c2--] == player){length++;}
				else{sE = false;}
			}
		}
		return length == k;
	}

	void printRules(){ System.out.println(""); }

	public static void main(String[] args){
		/* Get user input */
		printRules();

		/* Run 3x3x3 */
		Game tictactoe = new Game(3,3,3);
		//get user input
		//get opponent move according to mode

		/* Run 6x7x4 */
		Game ssf = new Game(6,7,4);

	}
}