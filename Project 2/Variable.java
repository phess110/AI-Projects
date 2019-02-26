import java.util.HashSet;

public abstract class Variable<T>{

	private HashSet<T> domain;
	private String varName;
	private int hash;

	public Variable(String s, HashSet<T> d, int h){
		domain = d;
		varName = s;
		hash = h;
	}

	public int getHash(){
		return hash;
	}

	public String getName(){
		return varName;
	}

	public HashSet<T> getDomain(){
		return domain;
	}
}