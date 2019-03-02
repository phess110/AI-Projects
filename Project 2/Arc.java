public class Arc<T>{
	
	private Constraint<T> constraint;
	private boolean orientation; // true is natural orientation, false is reverse orientation

	public Arc(Constraint<T> c, boolean b){
		constraint = c;
		orientation = b;
	}

	public boolean getOrientation(){ return orientation; }
	public Constraint<T> getConstraint(){ return constraint; }
	public Variable<T> first() { return constraint.getScope().get(orientation ? 0 : 1); }
	public Variable<T> second() { return constraint.getScope().get(orientation ? 1 : 0); }
}