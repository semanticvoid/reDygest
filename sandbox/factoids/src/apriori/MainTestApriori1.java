package apriori;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import apriori.AlgoApriori;
import apriori.ContextApriori;
import apriori.Itemsets;

/**
 * Class to test the APRIORI algorithm .
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestApriori1 {

	public static void main(String [] arg){
		// Loading the binary context (a transaction database)
		
		ContextApriori context = new ContextApriori();
		try {
			context.loadFile(fileToPath("input.txt"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		context.printContext();
		
		// Applying the APRIORI algorithm
		AlgoApriori apriori = new AlgoApriori(context);
		Itemsets patterns = apriori.runAlgorithm(0.001);
		patterns.printItemsets(context.size());
		apriori.printStats();

	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestApriori1.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
