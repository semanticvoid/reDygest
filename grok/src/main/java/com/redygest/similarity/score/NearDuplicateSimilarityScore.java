package com.redygest.similarity.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.hash.MurmurHash;
import com.redygest.commons.nlp.StopWord;

public class NearDuplicateSimilarityScore implements ISimilarityScore {

	// number of hash functions
	private final int numHash;

	/**
	 * Constructor
	 * 
	 * @param numHash
	 *            - the number if hash functions
	 */
	public NearDuplicateSimilarityScore(int numHash) {
		this.numHash = numHash;
	}

	public double score(Data d1, Data d2) {
		List<String> tokens1 = d1.getValues(DataType.BODY_TOKENIZED);
		List<String> tokens2 = d2.getValues(DataType.BODY_TOKENIZED);

		if (tokens1 != null && tokens2 != null) {
			List<String> tokens1NoStopWords = new ArrayList<String>();
			List<String> tokens2NoStopWords = new ArrayList<String>();

			// remove stop words
			Set<String> seen = new HashSet<String>();
			for (String t : tokens1) {
				if (!StopWord.isStopWord(t) && !seen.contains(t) && !isPunct(t)) {
					tokens1NoStopWords.add(t.toLowerCase());
					seen.add(t.toLowerCase());
				}
			}
			// Collections.sort(tokens1NoStopWords);

			// remove stop words
			seen = new HashSet<String>();
			for (String t : tokens2) {
				if (!StopWord.isStopWord(t) && !seen.contains(t) && !isPunct(t)) {
					tokens2NoStopWords.add(t.toLowerCase());
					seen.add(t.toLowerCase());
				}
			}
			// Collections.sort(tokens2NoStopWords);

			List<Integer> sigs1 = generateSigs(tokens1NoStopWords);
			List<Integer> sigs2 = generateSigs(tokens2NoStopWords);
			if (sigs1 != null && sigs2 != null) {
				return jaccardSim(sigs1, sigs2);
			}
		}

		return 0;
	}

	private boolean isPunct(String s) {
		return !s.matches("^[a-zA-Z0-9].*");
	}

	/**
	 * Calculate Jaccard Sim between the two lists
	 * 
	 * @param tokens1
	 * @param tokens2
	 * @return
	 */
	private double jaccardSim(List<Integer> tokens1, List<Integer> tokens2) {
		if (tokens1 == null || tokens2 == null) {
			return 0;
		}

		int aNb = 0;
		for (int i = 0; i < tokens1.size(); i++) {
			for (int j = 0; j < tokens2.size(); j++) {
				if (tokens1.get(i).compareTo(tokens2.get(j)) == 0) {
					aNb++;
					break;
				}
			}
		}

		for (int i = 0; i < tokens2.size(); i++) {
			for (int j = 0; j < tokens1.size(); j++) {
				if (tokens2.get(i).compareTo(tokens1.get(j)) == 0) {
					aNb++;
					break;
				}
			}
		}

		double sim = ((aNb * 1.0) / (tokens1.size() + tokens2.size()));
		if (sim > 1) {
			sim = 1;
		}

		return sim;
	}

	private List<Integer> generateSigs(List<String> tokens) {
		MurmurHash hash = MurmurHash.getInstance();
		List<Integer> signatures = new ArrayList<Integer>();

		for (int n = 1; n <= numHash; n++) {
			List<Integer> nSigs = new ArrayList<Integer>();

			for (int i = 0; i < tokens.size(); i++) {
				if (i - 1 >= 0) {
					StringBuffer buf = new StringBuffer();
					for (int j = i - 1; j <= i; j++) {
						buf.append(tokens.get(j) + " ");
					}
					int sig = hash.hash(buf.toString().trim().getBytes(), buf
							.toString().trim().getBytes().length, n);
					nSigs.add(sig);
				}
			}

			Collections.sort(nSigs);
			if (nSigs.size() > 0) {
				signatures.add(nSigs.get(0));
			}
		}

		return signatures;
	}

}
