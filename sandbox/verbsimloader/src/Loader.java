import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;

public class Loader {

	static String mVerb;
	static String mVerbSysnet;
	static List<String> lines = new ArrayList<String>();

	public static String getKey(String w1, String w2, String delimiter) {
		String[] words = new String[2];
		words[0] = w1;
		words[1] = w2;

		List list = Arrays.asList(words);
		Collections.sort(list);

		StringBuffer key = new StringBuffer();
		for (Object s : list) {
			key.append((String) s + delimiter);
		}
		return key.toString().substring(0, key.toString().length() - 1);
	}

	public static void process(String credentialsFile, String dir) {
		final AmazonSimpleDB db;

		try {
			db = new AmazonSimpleDBClient(new PropertiesCredentials(new File(
					credentialsFile)));
			// db.deleteDomain(new DeleteDomainRequest("verbsimilarity"));
			db.createDomain(new CreateDomainRequest("verbsimilarity"));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		File directory = new File(dir);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			int count = 0;
			for (File file : files) {
				BufferedReader rdr;
				try {
					rdr = new BufferedReader(new FileReader(file));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				String line;
				String fileName = file.getName();
				System.out.println("processing... " + fileName);
				mVerb = (fileName.split("#"))[0];
				mVerbSysnet = (fileName.split("\\."))[0];
				lines = new ArrayList<String>();

				// List<ReplaceableItem> items = new
				// ArrayList<ReplaceableItem>();
				// HashMap<String, ReplaceableItem> seen = new HashMap<String,
				// ReplaceableItem>();
				int itemCount = 0;
				int i = 0;
				try {
					while ((line = rdr.readLine()) != null) {

						if (i == 0) {
							i++;
							continue;
						}

						lines.add(line);
						// String[] tokens = line.split("[ ]+");
						// String score = tokens[0];
						// String sVerbSysnet = tokens[1];
						// String sVerb = (tokens[1].split("#"))[0];
						// Map<String, String> sim = new HashMap<String,
						// String>();
						// if(Double.valueOf(score) == 0) {
						// continue;
						// }
						// sim.put("score", score);
						// try {
						// String key = getKey(mVerb, sVerb, "#");
						// ReplaceableItem item = null;
						// List<ReplaceableAttribute> attrs = null;
						// if(seen.containsKey(key)) {
						// item = seen.get(key);
						// attrs = item.getAttributes();
						// } else {
						// item = new ReplaceableItem(key);
						// attrs = new ArrayList<ReplaceableAttribute>();
						// }
						//
						// attrs.add(new
						// ReplaceableAttribute(getKey(mVerbSysnet, sVerbSysnet,
						// "~"), score, new Boolean(true)));
						// item.setAttributes(attrs);
						//
						// if(!seen.containsKey(key)) {
						// items.add(item);
						// seen.put(key, item);
						// }
						//
						// itemCount++;
						// db.putAttributes(new
						// PutAttributesRequest("verbsimilarity", key, attrs));
						// // System.out.println(key + "\t" + score);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// //
						// // if(itemCount == 25) {
						// // itemCount = 0;
						// // db.batchPutAttributes(new
						// BatchPutAttributesRequest("verbsimilarity", items));
						// // items = new ArrayList<ReplaceableItem>();
						// // seen = new HashMap<String, ReplaceableItem>();
						// // }
					}

					// if(itemCount > 0) {
					// db.batchPutAttributes(new
					// BatchPutAttributesRequest("verbsimilarity", items));
					// }
				} catch (IOException e) {
					e.printStackTrace();
				}

				ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 10,
						TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(
								lines.size()));
				for (final String l : lines) {
					Runnable r = new Runnable() {

						public void run() {
							upload(l, db);
						}

					};
					pool.execute(r);
				}

				count++;
				pool.shutdown();
				try {
					pool.awaitTermination(1800, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(count + "/" + files.length);
			}
		}
	}

	public static void upload(String line, AmazonSimpleDB db) {
		String[] tokens = line.split("[ ]+");
		String score = tokens[0];
		String sVerbSysnet = tokens[1];
		String sVerb = (tokens[1].split("#"))[0];
//		Map<String, String> sim = new HashMap<String, String>();
		if (Double.valueOf(score) != 0) {
//			sim.put("score", score);
			try {
				String key = getKey(mVerb, sVerb, "#");
				ReplaceableItem item = null;
				List<ReplaceableAttribute> attrs = null;
				// if(seen.containsKey(key)) {
				// item = seen.get(key);
				// attrs = item.getAttributes();
				// } else {
				item = new ReplaceableItem(key);
				attrs = new ArrayList<ReplaceableAttribute>();
				// }

				attrs.add(new ReplaceableAttribute(getKey(mVerbSysnet,
						sVerbSysnet, "~"), score, new Boolean(true)));
				item.setAttributes(attrs);

				// if(!seen.containsKey(key)) {
				// items.add(item);
				// seen.put(key, item);
				// }

				db.putAttributes(new PutAttributesRequest("verbsimilarity",
						key, attrs));
//				 System.out.println(key + "\t" + score);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("failed to upload...\t" + mVerb + "\t" + mVerbSysnet + "\t" + sVerb + "\t" + sVerbSysnet + "\t" + score);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("java Loader <credentials file> <verb sim dir>");
		}

		String credFile = args[0];
		String file = args[1];

		process(credFile, file);
	}
}
