import java.util.ArrayList;

public class QueenProblem extends CSProb<Integer>{
	
	public int count = 0;
	static int n;
	static Relation<Integer> isNonAttacking = (Integer a, Integer b)->(nonAttacking(a,b));

	public class QueenVar extends Variable<Integer>{
		public QueenVar(String s, Domain<Integer> d){
			super(s, d, count);
			count++;
		}
	}

	public class QueenConstraint extends Constraint<Integer>{
		public QueenConstraint(ArrayList<Variable<Integer>> sc, Relation<Integer> r){
			super(sc, r);
		}
	}

	public Constraint<Integer> makeCons(QueenVar s1, QueenVar s2){
			ArrayList<Variable<Integer>> sc = new ArrayList<Variable<Integer>>(2);
			sc.add(s1);
			sc.add(s2);
			return new QueenConstraint(sc, isNonAttacking);
	}

	public void printSolution(ArrayList<Integer> assignment){

	}

	public QueenProblem(int size){
		super();
		n = size;

		ArrayList<QueenVar> vars = new ArrayList<QueenVar>();

		//initialize n vars, each variable corresponds to queens position within a column
		for(int i = 0; i < size; i++){							//col#: 0   1   ... n-1
			ArrayList<Integer> dom = new ArrayList<Integer>(); 	//0		0   n   ... n^2-n <- Board Indexing
			for(int j = 0; j < size; j++){						//1		1	n+1 ... . 
				dom.add(j + size * i);							//.		.	.   ... .
			}													//n-1	n-1 2n-1 .. n^2-1
			Domain<Integer> d = new Domain<Integer>(dom);
			QueenVar v = new QueenVar("", d);
			vars.add(v);
			addVar(v);
		}
		//add non-attacking constraints
		for(int i = 0; i < size; i++){
			for(int j = i+1; j < size; j++){
				addConstraint(makeCons(vars.get(i), vars.get(j)));
			}
		}
	}

	static boolean nonAttacking(Integer a, Integer b){
		int a_col = a / n;
		int a_row = a % n;
		int b_col = b / n;
		int b_row = b % n;

		if(a_row != b_row){ //not in same row
			return (Math.abs(a_row - b_row) != Math.abs(a_col - b_col)); //horiz change = vertical change => same diag
		}
		return false;
	}
	
}