import bn.core.Domain;
import bn.core.Value;
import bn.core.RandomVariable;
import bn.core.BayesianNetwork;
import bn.base.*;
import bn.parser.*;
import java.io.*;
import java.util.Map;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class WeightedInference implements ApproximateBayesianInference{

	static int N;
	static final String format = "usage: java -cp ./bin WeightedInference filename.[xml|bif] [numSamples] [queryVar] [evidenceVar [value in domain]]*";
	static List<RandomVariable> sortedVars; 

	public static Distribution query(RandomVariable X, Assignment e, BayesianNetwork bn){ 
		sortedVars = bn.getVariablesSortedTopologically();
		return weightedInference(X,e,bn); 
	}

	public static Distribution weightedInference(RandomVariable X, Assignment e, BayesianNetwork bn){
		Distribution dist = new Distribution(X); 
		for(Value x: X.getDomain()){
			dist.set(x, 0);
		}

		Double weight;
		Value o;
		Assignment sample;
		for(int i = 1; i <= N; i++){
			sample = e.copy();
			weight = weightedSample(sample, e, bn);
			o = sample.get(X);
			dist.set(o, dist.get(o) + weight);
		}

		dist.normalize();
		return dist;
	}

	public static Double weightedSample(Assignment sample, Assignment e, BayesianNetwork bn){
		Double w = 1.0;
		for(RandomVariable v : sortedVars){
			if(e.containsKey(v)){
				w *= bn.getProbability(v, sample);
			}else{
 				sample.put(v, ApproximateBayesianInference.getSample(ApproximateBayesianInference.getDistribution(bn, v, sample)));
 			}
 		}
 		return w;
	}

	public static void main(String [] argv) throws IOException, ParserConfigurationException, SAXException{
		BayesianNetwork network;
		Boolean isXML = argv[0].contains(".xml");
		if (argv.length < 3 || argv.length % 2 != 1 || !(argv[0].contains(".bif") || isXML)) {
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