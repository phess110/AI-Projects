import Vector;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Scanner;

//import learn.lc.core.Example;

/* Slightly modified version of Data.java provided */
public class LinearClassifierTest{
	
		public static ArrayList<Vector> readFromFile(String filename) throws IOException {

		ArrayList<Vector> examples = new ArrayList<Vector>();
		FileReader reader = new FileReader(new File(filename));
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.eolIsSignificant(true);
		Vector values = new Vector();
		boolean done = false;

		while (!done) {

			int token = tokenizer.nextToken();
			switch (token) {
			case StreamTokenizer.TT_EOF:
				done = true;
				break;
			case StreamTokenizer.TT_EOL:
				int nvalues = values.size();
				if (!examples.isEmpty() && nvalues != examples.get(0).inputs.length) {
					throw new IOException("example length mismatch: " + values.toString());
				}
				Example example = new Example(nvalues);
				example.inputs[0] = 1.0;
				for (int i=1; i < values.size(); i++) {
					example.inputs[i] = values.get(i-1);
				}
				example.output = values.lastElement();
				examples.add(example);
				values.clear();
				break;
			case StreamTokenizer.TT_NUMBER:
				values.add(tokenizer.nval);
				break;
			case StreamTokenizer.TT_WORD:
				if (!tokenizer.sval.equals(",")) {
					throw new IOException("bad word token: " + tokenizer.sval);
				}
				break;
			}
		}
		reader.close();
		return examples;
	}


}