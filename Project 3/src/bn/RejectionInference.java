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

public class RejectionInference implements ApproximateBayesianInference {

	static int N;
	static final String format = "usage: java -cp \"./bin\" RejectionInference filename.[xml|bif] [numSamples] [queryVar] [evidenceVar [value in domain]]*";
	static List<RandomVariable> sortedVars;

	public static Distribution query(RandomVariable X, Assignment e, BayesianNetwork bn){ 
		sortedVars = bn.getVariablesSortedTopologically();
		return rejectionSampling(X, e, bn); 
	}

	public static Distribution rejectionSampling(RandomVariable X, Assignment e, BayesianNetwork bn){
		Distribution dist = new Distribution(X); 
		for(Value x: X.getDomain()){
			dist.set(x, 0);
		}

		for(int i = 1; i <= N; i++){
			Assignment x = priorSample(bn);
			if(isConsistent(e, x)){ 
				Value o = x.get(X);
				dist.set(o, dist.get(o)+1);
			}
		}
		dist.normalize();
		return dist;
	}

	/* Check if evidence assignment e is consistent with sample */
	public static boolean isConsistent(Assignment e, Assignment sample){
		return sample.containsAll(e);
	}

	/* Return a sampling of the random variables (according to prior distribution, i.e. without evidence e) */
	public static Assignment priorSample(BayesianNetwork bn){
 		Assignment sample = new Assignment();
 		for(RandomVariable v : bn.getVariablesSortedTopologically()){
 			sample.put(v, ApproximateBayesianInference.getSample(ApproximateBayesianInference.getDistribution(bn, v, sample))); //x.copy?
 		}
 		return sample;
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
			if(entry.getValue().isNaN()){ System.out.println("Sampling unsuccessful. No samples matching evidence were generated. Try increasing N."); break;}
			System.out.println("P(" + argv[2] + " = " + entry.getKey() + "|e) = " + entry.getValue());
		}
	}

}