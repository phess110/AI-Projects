import java.util.ArrayList;
import java.util.LinkedList;

public abstract class CSProb<T>{
	
	private ArrayList<Variable<T>> vars;
	private ArrayList<Constraint<T>> cons;

	public CSProb(){
		vars = new ArrayList<Variable<T>>();
		cons = new ArrayList<Constraint<T>>();
	}
	
	public ArrayList<Variable<T>> getv(){ //REMOVE
		return vars;
	}

	public ArrayList<Constraint<T>> getc(){ //REMOVE
		return cons;
	}
	
	public Variable<T> getVar(int idx){ return vars.get(idx); }
	public void addVar(Variable<T> v){ vars.add(v); }
	public void addConstraint(Constraint<T> c){ cons.add(c); }

	public ArrayList<T> backtrackingSearch(){
		ArrayList<T> assignment = new ArrayList<T>(vars.size());
		for(int i = 0; i < vars.size(); i++){
			assignment.add(null);
		}
		return backtracking(assignment);
	}

	public ArrayList<T> backtracking(ArrayList<T> assignment){
		if(isComplete(assignment)){
			return assignment;
		}
		Variable<T> v = getUnassignedVar(assignment);
		for(T value: v.getDomain()){ //order domain?
			if(!v.contains(value)){ continue; } //value has been removed
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

	// Minimum remaining values heuristic
	public Variable<T> getUnassignedVar(ArrayList<T> assignment){
		Variable<T> min = null;
		int c = Integer.MAX_VALUE;
		int temp;
		for(int i = 0; i < assignment.size(); i++){
			if(assignment.get(i) == null){ 
				temp = vars.get(i).getSize();
				if(temp < c){
					min = vars.get(i); 
					c = temp;
				}
			}
		}
		return min;
	}

	/*
	//MOST CONSTRAINING
	Variable<T> max = null;
		int c = 0;
		int temp;
		for(int i = 0; i < assignment.size(); i++){
			if(assignment.get(i) == null){ 
				temp = countConsUsing(vars.get(i));
				if(temp >= c){
					max = vars.get(i); 
					c = temp;
				}
			}
		}
		return max;
	*/

	public int countConsUsing(Variable<T> v){
		int counter = 0;
		for(Constraint<T> c: cons){
			counter += (c.inScope(v) ? 1 : 0);
		}
		return counter;
	}

	public boolean isComplete(ArrayList<T> assignment){
		for(T val: assignment){
			if(val == null){ return false; }
		}
		return true;
	}

	//check constraints involving v
	public boolean isConsistent(Variable<T> v, T value, ArrayList<T> assignment){
		assignment.set(v.getHash(), value);
		for(Constraint<T> c: cons){
			if(c.inScope(v) && c.allAssigned(assignment)){
				if(!c.isSatisfied(assignment)){ assignment.set(v.getHash(), null); return false; }
			}
		}
		assignment.set(v.getHash(), null); 
		return true;
	}

	public abstract void printSolution(ArrayList<T> assignment);

	/*
	* Imposes initial arc consistency
	*/
	public boolean AC(){
		LinkedList<Arc<T>> queue = new LinkedList<Arc<T>>();
		for(Constraint<T> c: cons){ //load queue with CSP arcs
			queue.add(new Arc<T>(c, true));
			queue.add(new Arc<T>(c, false));
		}
		return AC3(queue);
	}

	/*
	* Imposes arc consistency after assignment to var
	*/
	public boolean MAC(Variable<T> var, ArrayList<T> assignment){
		LinkedList<Arc<T>> queue = new LinkedList<Arc<T>>();
		for(Constraint<T> c: cons){
			if(c.inScope(var)){
				int k = (c.getScope().get(0).equals(var)) ? 1 : 0;
				Variable<T> xj = c.getScope().get(k);
				boolean orient = (k == 0);
				if(assignment.get(xj.getHash()) == null){
					queue.add(new Arc<T>(c, orient));
				}
			}
		}
		return AC3(queue);
	}

	public boolean AC3(LinkedList<Arc<T>> queue){
		while(!queue.isEmpty()){
			Arc<T> a = queue.poll();
			if(revise(a)){
				if(a.first().isEmpty()){ return false; }
				for(Constraint<T> c: cons){
					if(c.inScope(a.first())){

						ArrayList<Variable<T>> sc = c.getScope();
						int k = (sc.get(0).equals(a.first())) ? 1 : 0;
						Variable<T> xk = sc.get(k);
						boolean orient = (k == 0);

						if(!xk.equals(a.second())){
							queue.add(new Arc<T>(c, orient));
						}
					}
				}
			}
		}
		return true;
	}

	public boolean revise(Arc<T> a){
		boolean revised = false;
		Variable<T> xi = a.first();
		Variable<T> xj = a.second();
		Relation<T> ijCons = a.getConstraint().getRelation();
		ArrayList<T> xiDom = xi.getDomain();
		ArrayList<T> xjDom = xj.getDomain();
		T x, y;

		for(int i = 0; i < xiDom.size(); i++){
			if(!xi.contains(i)){ continue; }
			boolean satisfiable = false;
			for(int j = 0; j < xjDom.size(); j++){
				if(!xj.contains(j)){ continue; }
				x = a.getOrientation() ? xiDom.get(i) : xjDom.get(j);
				y = a.getOrientation() ? xjDom.get(j) : xiDom.get(i);
				if(ijCons.relation(x, y)){
					satisfiable = true;
					break;
				}
			}
			if(!satisfiable){
				xi.remove(i);
				revised = true;
			}
		}
		return revised;
	}

	public ArrayList<T> backtrackingWithInference(ArrayList<T> assignment){
		if(isComplete(assignment)){
			return assignment;
		}
		Variable<T> v = getUnassignedVar(assignment);
		for(T value: v.getDomain()){ 
			if(!v.contains(value)){ continue; } //value has been removed
			if(isConsistent(v, value, assignment)){
				assignment.set(v.getHash(), value);
				v.assign(value);
				//inferencing
				if(MAC(v, assignment)){
					ArrayList<T> result = backtracking(assignment);
					if(result != null){
						return result;
					}
				}
				else{//reset domains of v and remaining unassigned variables. Call AC().
					v.reset();
					for(int i = 0; i < assignment.size(); i++){
						if(assignment.get(i) == null){ vars.get(i).reset(); }
					}
					AC();
				}
			}
			assignment.set(v.getHash(), null);
		}
		return null;
	}
}