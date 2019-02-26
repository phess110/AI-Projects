import java.util.HashSet;
import java.util.ArrayList;

public abstract class CSProb<T>{
	
	private ArrayList<Variable<T>> vars;
	private HashSet<Constraint<T>> cons;

	public CSProb(){
		vars = new ArrayList<Variable<T>>();
		cons = new HashSet<Constraint<T>>();
	}

	public CSProb(ArrayList<Variable<T>> v, HashSet<Constraint<T>> c){
		cons = c;
		vars = v;
	}

	public void addVar(Variable<T> v){
		vars.add(v);
	}

	public Variable<T> getVar(int hash){
		return vars.get(hash);
	}

	public void addConstraint(Constraint<T> c){
		cons.add(c);
	}

	public ArrayList<T> backtrackingSearch(){
		ArrayList<T> assignment = new ArrayList<T>(vars.size());
		return backtracking(assignment);
	}

	public ArrayList<T> backtracking(ArrayList<T> assignment){
		if(isComplete(assignment)){
			return assignment;
		}

		Variable<T> v = getUnassignedVar();
		for(T value: v.getDomain()){ //order domain?

			if(isConsistent(v, value, assignment)){
				assignment.set(v.getHash(), value);
				//infrancing goes here
				ArrayList<T> result = backtracking(assignment);
				if(result != null){
					return result;
				}
			}
			assignment.set(v.getHash(), null);
		}
		return null;
	}

	public Variable<T> getUnassignedVar(){
		return null; //TODO
	}

	public void printSolution(ArrayList<T> assignment){ //this is probably better abstract
		for(Variable<T> v: vars){
			System.out.println(v.getName() + " = " + assignment.get(v.getHash()));
		}
	}

	public boolean isComplete(ArrayList<T> assignment){
		//is assignement complete 
		for(T val: assignment){
			if(val == null){ return false; }
		}

		return true;
	}

	//check constraints involving v
	public boolean isConsistent(Variable<T> v, T value, ArrayList<T> assignment){
		for(Constraint<T> c: cons){
			if(c.inScope(v) && c.allAssigned(assignment)){
				if(!c.isSatisfied(assignment)){ return false; }
			}
		}

		return true;
	}

}