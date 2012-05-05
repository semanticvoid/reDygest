package com.redygest.piggybank.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.util.hash.Hash;
import org.apache.hadoop.util.hash.MurmurHash;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank function for Shingling
 * 
 * @author akishore
 * 
 */
public class Shingle extends EvalFunc<Tuple> {

	// List from Lucene
	public static final String[] ENGLISH_STOP_WORDS = { "a", "and", "are",
			"as", "at", "be", "but", "by", "for", "if", "in", "into", "is",
			"it", "no", "not", "of", "on", "or", "s", "such", "t", "that",
			"the", "their", "then", "there", "these", "they", "this", "to",
			"was", "will", "with" };

	private static Set<String> stopWords = new HashSet<String>();
	private static Hash mh = MurmurHash.getInstance();

	static {
		for (String w : ENGLISH_STOP_WORDS) {
			stopWords.add(w);
		}
	}

	@Override
	public Tuple exec(Tuple input) throws IOException {
		HashMap<String, Integer> shingles = new HashMap<String, Integer>();
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {
			String line = (String) input.get(0);
			int k = (Integer) input.get(1);

			if (line != null) {
				// lower & tokenize
				line = line.replaceAll("[\\.!?%$#@*()':;,\"]", "");
				// remove html tags if any
				line = line.replaceAll("<.*>", "");
				String[] tokens = line.toLowerCase().split("[\\s]+");

				// remove stop words
				ArrayList<String> terms = new ArrayList<String>();
				for (String s : tokens) {
					if (!stopWords.contains(s)) {
						terms.add(s);
					}
				}

				// generate shingles of size k
				for (int i = 0; i <= terms.size(); i++) {
					if (i - k >= 0) {
						StringBuffer buf = new StringBuffer();
						for (int j = (i - k); j < i; j++) {
							buf.append(terms.get(j) + " ");
						}
						String s = buf.toString().trim();
						if (!shingles.containsKey(s)) {
							shingles.put(s, mh.hash(s.getBytes()));
						}
					}
				}

				// add shingles to output tuple
				for (String key : shingles.keySet()) {
					oTuple.append(shingles.get(key));
				}
			}

			return oTuple;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
