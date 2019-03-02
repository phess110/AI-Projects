import java.util.ArrayList;

public abstract class Constraint<T>{

	private ArrayList<Variable<T>> scope;
	private Relation<T> rel;

	public Constraint(ArrayList<Variable<T>> s, Relation<T> r){
		scope = s;
		rel = r;
	}

	public ArrayList<Variable<T>> getScope(){ return scope; }
	public Relation<T> getRelation(){ return rel; }

	//check if variable v lies in the scope of constraint
	boolean inScope(Variable<T> v){
		return scope.contains(v);
	}

	//check boolean constraint
	boolean isSatisfied(ArrayList<T> assignment){
		return rel.relation(assignment.get(scope.get(0).getHash()), assignment.get(scope.get(1).getHash()));
	}

	//check if all variables in constraint have been assigned
	boolean allAssigned(ArrayList<T> assignment){
		for(Variable<T> v : scope){
			if(assignment.get(v.getHash()) == null) { return false; }
		}
		return true;
	}

}