import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.commons.db.nosql.SimpleDB;


public class Loader {
	
	public static String getKey(String w1, String w2) {
		String[] words = new String[2];
		words[0] = w1;
		words[1] = w2;
		
		List list = Arrays.asList(words);
		Collections.sort(list);
		
		StringBuffer key = new StringBuffer();
		for(Object s : list) {
			key.append((String) s + "-");
		}
		return key.toString().substring(0, key.toString().length()-1);
	}

	public static void process(String accessId, String secret, String dir) {
		SimpleDB db = new SimpleDB(accessId, secret);
		File directory = new File(dir);
		if(directory.isDirectory()) {
			File[] files = directory.listFiles();
			
			for(File file : files) {
				BufferedReader rdr;
				try {
					rdr = new BufferedReader(new FileReader(file));
				} catch(Exception e) {
					e.printStackTrace();
					return;
				}
				
				String line;
				String mVerb = file.getName();
				mVerb = (mVerb.split("#"))[0];
				int i = 0;
				try {
					while((line = rdr.readLine()) != null) {
						if(i == 0) {
							i++;
							continue;
						}
						String[] tokens = line.split("[ #]+");
						String score = tokens[0];
						String sVerb = tokens[1];
						Map<String, String> sim = new HashMap<String, String>();
						sim.put("score", score);
						try {
							db.putAttributes("verbsimilarity", getKey(mVerb, sVerb), sim);
//							System.out.println(getKey(mVerb, sVerb) + "\t" + score);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 3) {
			System.err.println("java Loader <accessid> <secret> <verb sim dir>");
		}
		
		String accessId = args[0];
		String secret = args[1];
		String file= args[2];
		
		process(accessId, secret, file);
	}

}
