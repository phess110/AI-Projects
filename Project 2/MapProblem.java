import java.util.HashSet;
import java.util.ArrayList;

public class MapProblem extends CSProb<String>{
	static int count = 0;
	static Relation<String> neq = (String a, String b)->(!a.equals(b));

	public class MapVar extends Variable{
		public MapVar(String s, HashSet<String> d){
			super(s, d, count);
			count += 1;
		}
	}

	public class MapConstraint extends Constraint<String>{
		public MapConstraint(ArrayList<Variable<String>> sc, Relation<String> r){
			super(sc, r);
		}
	}

	public Constraint<String> makeConstraint(MapVar s1, MapVar s2){
			ArrayList<Variable<String>> sc = new ArrayList<Variable<String>>(2);
			sc.set(0, s1);
			sc.set(1, s2);
			return new MapConstraint(sc, neq);
	}

	// creates instance of australian map problem
	public MapProblem(){
		super();

		HashSet<String> colors = new HashSet<String>();
		colors.add("red");
		colors.add("green");
		colors.add("blue");

		String[] states = new String[]{"T", "WA", "NT", "Q", "NSW", "V", "SA"};
		ArrayList<MapVar> vars = new ArrayList<MapVar>(7);

		for(String s: states){
			MapVar v = new MapVar(s, colors);
			vars.set(v.getHash(), v);
			addVar(v);
		}

		addConstraint(makeConstraint(vars.get(1), vars.get(6))); //sa,wa
		addConstraint(makeConstraint(vars.get(2), vars.get(6))); //sa,nt
		addConstraint(makeConstraint(vars.get(3), vars.get(6))); //sa,q
		addConstraint(makeConstraint(vars.get(4), vars.get(6))); //sa,nsw
		addConstraint(makeConstraint(vars.get(5), vars.get(6))); //sa,v
		addConstraint(makeConstraint(vars.get(1), vars.get(2))); //wa,nt
		addConstraint(makeConstraint(vars.get(2), vars.get(3))); //nt,q
		addConstraint(makeConstraint(vars.get(3), vars.get(4))); //q,nsw
		addConstraint(makeConstraint(vars.get(4), vars.get(5))); //nsw,v
	}
}