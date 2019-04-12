
/*
	Storage for examples, weights, output, etc. Supports dot product.
*/
public class Vector extends ArrayList<Object>{

	public Double dotProduct(Vector weights){
		if(weights.size() == 0 || this.size() != weights.size() || !(weights.get(0) instanceof Number && this.get(0) instanceof Number)){
			// error
		} 

		Double prod = 0.0;

		for(int i = 0; i < this.size(); i++){
			prod += weights.get(i) * this.get(i);
		}
		
		return prod;
	}


}