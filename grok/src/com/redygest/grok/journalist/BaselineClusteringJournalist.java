/**
 * 
 */
package com.redygest.grok.journalist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aliasi.cluster.CompleteLinkClusterer;
import com.aliasi.cluster.Dendrogram;
import com.aliasi.cluster.HierarchicalClusterer;
import com.aliasi.util.Distance;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Story;
import com.redygest.commons.data.Tweet;

/**
 * @author semanticvoid
 * 
 */
public class BaselineClusteringJournalist extends BaseJournalist {

	static final int MAX_CLUSTERS = 20;
	static final int MIN_CLUSTERS = 10;
	static final int SAMPLE_WINDOW = 100;

	int tweetsRead = 0;

	/**
	 * Jaccard distance
	 * 
	 * @author semanticvoid
	 * 
	 */
	static class JaccardDistance implements Distance<String> {
		@Override
		public double distance(String s1, String s2) {
			String[] tokens1 = s1.split("[ ]+");
			String[] tokens2 = s2.split("[ ]+");

			int aNb = 0;
			for (int i = 0; i < tokens1.length; i++) {
				for (int j = 0; j < tokens2.length; j++) {
					if (tokens1[i].equalsIgnoreCase(tokens2[j])) {
						aNb++;
						break;
					}
				}
			}

			double sim = ((2.0 * aNb) / (tokens1.length + tokens2.length));
			if (sim > 1) {
				sim = 1;
			}

			return 1 - sim;
		}
	}

	/**
	 * reservior sampling
	 * 
	 * @param tweets
	 * @return sampled tweets
	 */
	protected void addTweet(Tweet t) {
		tweetsRead++;
		
		if (tweets.size() < SAMPLE_WINDOW) {
			tweets.add(t);
		} else {
			double rand = Math.random();
			int index = (int) ((int) tweetsRead * rand);
			if (index < tweets.size()) {
				tweets.set(index, t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	Story process(List<Data> tweets) {
		Distance<String> JACCARD_DISTANCE = new JaccardDistance();
		Set<String> inputSet = new HashSet<String>();
		for (Data t : tweets) {
			inputSet.add(t.getValue(DataType.BODY));
		}

		HierarchicalClusterer<String> clClusterer = new CompleteLinkClusterer<String>(
				1, JACCARD_DISTANCE);
		Dendrogram<String> clDendrogram = clClusterer
				.hierarchicalCluster(inputSet);
		double k = 0;
		while (k < MIN_CLUSTERS) {
			k = Math.random();
			k = MAX_CLUSTERS * k;
		}

		Set<Set<String>> clKClustering = clDendrogram.partitionK((int) k);

		StringBuffer buf = new StringBuffer();
		for (Set<String> c : clKClustering) {
			// pick one from each cluster
			for (String s : c) {
				buf.append(s);
				break;
			}
		}

		Story story = new Story(buf.toString().substring(0, 200),
				buf.toString());

		return story;
	}

}
