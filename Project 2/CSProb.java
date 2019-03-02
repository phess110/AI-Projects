import java.util.ArrayList;
import java.util.LinkedList;
import java.time.Duration;
import java.time.Instant;

public abstract class CSProb<T>{
	
	private ArrayList<Variable<T>> vars;
	private ArrayList<Constraint<T>> cons;

	public CSProb(){
		vars = new ArrayList<Variable<T>>();
		cons = new ArrayList<Constraint<T>>();
	}
	
	public abstract void printSolution(ArrayList<T> assignment);
	public Variable<T> getVar(int idx){ return vars.get(idx); }
	public void addVar(Variable<T> v){ vars.add(v); }
	public void addConstraint(Constraint<T> c){ cons.add(c); }

	public ArrayList<T> backtrackingSearch(){
		ArrayList<T> assignment = new ArrayList<T>(vars.size());
		for(int i = 0; i < vars.size(); i++){
			assignment.add(null);
		}
		AC();
		return backtracking(assignment);
	}

	public ArrayList<T> backtrackingInferenceSearch(){
		ArrayList<T> assignment = new ArrayList<T>(vars.size());
		for(int i = 0; i < vars.size(); i++){
			assignment.add(null);
		}
		return backtrackingWithInference(assignment);
	}

	public ArrayList<T> backtracking(ArrayList<T> assignment){
		if(isComplete(assignment)){
			return assignment;
		}
		Variable<T> v = getUnassignedVar(assignment);
		ArrayList<T> dom = v.getDomain();
		for(int i = 0; i < dom.size(); i++){
			if(v.removed(i)){ continue; }
			T value = dom.get(i);
			if(isConsistent(v, value, assignment)){
				assignment.set(v.getHash(), value);
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
			if(assignment.get(i) == null){ //unassigned
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

	public int countConsUsing(Variable<T> v){
		int counter = 0;
		for(Constraint<T> c: cons){
			counter += (c.inScope(v) ? 1 : 0);
		}
		return counter;
	}
	*/

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

	void run(){
		Instant start = Instant.now();
		ArrayList<T> sol = backtrackingSearch();
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		if(sol != null){
			printSolution(sol);
		}else{
			System.out.println("There is no solution.");
		}
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
	}

	void runWithInference(){
		Instant start = Instant.now();
		ArrayList<T> sol = backtrackingInferenceSearch();
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		if(sol != null){
			printSolution(sol);
		}else{
			System.out.println("There is no solution.");
		}
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
	}

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
			if(xi.removed(i)){ continue; }
			boolean satisfiable = false;
			for(int j = 0; j < xjDom.size(); j++){
				if(xj.removed(j)){ continue; }
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
		ArrayList<T> dom = v.getDomain();
		ArrayList<T> result;
		for(int i = 0; i < dom.size(); i++){
			if(v.removed(i)){ continue; }
			T value = dom.get(i);
			if(isConsistent(v, value, assignment)){
				assignment.set(v.getHash(), value);
				v.assign(value);

				if(MAC(v, assignment) && (result = backtrackingWithInference(assignment)) != null){ //inference
					return result;
				}else{
					//reset domains of v and remaining unassigned variables. Call AC().
					v.reset();
					for(int j = 0; j < assignment.size(); j++){
						if(assignment.get(j) == null){ vars.get(j).reset(); }
					}
					AC();
				}
			}
			assignment.set(v.getHash(), null);
		}
		return null;
	}
}