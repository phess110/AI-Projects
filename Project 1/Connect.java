import java.util.HashSet;
import java.util.Scanner;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;

public class Connect implements Game{
	private State s0; 
	private final int height, width, in_a_row;
	private Action[] actions;
	private int maxDepth;

	Connect(int m, int n, int k){
		height = m;
		width = n;
		in_a_row = k;
		s0 = new State(m,n);
		actions = new Action[n];
		for(int i = 0; i < n; i++){
			actions[i] = new Action(i);
		}
	}

	State getState(){
		return s0;
	}

	void setDepth(int depth){
		maxDepth = depth;
	}

	/* iterate through board - X's for P1 and O's for P2. O(nm) */
	void displayBoard(){
		System.out.print("\n ");
		for(int i = 0; i < width; i++){ System.out.print(i + " "); }
		for(int[] row: s0.getBoard()){
			System.out.print("\n|");
			for(int entry: row){
				if(entry == 1){ System.out.print("X "); }
				else if(entry == 2){ System.out.print("O "); }
				else{ System.out.print("_ "); }
			}
			System.out.print("|");
		}
		System.out.println("\n");
	}

	/* returns set of applicable actions in current state s. O(n) */
	public HashSet<Action> applicableActions(State s){
		HashSet<Action> applicable = new HashSet<Action>();
		for (Action a: actions){
			if(!s.isFull(a.col)){
				applicable.add(a);
			}
		}
		return applicable;
	}

	/* computes the state which would result from applying action a in s0. O(nm) */
	public State result(State s, Action a){
		State newState = s.copy(height, width);
		newState.setEntry(getRow(s, a) - 1, a.col, s.player); //move up one to empty row
		newState.setPlayer(other(s.player));
		return newState;
	}

	/* modifies state s0 of the game given action a (assumes a is applicable in current state) O(m) */
	public void applyAction(Action a){
		s0.setEntry( getRow(s0, a) - 1, a.col, s0.player);
		s0.setPlayer( other(s0.player) );
	}

