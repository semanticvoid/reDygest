package com.redygest.redundancy.clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.redygest.commons.data.Cluster;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Tweet;
import com.redygest.redundancy.similarity.BaseLineDataSimilarity;
import com.redygest.redundancy.similarity.IDataSimilarity;
import com.redygest.redundancy.similarity.score.ISimilarityScore;
import com.redygest.redundancy.similarity.score.SimScoreFactory;
import com.redygest.redundancy.similarity.score.SimScoreFactory.Score;

public class BaselineClustering implements IClustering {

	public List<Cluster> cluster(List<Data> data) {
		List<Cluster> clusters = new ArrayList<Cluster>();
		List<Boolean> done = new ArrayList<Boolean>();
		for (int i = 0; i < data.size(); i++) {
			done.add(false);
		}

		List<ISimilarityScore> scoringFunctions = new ArrayList<ISimilarityScore>();
		scoringFunctions.add(SimScoreFactory.produceScore(Score.EXACTDUP));
		scoringFunctions.add(SimScoreFactory.produceScore(Score.NEARDUP));
		scoringFunctions.add(SimScoreFactory.produceScore(Score.PHRASENEARDUP));
		IDataSimilarity baseDataSimilarity = new BaseLineDataSimilarity(
				scoringFunctions);

		for (int i = 0; i < data.size(); i++) {
			if (done.get(i)) {
				continue;
			}
			Cluster c = new Cluster();
			c.add(data.get(i));
			done.set(i, true);
			for (int j = 0; j < data.size(); j++) {
				if (j == i || done.get(j)) {
					continue;
				}
				double score = baseDataSimilarity.similarity(data.get(i),
						data.get(j));
				if (score == 1) {
					c.add(data.get(j));
					done.set(j, true);
				}
			}
			if (c.size() != 0) {
				clusters.add(c);
			}
		}
		return clusters;
	}

	public static void main(String[] args) {
		BaselineClustering bsc = new BaselineClustering();
		Date start = new Date();
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(
							"/Users/tejaswi/Documents/workspace/reDygest/sandbox/experiments/1.clinton"));
			String line = null;
			List<Data> data = new ArrayList<Data>();
			int id = 0;
			while ((line = br.readLine()) != null) {
				String json = "{\"text\":\"" + line.split("\\s+", 3)[2] + "\"}";
				try {
					id++;
					Data d = new Tweet(json, String.valueOf(id));
					data.add(d);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<Cluster> clusters = bsc.cluster(data);
			Date end = new Date();
			for (Cluster c : clusters) {
				System.out
						.println("=====================Cluster==================");
				for (Data d : c) {
					System.out.println(d.getValue(DataType.BODY));
				}
				System.out
						.println("==============================================");
			}
			System.out.println("Total time: "
					+ (end.getTime() - start.getTime()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
