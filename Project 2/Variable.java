import java.util.ArrayList;

public abstract class Variable<T>{

	private Domain<T> domain;
	private String varName;
	private int hash;

	public Variable(String s, Domain<T> d, int h){
		domain = d;
		varName = s;
		hash = h;
	}

	public boolean equals(Variable<T> b) { return this.hash == b.hash; }
	public int getHash(){ return hash; }
	public String toString(){ return varName; }
	public ArrayList<T> getDomain(){ return domain.getList(); }
	public boolean isEmpty(){ return domain.isEmpty(); }
	public int getSize(){ return domain.size(); }
	public void reset(){ domain.reset(); }
	public void remove(int idx){ domain.remove(idx); }
	public void assign(T v){ domain.assign(v); }
	public boolean removed(int idx){ return domain.removed(idx); }
}