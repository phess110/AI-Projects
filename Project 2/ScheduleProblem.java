import java.util.HashSet;
import java.util.ArrayList;

public class ScheduleProblem extends CSProb<Integer>{
	static int count = 0;
	static Relation<Integer> precede1 = (Integer a, Integer b)->(a + 1 <= b);
	static Relation<Integer> precede2 = (Integer a, Integer b)->(a + 2 <= b);
	static Relation<Integer> precede3 = (Integer a, Integer b)->(a + 3 <= b);
	static Relation<Integer> precede10 = (Integer a, Integer b)->(a + 10 <= b);
	//static Relation<Integer> disjunct = () -> //given two relations at least one is true

	public class ScheduleVar extends Variable<Integer>{
		public ScheduleVar(String s, HashSet<Integer> d){
			super(s, d, count);
			count += 1;
		}
	}

	public class ScheduleConstraint extends Constraint<Integer>{
		public ScheduleConstraint(ArrayList<Variable<Integer>> sc, Relation<Integer> r){
			super(sc, r);
		}
	}

	public Constraint<Integer> makeConstraint(ScheduleVar s1, ScheduleVar s2){
			ArrayList<Variable<Integer>> sc = new ArrayList<Variable<Integer>>(2);
			sc.set(0, s1);
			sc.set(1, s2);
			return new ScheduleConstraint(sc, precede10);
	}

	// creates instance of australian map problem
	public ScheduleProblem(){
		super();

		HashSet<Integer> dom = new HashSet<Integer>();
		for(int i = 1; i <= 27; i++){
			dom.add(i);
		}

		String[] states = new String[]{"AXLE_f", "AXLE_b", "Wheel_rf", "Wheel_lf", "Wheel_rb", "Wheel_lb", "Nuts_rf", "Nuts_lf", "Nuts_rb", "Nuts_lb", "Cap_rf", "Cap_lf", "Cap_rb", "Cap_lb", "Inspect"};
		ArrayList<ScheduleVar> vars = new ArrayList<ScheduleVar>(15);

		for(String s: states){
			ScheduleVar v = new ScheduleVar(s, dom);
			vars.set(v.getHash(), v);
			addVar(v);
		}

		//add constraints, AIMA ch 6
	}
}