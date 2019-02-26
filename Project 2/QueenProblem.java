public class QueenProblem extends CSProb<Integer>{

	private int n;
	static int count = 0;
	static Relation<Integer> isNonAttacking = (Integer a, Integer b)->(a.nonAttacking(b));

	public class QueenVar extends Variable<Integer>{
		public QueenVar(String s, HashSet<Integer> d){
			super(s, d, count);
		}
	}

	public class QueenConstraint extends Constraint<Integer>{
		public QueenConstraint(ArrayList<Variable<Integer>> sc, Relation<Integer> r){
			super(sc, r, count);
			count++;
		}
	}

	public Constraint<Integer> makeConstraint(QueenVar s1, QueenVar s2){
			ArrayList<Variable<Integer>> sc = new ArrayList<Variable<Integer>>(2);
			sc.set(0, s1);
			sc.set(1, s2);
			return new QueenConstraint(sc, isNonAttacking);
	}

	public QueenProblem(int size){
		super();
		n = size;

		ArrayList<QueenVar> vars = new ArrayList<QueenVar>(size);

		//initialize n vars, each variable corresponds to queens position within a column
		for(int i = 0; i < size; i++){
			HashSet<Integer> dom = new HashSet<Integer>();
			for(int j = 0; j < size; j++){
				dom.add(j + size * i);
			}
			QueenVar v = new QueenVar("",dom);
			vars.set(i, v);
			addVar(v);
		}
		//add non-attacking constraints
		for(int i = 0; i < size; i++){
			for(int j = i+1; j < size; j++){
				addCons(makeConstraint(vars.get(i), vars.get(j)));
			}
		}
	}

	boolean nonAttacking(Integer b){
		int a_col = this / n;
		int a_row = this % n;
		int b_col = b / n;
		int b_row = b % n;

		if(a_row != b_row){ //not in same row
			return abs(a_row - b_row) == abs(a_col - b_col); //horiz change = vertical change => same diag
		}
		return false;
	}
	
}