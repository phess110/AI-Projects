import java.util.ArrayList;

public class MapProblem extends CSProb<String>{
	public int count = 0;
	static Relation<String> neq = (String a, String b)->(!a.equals(b));
	static String[] states = new String[]{"T", "WA", "NT", "Q", "NSW", "V", "SA"};

	public class MapVar extends Variable<String>{
		public MapVar(String s, Domain<String> d){
			super(s, d, count);
			count += 1;
		}
	}

	public class MapConstraint extends Constraint<String>{
		public MapConstraint(ArrayList<Variable<String>> sc, Relation<String> r){
			super(sc, r);
		}
	}

	public Constraint<String> makeConst(MapVar s1, MapVar s2){
			ArrayList<Variable<String>> sc = new ArrayList<Variable<String>>();
			sc.add(s1);
			sc.add(s2);
			return new MapConstraint(sc, neq);
	}

	public void printCSP(){
		System.out.print("VARIABLES: ");
		for(Variable<String> v: getv()){ 
			System.out.print(v.toString() + " ");
		}
		System.out.println("\nCONSTRAINTS: SA not equal to WA, NT, Q, NSW, V.\nWA not equal to NT.\nNT not equal to Q.\nQ not equal to NSW.\nNSW not equal to V.\n");
	}

	public void printSolution(ArrayList<String> assignment){
		System.out.println("\nAUSTRALIAN MAP COLORING:\n");
		printCSP();
		for(int i = 0; i < count; i++){
			System.out.println(getVar(i).toString() + " is colored " + assignment.get(i));
		}
	}

	// creates instance of australian map problem
	public MapProblem(){
		super();
		ArrayList<String> colors = new ArrayList<String>();
		colors.add("red");
		colors.add("green");
		colors.add("blue");
		ArrayList<MapVar> vars = new ArrayList<MapVar>();
		for(String s: states){
			Domain<String> d = new Domain<String>(colors);
			MapVar v = new MapVar(s, d);
			vars.add(v);
			addVar(v);
		}

		addConstraint(makeConst(vars.get(1), vars.get(6))); //sa,wa
		addConstraint(makeConst(vars.get(2), vars.get(6))); //sa,nt
		addConstraint(makeConst(vars.get(3), vars.get(6))); //sa,q
		addConstraint(makeConst(vars.get(4), vars.get(6))); //sa,nsw
		addConstraint(makeConst(vars.get(5), vars.get(6))); //sa,v
		addConstraint(makeConst(vars.get(1), vars.get(2))); //wa,nt
		addConstraint(makeConst(vars.get(2), vars.get(3))); //nt,q
		addConstraint(makeConst(vars.get(3), vars.get(4))); //q,nsw
		addConstraint(makeConst(vars.get(4), vars.get(5))); //nsw,v
	}
}