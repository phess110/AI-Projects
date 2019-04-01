import bn.core.Domain;
import bn.core.Value;
import bn.core.RandomVariable;
import bn.core.BayesianNetwork;
import bn.base.*;
import bn.parser.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.util.List;

public class ExactInference{
	static final String format = "usage: java -cp ./bin ExactInference filename.[xml|bif] [queryVar] [evidenceVar [value in domain]]*";

	public static Distribution query(RandomVariable x, Assignment e, BayesianNetwork network){ 
		return enumAsk(x, e, network);
	}

	public static Distribution enumAsk(RandomVariable X, Assignment e, BayesianNetwork bn){
		Domain Dx = X.getDomain();
		Distribution dist = new Distribution(X);
		if(e.containsKey(X)){ // special case where X is in the evidence
			for(Value o : Dx){
				dist.put(o, 0.0);
			}
			dist.put(e.get(X), 1.0);
			return dist;
		}
		List<RandomVariable> tSort = bn.getVariablesSortedTopologically();

		for(Value o : Dx){
			Assignment ePrime = e.copy();
			ePrime.put(X,o);
			dist.put(o, enumAll(bn, tSort, ePrime, 0));
		}
		dist.normalize();
		return dist;
	}

	public static Double enumAll(BayesianNetwork bn, List<RandomVariable> vars, Assignment e, int idx){
		if(idx >= vars.size()){ return 1.0; }
		RandomVariable Y = vars.get(idx);

		if(e.get(Y) != null){ // Y has been assigned
			return bn.getProbability(Y, e) * enumAll(bn, vars, e, idx + 1);
		}
		else{
			Double sum = 0.0;
			for(Value v : Y.getDomain()){
				Assignment ePrime = e.copy();
				ePrime.put(Y, v);
				sum += bn.getProbability(Y, ePrime) * enumAll(bn, vars, ePrime, idx + 1);
			}
			return sum;
		}
	}

	public static void main(String [] argv) throws IOException, ParserConfigurationException, SAXException{
		Boolean isXML = argv[0].contains(".xml");
		if (argv.length < 2 || argv.length % 2 != 0 || !(argv[0].contains(".bif") || isXML)) {
			System.err.println(format);
		}

		String infilename = "./bin/examples/" + argv[0];
		BayesianNetwork network;

		if(isXML){
			XMLBIFParser xp = new XMLBIFParser();
			network = xp.readNetworkFromFile(infilename);
		}else{
			BIFParser p = new BIFParser(new FileInputStream(infilename));
			network = p.parseNetwork();
		}

		RandomVariable x = network.getVariableByName(argv[1]);
		Assignment e = new Assignment();

		for(int i = 2; i < argv.length; i+=2){
			RandomVariable evar = network.getVariableByName(argv[i]);
			for(Value v: evar.getDomain()){
				if( v.toString().equals(argv[i+1]) ){
					e.put(evar, v);
					break;
				}
			}
			if(!e.containsKey(evar)){
				System.err.println("Invalid value assignment to evidence variable.");
				System.exit(1);
			}
		}

		Distribution d = query(x, e, network);
		for(Value v : x.getDomain()){
			System.out.println( "P(" + argv[1] + " = " + v.toString() + " | e) = " + d.get(v));
		}
	}

}