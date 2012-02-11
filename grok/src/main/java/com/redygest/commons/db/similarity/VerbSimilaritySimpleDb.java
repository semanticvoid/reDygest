package com.redygest.commons.db.similarity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.redygest.commons.db.nosql.SimpleDB;

public class VerbSimilaritySimpleDb implements ISimilarityDb {

	private SimpleDB db = null;

	/**
	 * Constructor
	 */
	public VerbSimilaritySimpleDb() {
		db = SimpleDB.getInstance();
	}

	public Map<String, Double> getSimilarity(String w1, String w2) {
		Map<String, Double> simMap = new HashMap<String, Double>();
		StringBuffer query = new StringBuffer(
				"select * from verbsimilarity where ItemName() = '");
		
		if (db != null && w1 != null && w2 != null) {
			String key = getKey(w1, w2, "#");
			query.append(key);
			query.append("' limit 1");
			SelectResult result = db
					.select(new SelectRequest(query.toString()));
			if (result != null) {
				List<Item> items = result.getItems();
				if(items != null) {
					for(Item item : items) {
						List<Attribute> attributes = item.getAttributes();
						if(attributes != null) {
							for(Attribute attr : attributes) {
								simMap.put(attr.getName(), Double.valueOf(attr.getValue()));
							}
						}
					}
				}
			}
		}

		return simMap;
	}

	/**
	 * Generate key
	 * 
	 * @param w1
	 * @param w2
	 * @param delimiter
	 * @return key
	 */
	private String getKey(String w1, String w2, String delimiter) {
		String[] words = new String[2];
		w1.replaceAll(" ", "_");
		w2.replaceAll(" ", "_");
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

	public static void main(String[] args) {
		ISimilarityDb db = SimilarityDbFactory.getInstance().produce(
				SimilarityDbType.VERBSDB);
		Map<String, Double> map = db.getSimilarity("abandon", "increase");
		for(String k : map.keySet()) {
			System.out.println(k + "\t" + map.get(k));
		}
	}
}
