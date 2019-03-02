import java.util.ArrayList;
import java.util.BitSet;

public class Domain<T>{
	
	private ArrayList<T> domain;
	private BitSet removed; //removed(i) = true <-> domain(i) was removed during inference

	public Domain(ArrayList<T> d){
		domain = d;
		removed = new BitSet();
	}

	ArrayList<T> getList(){ return domain; }
	int size(){ return domain.size() - removed.cardinality(); }

	void assign(T v){ removed.set(0, domain.size()); removed.flip(domain.indexOf(v)); } //removes all but idx from domain
	void remove(int idx){ removed.set(idx); } //remove domain element at idx
	void reset(){ removed.clear(); } //set contains to false (full domain)
	boolean removed(int idx){ return removed.get(idx); } //has domain element at idx been removed
	boolean isEmpty(){
		for(int i = 0; i < domain.size(); i++){
			if(removed.get(i) == false){
				return false;
			}
		}
		return true;
	}
}