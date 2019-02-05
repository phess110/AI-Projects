public class Action{
	int col;
	Action(int j){ col = j; }

	@Override
	public boolean equals(Object o){
		if(o instanceof Action){
				Integer t = ((Action) o).col;
				return t.equals(this.col);
		}
		return false;
	}

	@Override
	public int hashCode(){
		return ((Integer) col).hashCode();
	}
}