	/* Returns top (filled) row in column c or height if column is empty. O(m) */
	int getRow(State s, Action a){
		int i = 0;
		while(i < height && s.entry(i,a.col) == 0){
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
	public Outcome terminalTest(State s, Action a){
		int i = getRow(s, a);
		int j = a.col;
		int player = s.entry(i,j);
		if(lowerDiag(s,player,i,j) == in_a_row-1 || upperDiag(s,player,i,j) == in_a_row-1 || horizontal(s,player,i,j) == in_a_row-1 || vertical(s,player,i,j) == in_a_row-1)
		{ return player == 1 ? Outcome.AILOSS : Outcome.AIWIN; }
		return isTie(s);
	}

	Outcome isTie(State s){
		for(int k = 0; k < width; k++){ 
			if( !s.isFull(k) ){ return Outcome.NOTA; }
		}
		return Outcome.DRAW;
	}

	/* Heuristic used by hMinimax */
	int heuristic(State s){
		int [] h = updateHeuristic(s);
		// aiHeuristic = h[0];
		// humanHeuristic = h[1];
		return h[0]-h[1];
	} 

	/* 
	A good sequence for player i is a consecutive sequence of (in_a_row-2) of i's tiles on the board.
	A good move is any move builds on, or creates, a good sequence. The heuristic counts the number of good moves. 
	This function updates the number of good moves for the human + computer after move (i,j) -> state s.
	*/ 
	int[] updateHeuristic(State s){
		int[] heuristic = {0, 0};
		int c;
		for(Action a: applicableActions(s)){
			for(int r = getRow(s,a) - 1; r >= 0; r--){
				c = a.col;
				heuristic[0] += update(s, 2, r, c);
				heuristic[1] += update(s, 1, r, c);
				if(c > 0 && c < width-1 && s.entry(r,c-1) == 0 && s.entry(r,c+1) == 0){ //don't check squares without neighbors
					break;
				}else if(c == width-1 && s.entry(r,c-1) == 0){
					break; 
				}else if(c == 0 && s.entry(r,c+1) == 0 ){
					break;
				}
			}
		}
		return heuristic;
	}

	int update(State s, int player, int i, int j){
		int m;
		if(	(m = lowerDiag(s,player,i,j)) >= in_a_row-2 	|| (m = upperDiag(s,player,i,j)) >= in_a_row-2 
			|| (m = horizontal(s,player,i,j)) >= in_a_row-2 || (m = vertical(s,player,i,j)) >= in_a_row-2){
			return m + 3 - in_a_row;
		}
		return 0;
	}

	/* Length of longest sequence of player's tiles, going through square (i,j) along the upper diag. */	
	int upperDiag(State s, int player, int i, int j){
		int length = 0;
		boolean sW = true, nE = true;
		int r1 = i, r2 = i, c1 = j, c2 = j;
		while((nE || sW) && length <= in_a_row){ //upper diagonal
			if(nE){ 
				if(r1 > 0 && c1 < width-1 && s.entry(--r1,++c1) == player){length++;}
				else{nE = false;} 
			}
			if(sW){
				if(r2 < height-1 && c2 > 0  && s.entry(++r2,--c2) == player){length++;}
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
				if(r1 > 0 && c1 > 0 && s.entry(--r1,--c1) == player){length++;}
				else{nW = false;} 
			}
			if(sE){
				if(r2 < height-1 && c2 < width-1 && s.entry(++r2,++c2) == player){length++;}
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
				if(r1 < height-1 && s.entry(++r1,j) == player){length++;}
				else{down = false;} 
			}
			if(up){
				if(r2 > 0 && s.entry(--r2, j) == player){length++;}
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
				if(c1 > 0 && s.entry(i,--c1) == player){length++;}
				else{left = false;} 
			}
			if(right){
				if(c2 < width-1 && s.entry(i,++c2) == player){length++;}
				else{right = false;}
			}
		}
		return length;
	}

	/* 
	AI is max: Wants to get an AIWIN (as fast as possible) or if it is guaranteed to lose then lose as slow as possible
	Human is min: Wants an AILOSS (as fast as possible)
	*/
	public int utility(Outcome o, int depth){
		switch(o){
			case AIWIN: return 50 - depth;
			case AILOSS: return -50 + depth;
			default: return 0; 
		}
	}

	public static class Opponent{
		private int mode;

		Opponent(int m){
			mode = m;
		}

		//returns action of opponent in state s0
		Action getMove(Connect g){
			switch(mode) {
				case 2: return minimax(g);
				case 3:	return alphaBetaPrune(g);
				case 4:	return hminimax(g);
				default: return randomAction(g);
			}
		}

		Action randomAction(Connect g){ 
			HashSet<Action> acts = g.applicableActions(g.getState());
			Random rand = new Random(); 
			Action a = new Action(rand.nextInt(g.width));
			while(!acts.contains(a)){
				a.col = rand.nextInt(g.width);
			}
			return a; 
		}

		Action minimax(Connect g){ 
			Action opt = null; 
			int v = Integer.MIN_VALUE;
			int m;
			State r;
			for(Action a: g.applicableActions(g.getState())){
				r = g.result(g.getState(), a);
				m = g.minVal(r, a, 0); // "human" is min player
				if( m > v ){
					opt = a;
					v = m;
				}
			}
			return opt; 
		}

		Action alphaBetaPrune(Connect g){ 
			Action opt = null; 
			int v = Integer.MIN_VALUE;
			int m;
			State r;
			for(Action a: g.applicableActions(g.getState())){
				r = g.result(g.getState(), a);
				m = g.abminVal(r, Integer.MIN_VALUE, Integer.MAX_VALUE, a, 0);
				if( m > v ){
					opt = a;
					v = m;
				}
			}
			return opt; 
		}

		Action hminimax(Connect g){ 
			Action opt = null; 
			int v = Integer.MIN_VALUE;
			int m;
			State r;
			for(Action a: g.applicableActions(g.getState())){
				r = g.result(g.s0, a);
				m = g.hminVal(r, a, 0);
				System.out.println(m + "  " + a.col);
				if( m > v ){
					opt = a;
					v = m;
				}
			}
			return opt; 
		}
	}

	int abmaxVal(State s, int alpha, int beta, Action recent, int depth){
		Outcome o = terminalTest(s, recent);
		if( o != Outcome.NOTA )
			{ return utility(o, depth); }

		int v = Integer.MIN_VALUE;
		for(Action a: applicableActions(s)){
			v = Math.max(v, abminVal(result(s, a), alpha, beta, a, depth+1));
			if(v >= beta)
				{ return v; }
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	int abminVal(State s, int alpha, int beta, Action recent, int depth){
		Outcome o = terminalTest(s, recent);
		if( o != Outcome.NOTA )
			{ return utility(o, depth); }

		int v = Integer.MAX_VALUE;
		for(Action a: applicableActions(s)){
			v = Math.min(v, abmaxVal(result(s, a), alpha, beta, a, depth+1));
			if(v <= alpha)
				{ return v; }
			beta = Math.min(beta, v);
		}
		return v;
	}

	int minVal(State s, Action recent, int depth){
		Outcome o = terminalTest(s, recent);
		if( o != Outcome.NOTA )
			{ return utility(o, depth); }

		int v = Integer.MAX_VALUE;
		for(Action a: applicableActions(s)){
			v = Math.min(v, maxVal(result(s, a), a, depth+1));
		}
		return v;
	}

	int maxVal(State s, Action recent, int depth){
		Outcome o = terminalTest(s, recent);
		if( o != Outcome.NOTA )
			{ return utility(o, depth); }

		int v = Integer.MIN_VALUE;
		for(Action a: applicableActions(s)){
			v = Math.max(v, minVal(result(s, a), a, depth+1));
		}
		return v;
	}

	int hminVal(State s, Action recent, int depth){
		Outcome o = terminalTest(s, recent);
		if( o != Outcome.NOTA )
			{ return utility(o, 0); }

		int v = Integer.MAX_VALUE;
		State r;
		for(Action a: applicableActions(s)){
			r = result(s,a);
			if(depth >= maxDepth){ 
				return heuristic(r); 
			}
			v = Math.min(v, hmaxVal(r, a, depth+1));
		}
		return v;
	}

	int hmaxVal(State s, Action recent, int depth){
		Outcome o = terminalTest(s, recent);
		if( o != Outcome.NOTA )
			{ return  utility(o, 0); }

		int v = Integer.MIN_VALUE;
		State r;
		for(Action a: applicableActions(s)){
			r = result(s,a);
			if(depth >= maxDepth){ 
				return heuristic(r);
			}
			v = Math.max(v, hminVal(r, a, depth+1));
		}
		return v;
	}

	static Connect getBoardType(){
		Scanner sc = new Scanner(System.in);
		System.out.print("Choose the board dimensions.\nEnter 1 for 3x3x3 or 2 for 6x7x4: "); //validate inputs....
		int bT = sc.nextInt();
		while(bT > 2 || bT < 1){System.out.print("Invalid input. Try again (1-2): "); bT = sc.nextInt();} 
		return bT == 1 ? new Connect(3,3,3) : new Connect(6,7,4);
	}

	static Opponent getOpponent(Connect g){
		Opponent opp;
		Scanner sc = new Scanner(System.in);
		System.out.print("Choose opponent type.\nEnter 1 for random, 2 for minimax, 3 for alpha/beta pruning, 4 for h-Minimax: ");
		int mode = sc.nextInt();
		while(mode > 4 || mode < 1){System.out.print("Invalid input. Try again (1-4): "); mode = sc.nextInt();}  
		if(mode == 4){
			System.out.print("Enter the maximum depth:");
			int depth = sc.nextInt();
			while( depth <= 0 ){System.out.print("Depth must be positive: "); depth = sc.nextInt();}
			g.setDepth(depth);
		}
		return new Opponent(mode);
	}

	public static void main(String[] args){
		Connect g = getBoardType();
		Opponent o = getOpponent(g);
		switch(play(g, o)){
			case AIWIN: System.out.println("You lose.");
					break;
			case AILOSS: System.out.println("You win.");
					break;
			default: System.out.println("It's a tie.");
		}
	}

	static Outcome play(Connect g, Opponent o){
		Action a;
		Instant start, end;
		Outcome r;
		Scanner sc = new Scanner(System.in);

		while(true){
			g.displayBoard();
			System.out.print("Enter your move (column no. 0-" + (g.width - 1) + "): ");
			a = new Action(sc.nextInt());
			while(! g.applicableActions(g.getState()).contains(a) ){ //check if move is valid
				System.out.print("Not a valid move, try again: ");
				a = new Action(sc.nextInt());
			}
			g.applyAction(a);
			r = g.terminalTest(g.getState(), a);
			if( r != Outcome.NOTA ){ sc.close(); return r; } 

			start = Instant.now();
			a = o.getMove(g);
			end = Instant.now();

			g.applyAction(a);
			Duration diff = Duration.between(start, end);
			System.out.println("Opponent's move: column " + a.col + ".\nTime elapsed: " + diff.toMillis() + "ms");
			r = g.terminalTest(g.getState(), a);
			if( r != Outcome.NOTA ){ sc.close(); return r; } 
		}
	}
}