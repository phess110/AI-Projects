import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class Game{
	private State s0;
	private final int height, width, in_a_row; //height, width, and in-a-row dimensions
	private Action[] actions;

	Game(int m, int n, int k){
		height = m;
		width = n;
		in_a_row = k;
		s0 = new State(n, m);
		actions = new Action[n];
		for(int i = 0; i < n; i++){
			actions[i] = new Action(i);
		} 
	}

	Game(int m, int n, int k, int depth){
		height = m;
		width = n;
		in_a_row = k;
		s0 = new State(m, n);
		actions = new Action[n];
		for(int i = 0; i < n; i++){
			actions[i] = new Action(i);
		}
	}

	State getState(){
		return s0;
	}

	/* iterate through board - X's for P1 and O's for P2. O(nm) */
	void displayBoard(){
		for(int[] row: s0.board){
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

	/* returns set of applicable actions in current state s. O(n) */
	Set<Action> applicableActions(State s){
		Set<Action> applicable = new HashSet<Action>();
		for (Action a: actions){
			if(!s.isFull(a.col)){
				applicable.add(a);
			}
		}
		return applicable;
	}

	/* computes the state which would result from applying action a in s0. O(nm) */
	State result(Action a, int player){
		State newState = s0.copy(height, width);
		newState.player = other(s0.player);
		newState.board[getRow(a.col)][a.col] = player; 
		return newState;
	}

	/* modifies state s0 of the game given action a (assumes a is applicable in current state) O(m) */
	void applyAction(Action a, int player){
		s0.board[getRow(a.col)][a.col] = player;
		s0.player = other(s0.player);
	}

	/* Returns row no. of empty slot in column c or -1 if the column is full. O(m) */
	int getRow(int c){
		if(s0.isFull(c)){ return -1; }
		int i = 1;
		while(i < height - 1 && s0.board[i+1][c] == 0){
			i++;
		}
		return i;
	}

	/* Returns signature of other player */
	static int other(int p){
		return p == 1 ? 2 : 1;
	}

	/* 
		Check if the move (i,j) made k in-a-row
		Returns whether the board in state s is a win, loss, draw, or nota (none-of-the-above) for player 1.
		O(n+k) 
	*/
	Outcome terminalTest(State s, int i, int j){
		int player = s.board[i][j];
		Outcome o = (player == 1 ? Outcome.WIN : Outcome.LOSS);
		if(lowerDiag(s,player,i,j) == in_a_row-1 || upperDiag(s,player,i,j) == in_a_row-1 || horizontal(s,player,i,j) == in_a_row-1 || vertical(s,player,i,j) == in_a_row-1)
		{ return o; }
		return isTie(s);
	}

	Outcome isTie(State s){
		for(int k = 0; k < width; k++){ 
			if( !s.isFull(k) ){ return Outcome.DRAW; }
		}
		return Outcome.DRAW;
	}

	/* 
	A good sequence for player i is a consecutive sequence of (in_a_row-2) of i's tiles on the board.
	A good move is any move builds on, or creates, a good sequence. The heuristic counts the number of good moves. 
	This function updates the number of good moves for the human + computer after move (i,j) -> state s. Returns the outcome of state s.
	*/ 
	void updateHeuristic(State s, int player){
		for(int c = 0; c < width; c++){
			for(int r = getRow(c); r >= 0; r++){
				update(s, player, r, c, 1);
				update(s, other(player), r, c, -1);
				if(c > 0 && c < width-1 && s.board[r][c-1] == 0 && s.board[r][c+1] == 0){
					break;
				}else if(c == width-1 && s.board[r][c-1] == 0){
					break; 
				}else if(c == 0 && s.board[r][c+1] == 0 ){
					break;
				}
			}
		}
	}

	void update(State s, int player, int i, int j, int val){
		if(	lowerDiag(s,player,i,j) == in_a_row-1 	|| upperDiag(s,player,i,j) == in_a_row-1 
			|| horizontal(s,player,i,j) == in_a_row-1 || vertical(s,player,i,j) == in_a_row-1){
			s.adjustHeuristic(player, val);
		}
	}

	/* Length of longest sequence of player's tiles, going through square (i,j) along the upper diag. */	
	int upperDiag(State s, int player, int i, int j){
		int length = 0;
		boolean sW = true, nE = true;
		int r1 = i, r2 = i, c1 = j, c2 = j;
		while((nE || sW) && length <= in_a_row){ //upper diagonal
			if(nE){ 
				if(r1 > 0 && c1 < width-1 && s.board[--r1][++c1] == player){length++;}
				else{nE = false;} 
			}
			if(sW){
				if(r2 < height-1 && c2 > 0  && s.board[++r2][--c2] == player){length++;}
				else{sW = false;}
			}
		}
		return length;
	}

	int lowerDiag(State s, int player, int i, int j){
		int length = 0;
		boolean nW = true, sE = true;
		int r1 = i, r2 = i, c1 = j, c2 = j;
		while((nW || sE) && length <= in_a_row){ //down diagonal
			if(nW){ 
				if(r1 > 0 && c1 > 0 && s.board[--r1][--c1] == player){length++;}
				else{nW = false;} 
			}
			if(sE){
				if(r2 < height-1 && c2 < width-1 && s.board[++r2][++c2] == player){length++;}
				else{sE = false;}
			}
		}
		return length;
	}

	int vertical(State s, int player, int i, int j){
		int length = 0;
		boolean up = true, down = true;
		int r1 = i, r2 = i;
		while((up || down) && length <= in_a_row){
			if(down){ 
				if(r1 < height-1 && s.board[++r1][j] == player){length++;}
				else{down = false;} 
			}
			if(up){
				if(r2 > 0 && s.board[--r2][j] == player){length++;}
				else{up = false;}
			}
		}
		return length;
	}

	int horizontal(State s, int player, int i, int j){
		int length = 0;
		boolean left = true, right = true;
		int c1 = j, c2 = j;
		while((left || right) && length <= in_a_row){
			if(left){ 
				if(c1 > 0 && s.board[i][--c1] == player){length++;}
				else{left = false;} 
			}
			if(right){
				if(c2 < width-1 && s.board[i][++c2] == player){length++;}
				else{right = false;}
			}
		}
		return length;
	}

	/* Utility of outcome */
	static int utility(Outcome o){
		switch(o){
			case WIN: return 1;
			case LOSS: return -1;
			default: return 0; 
		}
	}

	static void printRules(){ System.out.println(""); }

	public class Opponent{
		private int maxDepth;
		private int mode;

		Opponent(int m){
			mode = m;
		}

		Opponent(int d, int m){
			maxDepth = d;
			mode = m;
		}

		//returns action of opponent in state s0
		Action getMove(Game g){
			switch(mode) {
				case 1: return minimax(g);
				case 2:	return alphaBetaPrune(g);
				case 3:	return hminimax(g);
				default: return randomAction(g);
			}
		}

		Action randomAction(Game g){ /* todo */ return new Action(1); }

		Action minimax(Game g){ return g.minVal(g.getState()).getAction(); }

		Action alphaBetaPrune(Game g){ return g.minVal(g.getState(), -2, 2).getAction(); } // maxVal/minVal?

		Action hminimax(Game g){ return g.minVal(g.getState(), 0, maxDepth).getAction(); }
	}

	class Node{ // in search tree
		private Action a;
		double val;

		Node(Action act, double v){
			a = act;
			val = v;
		}

		Action getAction(){
			return a;
		}
		
		//sets this to minimum of n and this 
		void min(Node n){
			if(n.val < this.val){
				this.val = n.val;
				this.a = n.a;
			}
		}

		void max(Node n){
			if(n.val > this.val){
				this.val = n.val;
				this.a = n.a;
			}
		}
	}

	Node maxVal(State s, int alpha, int beta){
		Node optimal = new Node(null, -2);
		State r;
		for(Action a: applicableActions(s)){
			r = result(a, s.player);
			Outcome o = terminalTest(r, getRow(a.col), a.col);
			if( o != Outcome.DRAW ){ return new Node(a, utility(o)); }
			optimal.max(minVal(r, alpha, beta));
			if(optimal.val >= beta){ break; } // min has a better choice
			alpha = Math.max(alpha, (int)optimal.val);
		}
		return optimal;
	}

	Node minVal(State s, int alpha, int beta){
		Node optimal = new Node(null, 2);
		State r;
		for(Action a: applicableActions(s)){
			r = result(a, s.player);
			Outcome o = terminalTest(r, getRow(a.col), a.col);
			if( o != Outcome.DRAW ){ return new Node(a, utility(o)); }
			optimal.min(maxVal(r, alpha, beta));
			if(optimal.val <= alpha){ break; } // max has a better choice
			beta = Math.min(beta, (int)optimal.val);
		}
		return optimal;
	}

	Node minVal(State s){
		Node optimal = new Node(null, 2);
		State r;
		for(Action a: applicableActions(s)){
			r = result(a, s.player);
			Outcome o = terminalTest(r, getRow(a.col), a.col);
			if( o != Outcome.DRAW ){ return new Node(a, utility(o)); }
			optimal.min(maxVal(r));
		}
		return optimal;
	}

	Node maxVal(State s){
		Node optimal = new Node(null, -2);
		State r;
		for(Action a: applicableActions(s)){
			r = result(a, s.player);
			Outcome o = terminalTest(r, getRow(a.col), a.col);
			if( o != Outcome.DRAW ){ return new Node(a, utility(o)); }
			optimal.max(minVal(r));
		}
		return optimal;
	}

	Node hminVal(State s, int depth, int maxDepth){
		Node optimal = new Node(null, 2);
		State r;
		int row;
		for(Action a: applicableActions(s)){
			r = result(a, s.player);
			row = getRow(a.col);
			Outcome o = terminalTest(r, row, a.col);
			if( o != Outcome.DRAW ){ return new Node(a, utility(o)); }
			if(depth >= maxDepth){ 
				updateHeuristic(r, r.player);
				return new Node(a, r.heuristic()); 
			}
			optimal.min(hmaxVal(r, depth+1, maxDepth));
		}
		return optimal;
	}

	Node hmaxVal(State s, int depth, int maxDepth){
		Node optimal = new Node(null, -2);
		State r;
		int row;
		for(Action a: applicableActions(s)){
			r = result(a, s.player);
			row = getRow(a.col);
			Outcome o = terminalTest(r, row, a.col);
			if( o != Outcome.DRAW ){ return new Node(a, utility(o)); }
			if(depth >= maxDepth){ 
				updateHeuristic(r, r.player);
				return new Node(a, r.heuristic()); 
			}
			optimal.max(hminVal(r, depth+1, maxDepth));
		}
		return optimal;
	}

	public static void main(String[] args){
		Opponent o;
		Scanner sc = new Scanner(System.in);
		printRules(); //todo

		System.out.print("Enter 1 for 3x3x3 or 2 for 6x7x4:"); //validate inputs....
		int boardType = sc.nextInt();
		System.out.print("Enter 1 for minimax, 2 for alpha/beta pruning, 3 for h-Minimax:");
		int mode = sc.nextInt();
		if(mode == 3){
			System.out.print("Enter the maximum depth:")
			int depth = sc.nextInt();
			o = new Opponent(mode, depth);
		}else{
			o = new Opponent(mode);
		}

		play(o, boardType);
	}

	static void play(Opponent o, int boardType){
		Game g;
		if(boardType == 1){
			g = new Game(3,3,3);
		}else{
			g = new Game(6,7,4);
		}

		while(true){
		//get/apply user input, call terminal test, if WIN/Loss/draw -> break;
		//get/apply opponent move, call terminal test, if WIN/loss/draw -> break;
		o.getMove(this);
		}
	}
}