package com.redygest.commons.nlp.similarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.commons.nlp.WordNet;

import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;

public class VerbSimilarityLocal implements ISimilarityDb {

	private WordNet wn = null;

	/**
	 * Constructor
	 */
	public VerbSimilarityLocal() {
		wn = WordNet.getSingleton();
	}

	public Map<String, Double> getSimilarity(String w1, String w2) {
		Map<String, Double> simMap = new HashMap<String, Double>();
				
		if (wn != null && w1 != null && w2 != null) {
			List<ISynsetID> syns1 = wn.getSynsets(w1.toLowerCase(), POS.VERB);
			List<ISynsetID> syns2 = wn.getSynsets(w2.toLowerCase(), POS.VERB);
			double sim = 0;
			for (ISynsetID syn1 : syns1) {
				for (ISynsetID syn2 : syns2) {
					sim = wn.getSimilarity(syn1, syn2);
					if(sim > 0) {
						simMap.put(w1 + "~" + w2, sim);
					}
				}
			}
		}

		return simMap;
	}

	
	public static void main(String[] args) {
		ISimilarityDb db = SimilarityDbFactory.getInstance().produce(
				SimilarityDbType.VERBLOCAL);
		Map<String, Double> map = db.getSimilarity("accept", "admit");
		for(String k : map.keySet()) {
			System.out.println(k + "\t" + map.get(k));
		}
	}
}
