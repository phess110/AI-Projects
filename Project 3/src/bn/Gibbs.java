import bn.core.Domain;
import bn.core.Value;
import bn.core.RandomVariable;
import bn.core.BayesianNetwork;
import bn.base.*;
import bn.parser.*;
import bn.util.*;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Gibbs implements ApproximateBayesianInference{

	static int N;
	static final String format = "usage: java -cp \"./bin\" Gibbs filename.[xml|bif] [numSamples] [queryVar] [evidenceVar [value in domain]]*";
	static List<RandomVariable> sortedVars; 
	static ArrayMap<RandomVariable, Set<RandomVariable>> children; // precalculate children of every node

	public static Distribution query(RandomVariable X, Assignment e, BayesianNetwork bn){ 
		sortedVars = bn.getVariablesSortedTopologically();
		children = new ArrayMap<RandomVariable, Set<RandomVariable>>(sortedVars.size());

		for(RandomVariable v : sortedVars){ 
			children.put(v, bn.getChildren(v));
		}
		return gibbsSample(X,e,bn); 
	}

	public static Distribution gibbsSample(RandomVariable X, Assignment e, BayesianNetwork bn){
		Distribution dist = new Distribution(X); 
		for(Value x: X.getDomain()){
			dist.set(x, 0);
		}

		Set<RandomVariable> nonEvidence = new ArraySet<RandomVariable>();
		for(RandomVariable v : sortedVars){
			if(!e.containsKey(v)){
				nonEvidence.add(v);
				e.put(v, ApproximateBayesianInference.getRandomInitialValue(v)); // randomly initialize e for nonEvidence variables
			}
		}

		/*print assignment
		for(RandomVariable v : sortedVars){
			System.out.println(((NamedVariable)v).toString() + " = " + e.get(v).toString());
		}
		*/

		for(int i = 1; i <= N; i++){
			for(RandomVariable Z : nonEvidence){ 
				Distribution samplingDist = getMBDistribution(bn, Z, e.copy()); 
				e.put(Z, ApproximateBayesianInference.getSample(samplingDist)); // sample according to P(z|markovblanket(z)) (eq. 14.12)
			}
			Value o = e.get(X);
			dist.set(o, dist.get(o) + 1);
		}

		dist.normalize();
		return dist;
	}

	/* Returns P(Z|MB(Z)) = alpha * P(z|parents(Z)) * PROD_(y_j in Children(Z)) P(y_j | parents(Y_j))
	Warning: e is modified by this procedure, be sure to pass a copy of e rather than the real thing
	*/
	public static Distribution getMBDistribution(BayesianNetwork bn, RandomVariable Z, Assignment e){
		Distribution dist = new Distribution(Z);

		for(Value val : Z.getDomain()){
			e.put(Z, val);
			Double prob = bn.getProbability(Z, e); 
			for(RandomVariable c : children.get(Z)){
				prob *= bn.getProbability(c, e);
			}
			dist.put(val, prob);
		}

		dist.normalize();
		return dist;
	}

	public static void main(String [] argv) throws IOException, ParserConfigurationException, SAXException{
		BayesianNetwork network;
		Boolean isXML = true;

		if (argv.length < 3 || argv.length % 2 != 1 || !(argv[0].contains(".bif") || (isXML = argv[0].contains(".xml")))) {
			System.err.println(format);
		}

		String infilename = "./bin/examples/" + argv[0];

		try { N = Integer.parseInt(argv[1]); }
		catch (NumberFormatException e){ N = 1000; }

		if(isXML){
			XMLBIFParser xp = new XMLBIFParser();
			network = xp.readNetworkFromFile(infilename);
		}else{
			BIFParser p = new BIFParser(new FileInputStream(infilename));
			network = p.parseNetwork();
		}

		RandomVariable x = network.getVariableByName(argv[2]);
		Assignment e = new Assignment();
		ApproximateBayesianInference.readEvidence(argv, network, e);

		Distribution d = query(x, e, network);
		for(Map.Entry<Value,Double> entry : d.entrySet()){
			System.out.println("P(" + argv[2] + " = " + entry.getKey() + "|e) = " + entry.getValue());
		}
	}
}