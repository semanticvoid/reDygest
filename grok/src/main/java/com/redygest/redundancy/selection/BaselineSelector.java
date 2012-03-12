package com.redygest.redundancy.selection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.redygest.commons.data.Cluster;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Tweet;
import com.redygest.commons.util.Counter;

public class BaselineSelector implements ISelector {

	private Counter<String> getSignature(Cluster c) {
		Counter<String> sig = new Counter<String>();
		List<Data> data = c.getData();
		for (Data d : data) {
			sig.incrementAll(d.getValues(DataType.BODY_TOKENIZED), 1.0);
		}
		return sig;
	}

	public List<Data> select(List<Cluster> clusters) {
		List<Data> data = new ArrayList<Data>();
		List<Cluster> merged_clusters = new ArrayList<Cluster>();
		List<Boolean> done = new ArrayList<Boolean>();
		for (int i = 0; i < data.size(); i++) {
			done.add(false);
		}
		for (int i = 0; i < clusters.size(); i++) {
			if (done.get(i)) {
				continue;
			}
			Cluster c = new Cluster();
			done.set(i, true);

			List<Data> data_i = clusters.get(i).getData();
			for (int z = 0; z < data_i.size(); z++) {
				c.add(data_i.get(z));
			}

			Counter<String> sig_i = getSignature(clusters.get(i));
			for (int j = 0; j < clusters.size(); j++) {
				if (j == i || done.get(j)) {
					continue;
				}
				Counter<String> sig_j = getSignature(clusters.get(j));

				String[] tokens1 = (String[]) sig_i.keySet().toArray();
				String[] tokens2 = (String[]) sig_j.keySet().toArray();
				int aNb = 0;
				for (int x = 0; x < tokens1.length; x++) {
					for (int y = 0; y < tokens2.length; y++) {
						if (tokens1[x].equalsIgnoreCase(tokens2[y])) {
							aNb++;
							break;
						}
					}
				}

				for (int x = 0; x < tokens2.length; x++) {
					for (int y = 0; y < tokens1.length; y++) {
						if (tokens2[x].equalsIgnoreCase(tokens1[y])) {
							aNb++;
							break;
						}
					}
				}
				double sim = 0;
				double sim1 = ((aNb * 1.0) / (tokens1.length));
				double sim2 = ((aNb * 1.0) / (tokens2.length));
				if (sim1 > sim2) {
					sim = sim1;
				} else {
					sim = sim2;
				}

				if (sim > 0.5) {
					System.out.println("Merging clusters");
					List<Data> data_j = clusters.get(j).getData();
					for (int z = 0; z < data_j.size(); z++) {
						c.add(data_j.get(z));
					}
					merged_clusters.add(c);
					done.set(j, false);
				}
			}
		}

		for (Cluster c : merged_clusters) {
			List<Data> data_cluster = c.getData();
			data.add(data_cluster.get(0));
		}

		return data;
	}

	public static void main(String[] args) {
		BaselineSelector bls = new BaselineSelector();
		// BaselineClustering bsc = new BaselineClustering();
		// Date start = new Date();
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(
							"/Users/tejaswi/Documents/workspace/reDygest/sandbox/tst/redundancy.txt"));
			String line = null;
			// List<Data> data = new ArrayList<Data>();
			// int id =0;
			// while ((line = br.readLine()) != null) {
			// String json = "{\"text\":\"" + line.split("\\s+", 3)[2] + "\"}";
			// try {
			// id++;
			// Data d = new Tweet(json, String.valueOf(id));
			// data.add(d);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// List<Cluster> clusters = bsc.cluster(data);
			// List<Data> final_data = bls.select(clusters);
			// Date end = new Date();
			// for (Cluster c : clusters) {
			// System.out.println("=====================Cluster==================");
			// for (Data d : c.getData()) {
			// System.out.println(d.getValue(DataType.BODY));
			// }
			// System.out.println("==============================================");
			// }
			List<Cluster> clusters = new ArrayList<Cluster>();
			while ((line = br.readLine()) != null) {
				if (line.contains("=Cluster=")) {
					Cluster c = new Cluster();
					line = br.readLine().trim();
					while (!line.contains("===")) {
						if (line.length() == 0)
							continue;
						try {
							String json = "{\"text\":\"" + line + "\"}";
							Data d = new Tweet(json, "1");
							c.add(d);
						} catch (Exception e) {
							e.printStackTrace();
						}
						line = br.readLine();
					}
					clusters.add(c);
				}
			}
			List<Data> data = bls.select(clusters);
			for (Data d : data) {
				System.out.println(d.getValue(DataType.BODY));
			}

			// System.out.println("Total time: "+(end.getTime()-start.getTime()));
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
