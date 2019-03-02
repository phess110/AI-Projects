import java.util.ArrayList;

public class ScheduleProblem extends CSProb<Integer>{
	public int count = 0;
	static Relation<Integer> precede1 = (Integer a, Integer b)->(a + 1 <= b);
	static Relation<Integer> precede2 = (Integer a, Integer b)->(a + 2 <= b);
	static Relation<Integer> precede3 = (Integer a, Integer b)->(a + 3 <= b);
	static Relation<Integer> precede10 = (Integer a, Integer b)->(a + 10 <= b);
	static Relation<Integer> disjunct10 = (Integer a, Integer b) -> (precede10.relation(a,b) || precede10.relation(b,a));

	public class ScheduleVar extends Variable<Integer>{
		public ScheduleVar(String s, Domain<Integer> d){
			super(s, d, count);
			count += 1;
		}
	}

	public class ScheduleConstraint extends Constraint<Integer>{
		public ScheduleConstraint(ArrayList<Variable<Integer>> sc, Relation<Integer> r){
			super(sc, r);
		}
	}

	public Constraint<Integer> makeCons(ScheduleVar s1, ScheduleVar s2, Relation<Integer> r){
			ArrayList<Variable<Integer>> sc = new ArrayList<Variable<Integer>>();
			sc.add(s1);
			sc.add(s2);
			return new ScheduleConstraint(sc, r);
	}

	public void printSolution(ArrayList<Integer> assignment){
		for(int i = 0; i < count; i++){
			System.out.println(getVar(i).toString() + " is performed at time t = " + assignment.get(i));
		}
	}

	// creates instance of australian map problem
	public ScheduleProblem(){
		super();
		//ArrayList<Integer> dom1 = new ArrayList<Integer>();
		//dom1.add(1);
		//dom1.add(11);
		ArrayList<Integer> dom = new ArrayList<Integer>();
		for(int i = 1; i <= 27; i++){
			dom.add(i);
		}
										// 0		1			2			3			4			5			6			7		8			9			10		11			12		13			14
		String[] states = new String[]{"AXLE_f", "AXLE_b", "Wheel_rf", "Wheel_lf", "Wheel_rb", "Wheel_lb", "Nuts_rf", "Nuts_lf", "Nuts_rb", "Nuts_lb", "Cap_rf", "Cap_lf", "Cap_rb", "Cap_lb", "Inspect"};
		//int[] time = {10,10,1,1,1,1,2,2,2,2,1,1,1,1,3};

		ArrayList<ScheduleVar> vars = new ArrayList<ScheduleVar>();
		for(String s: states){
			/*
			if(s.equals("AXLE_f") || s.equals("AXLE_b")){
				d = new Domain<Integer>(dom1);
			}else
			{
				d = new Domain<Integer>(dom);
			}*/
			Domain<Integer> d = new Domain<Integer>(dom);
			ScheduleVar v = new ScheduleVar(s, d);
			vars.add(v);
			addVar(v);
		}

		//add constraints, AIMA ch 6
		addConstraint(makeCons(vars.get(0), vars.get(2), precede10));
		addConstraint(makeCons(vars.get(0), vars.get(3), precede10));
		addConstraint(makeCons(vars.get(1), vars.get(4), precede10));
		addConstraint(makeCons(vars.get(1), vars.get(5), precede10));
		addConstraint(makeCons(vars.get(2), vars.get(6), precede1));
		addConstraint(makeCons(vars.get(3), vars.get(7), precede1));
		addConstraint(makeCons(vars.get(4), vars.get(8), precede1));
		addConstraint(makeCons(vars.get(5), vars.get(9), precede1));
		addConstraint(makeCons(vars.get(6), vars.get(10), precede2));
		addConstraint(makeCons(vars.get(7), vars.get(11), precede2));
		addConstraint(makeCons(vars.get(8), vars.get(12), precede2));
		addConstraint(makeCons(vars.get(9), vars.get(13), precede2));
		addConstraint(makeCons(vars.get(0), vars.get(1), disjunct10));
		addConstraint(makeCons(vars.get(10), vars.get(14), precede1));
		addConstraint(makeCons(vars.get(11), vars.get(14), precede1));
		addConstraint(makeCons(vars.get(12), vars.get(14), precede1));
		addConstraint(makeCons(vars.get(13), vars.get(14), precede1));

	}
}