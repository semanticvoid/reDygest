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
import com.redygest.score.IScore;
import com.redygest.score.similarity.SimilarityScoreFactory;
import com.redygest.score.similarity.SimilarityScoreFactory.Score;

public class BaselineClustering implements IClustering {

	public List<Cluster> cluster(List<Data> data) {
		List<Cluster> clusters = new ArrayList<Cluster>();
		List<Boolean> done = new ArrayList<Boolean>();
		for (int i = 0; i < data.size(); i++) {
			done.add(false);
		}

		IScore simScore = SimilarityScoreFactory
				.produceScore(Score.HYBRIDMAX);

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
				double score = simScore.score(data.get(i), data.get(j));
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
							"/Users/akishore/projects/redygest/reDygest/sandbox/experiments/1.lokpal"));
			String line = null;
			List<Data> data = new ArrayList<Data>();
			int id = 0;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\t");
				if (tokens.length >= 3) {
					String json = "{\"text\":\"" + tokens[2] + "\"}";
					try {
						id++;
						Data d = new Tweet(json, String.valueOf(id));
						data.add(d);
					} catch (Exception e) {
						e.printStackTrace();
					}
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
					+ (end.getTime() - start.getTime()) / 1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
