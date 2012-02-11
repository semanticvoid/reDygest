/**
 * 
 */
package com.redygest.commons.db;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jsl.measure.jwi.LinSimilarity;

import com.redygest.commons.config.ConfigReader;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

/**
 * This class is the local wrapper over the WordNet library (so that other
 * classes are free from specific implementation details of the library). This
 * is a singleton class.
 * 
 * @author anand
 * 
 */
public class WordNet {

	private static WordNet wn = null;
	private IDictionary dict = null;
	private Dictionary dictionary = null;

	// Should write them to persistence cache object.
	private Map<String, List<ISynsetID>> WORD_SYNSETS_MAP = new HashMap<String, List<ISynsetID>>();
	private Map<String, Double> SYNSETS_SIMILARITY_MAP = new HashMap<String, Double>();

	private WordNet() throws Exception {
		// @TODO move this path to the Environment variable WNHOME
		ConfigReader config = ConfigReader.getInstance();
		URL url = new URL("file", null, config.getWordNetDictPath());
		this.dictionary = new Dictionary(url);
		this.dict = dictionary;
		this.dict.open();
	}

	/**
	 * Method that returns the singleton WordNet obj ﻿ * @return the
	 * singleton object ﻿
	 */
	public static synchronized WordNet getSingleton() {
		if (wn == null) {
			try {
				wn = new WordNet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return wn;

	}

	public List<ISynsetID> getSynsets(String word, POS p) {
		if (WORD_SYNSETS_MAP.containsKey(word)) {
			return WORD_SYNSETS_MAP.get(word);
		} else {
			List<ISynsetID> synsetids = new ArrayList<ISynsetID>();

			// @TODO to work on this after sudheer is done with Word obj
			// right now assume NOUN
			IIndexWord idxWord = this.dict.getIndexWord(word, p);
			if (idxWord == null) {
				String newName = word;
				if (newName.length() - 1 > 0) {
					newName = newName.substring(0, newName.length() - 1);
					word = newName;
					synsetids = getSynsets(word, p);
				}
			} else {
				List<IWordID> wordIDs = idxWord.getWordIDs();
				for (IWordID wid : wordIDs) {
					IWord w = dict.getWord(wid);
					ISynset sysnet = w.getSynset();
					synsetids.add(sysnet.getID());
				}
			}
			WORD_SYNSETS_MAP.put(word, synsetids);
			return synsetids;
		}
	}

	public double getSimilarity(ISynsetID syid1, ISynsetID syid2) {
		if (SYNSETS_SIMILARITY_MAP.containsKey(String.valueOf(syid1) + "_"
				+ String.valueOf(syid2))) {
			return SYNSETS_SIMILARITY_MAP.get(String.valueOf(syid1) + "_"
					+ String.valueOf(syid2));
		} else if (SYNSETS_SIMILARITY_MAP.containsKey(String.valueOf(syid2)
				+ "_" + String.valueOf(syid1))) {
			return SYNSETS_SIMILARITY_MAP.get(String.valueOf(syid2) + "_"
					+ String.valueOf(syid1));
		} else {
			 LinSimilarity sim = new LinSimilarity(this.dictionary);
//			 JiangConrathDistance sim = new
//			 JiangConrathDistance(this.dictionary);
//			RadaDistance sim = new RadaDistance(this.dictionary);
			// JiangConrathSimilarity sim = new
			// JiangConrathSimilarity(this.dictionary);
			double score = sim.calculateSimilarity(syid1, syid2);
			SYNSETS_SIMILARITY_MAP.put(
					String.valueOf(syid1) + "_" + String.valueOf(syid2), score);
			return score;
		}
	}

	public static void main(String[] args) {
		try {
			WordNet wn = new WordNet();
			List<ISynsetID> syns1 = wn.getSynsets("run", POS.VERB);
			List<ISynsetID> syns2 = wn.getSynsets("run down", POS.VERB);
			for (ISynsetID syn1 : syns1) {
				for (ISynsetID syn2 : syns2) {
					System.out.println(syn1 + "\t" + syn2 + "\t" + wn.getSimilarity(syn1, syn2));
				}
//				System.out.println(syn1.toString());
			}
		} catch (Exception ex) {
			Logger.getLogger(WordNet.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
