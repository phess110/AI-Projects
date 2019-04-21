import java.lang.Math;
import java.util.Random;
import LinearClassifierTest;
import Vector;

public class LinearClassifier{
	
	static int N; // 1 + arity(example)
	static int numIter = 1000; // -n flag
	static Double alpha = 0.0625; // -a
	static boolean decay = false; // -d
	static boolean logisticThreshold = false; // -s
	static final Random randomizer = new Random();

	public static Double hypothesis(Vector w, Vector input){
		return threshold(input.dotProduct(w));
	}

	// default hard threshold, use -l for soft threshold
	public static Double threshold(Double prod){
		if(logisticThreshold){
			return logisticFunc(prod);
		}else{
			return (prod >= 0) ? 1 : 0; // for hard threshold
		}
	}

	public static Double logisticFunc(Double input){
		return 1/(1 + Math.exp(-input));
	}

	public static void update(Vector weights, Vector example, Double y, int iter){
		Double hw = hypothesis(weights, example);
		Double ss = stepSize(iter);

		if(logisticThreshold){
			for(int i = 0; i < weights.size(); i++){
				weights.set(i, weights.get(i) + ss * (y-hw) * hw * (1-hw) * example.get(i));
			}
		}else{
			for(int i = 0; i < weights.size(); i++){
				weights.set(i, weights.get(i) + ss * (y-hw) * example.get(i));
			}
		}
	}

	// -a followed by a double to specify step size, -d to use O(1/t) decay
	public static Double stepSize(int iter){
		if(decay){
			return alpha/(alpha + iter);
		}else{
			return alpha;
		}
	}

	public static void learn(Vector weights, ArrayList<Vector> examples, Vector answer){
		for(int i = 1; i <= numIter; i++){
			int sample = randomizer.nextInt();
			update(weights, examples.get(sample), answer.get(sample), i);
		}
	}

	public static void main(String[] args){
		String filename;
		try{
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-f")){
					filename = args[++i];
				}else if(args[i].equals("-n")){
					numIter = Integer.parseInt(args[++i]);
				}else if(args[i].equals("-d")){
					decay = true;
				}else if(args[i].equals("-a")){
					alpha = Double.parseDouble(args[++i]);
				}else if(args[i].equals("-l")){
					logisticThreshold = true;
				}
			}
		}catch(Exception e){
			// bad arguments 
		}

		ArrayList<Vector> examples = LinearClassifierTest.readFromFile(filename); 

		// learn

		// display errors
		// display graph
	}
}
