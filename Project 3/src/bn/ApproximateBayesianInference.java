import java.util.Random;
import bn.core.Domain;
import bn.core.Value;
import bn.core.RandomVariable;
import bn.core.BayesianNetwork;
import bn.base.*;
import java.util.Map;
import java.util.Set;

public interface ApproximateBayesianInference{

	static Random randomizer = new Random();

	/*
		Returns the distribution to rv X given the assignment in e.
	*/
	public static Distribution getDistribution(BayesianNetwork bn, RandomVariable X, Assignment e){
		Domain domx = X.getDomain();
		Distribution distx = new Distribution(X);
		Double cmf = 0.0;
		for(Value o : domx){
			e.put(X, o);
			cmf += bn.getProbability(X,e);
			distx.set(o, cmf);
			e.remove(X);
		}
		return distx;
	}

	/*
	Returns a random sample given a distribution. 
	*/
	public static Value getSample(Distribution dist){
		Double rand = randomizer.nextDouble();
		Map.Entry<Value,Double> e = null;
		for(Map.Entry<Value,Double> entry : dist.entrySet()){
			if(rand <= entry.getValue()){
				return entry.getKey();
			}
			e = entry;
		}
		return e.getKey();
	}

	/*
		Only use for Gibbs sampling. Selects initial values for X, according to a uniform distribution.
	*/
	public static Value getRandomInitialValue(RandomVariable X){
		Domain domX = X.getDomain();
		int idx = randomizer.nextInt(domX.size());
		for(Value v : domX){ 
			if(idx == 0){ return v; }
			idx--; 
		}
		return null; //unreachable?
	}

	/* Restrict assignment to only include values for given set */
	public static Assignment restrictAssignment(Assignment e, Set<RandomVariable> restict){
		Assignment eResticted = new Assignment();
		for(RandomVariable v : restict){
			eResticted.put(v, e.get(v));
		}
		return eResticted;
	}

	/* 
	Must ensure len(argv) >= 3 and is odd before calling. 
	Reads evidence variables from command-line input.
	*/
	public static void readEvidence(String[] argv, BayesianNetwork bn, Assignment e){
		for(int i = 3; i < argv.length; i+=2){
			RandomVariable evar = bn.getVariableByName(argv[i]);
			for(Value v: evar.getDomain()){
				if( v.toString().equalsIgnoreCase(argv[i+1]) ){
					e.put(evar, v);
					break;
				}
			}
		}
	}

}