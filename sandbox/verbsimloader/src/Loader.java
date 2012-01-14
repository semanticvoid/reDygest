import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;


public class Loader {
	
	public static String getKey(String w1, String w2) {
		String[] words = new String[2];
		words[0] = w1;
		words[1] = w2;
		
		List list = Arrays.asList(words);
		Collections.sort(list);
		
		StringBuffer key = new StringBuffer();
		for(Object s : list) {
			key.append((String) s + "#");
		}
		return key.toString().substring(0, key.toString().length()-1);
	}

	public static void process(String credentialsFile, String dir) {
		AmazonSimpleDB db;
		
		try {
			db = new AmazonSimpleDBClient(new PropertiesCredentials(new File(credentialsFile)));
			db.createDomain(new CreateDomainRequest("verbsimilarity"));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		File directory = new File(dir);
		if(directory.isDirectory()) {
			File[] files = directory.listFiles();
			int count = 0;
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
				List<ReplaceableItem> items = new ArrayList<ReplaceableItem>();
				Set<String> seen = new HashSet<String>();
				int itemCount = 0;
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
						if(Double.valueOf(score) == 0) {
							continue;
						}
						sim.put("score", score);
						try {
							String key = getKey(mVerb, sVerb);
							if(seen.contains(key)) {
								continue;
							}
							List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>();
							attrs.add(new ReplaceableAttribute("score", score, new Boolean(true)));
							items.add(new ReplaceableItem(key, attrs));
							itemCount++;
							seen.add(key);
//				            db.putAttributes(new PutAttributesRequest("verbsimilarity", getKey(mVerb, sVerb), attrs));
							System.out.println(getKey(mVerb, sVerb) + "\t" + score);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(itemCount == 25) {
							itemCount = 0;
							db.batchPutAttributes(new BatchPutAttributesRequest("verbsimilarity", items));
							items = new ArrayList<ReplaceableItem>();
							seen = new HashSet<String>();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				count++;
				System.out.println(count + "/" + files.length);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("java Loader <credentials file> <verb sim dir>");
		}
		
		String credFile = args[0];
		String file= args[1];
		
		process(credFile, file);
	}

}
