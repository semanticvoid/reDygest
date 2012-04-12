/**
 * 
 */
package com.redygest.grok.journalist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Query;
import com.redygest.commons.data.Story;
import com.redygest.commons.preprocessor.twitter.PreprocessorZ20120215;
import com.redygest.grok.features.computation.FeaturesComputation;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.prefilter.PrefilterRunner;
import com.redygest.grok.prefilter.PrefilterType;
import com.redygest.grok.ranking.ClusterEntityPagerankSumRanking;
import com.redygest.grok.ranking.IRanking;
import com.redygest.grok.selection.ISelector;
import com.redygest.grok.selection.mmr.BaselineMMRSelector;

/**
 * Journalist 001 (Naming convention 'Journalist 0.0.1')
 * 
 * This is the first version of the production journalist for alpha release
 * 
 * Algo:
 * 
 * Step 1: Prefilter (done in BaseJournalist)
 * 
 * Step 2: Preprocess (done in BaseJournalist)
 * 
 * Step 3: Feature Extraction
 * 
 * Step 4: Generate graph
 * 
 * Step 5: Compute PageRanks
 * 
 * Step 6: Get top PageRank nodes
 * 
 * Step 7: Filter Graph to top PageRank nodes
 * 
 * Step 8: Community Detection
 * 
 * ----\\ Step 9: Cluster (Community Tweets) \\---
 * 
 * Step 9: Selection (form Story)
 * 
 */
public class Journalist001 extends BaseJournalist {

	protected String scriptsDir;

	/**
	 * Constructor
	 */
	public Journalist001(String scriptsDir) {
		super();
		this.scriptsDir = scriptsDir;
		// default twitter preprocessor
		this.preprocessor = new PreprocessorZ20120215();
		// prefilter setup
		this.prefilterRunner = new PrefilterRunner(
				PrefilterType.NONENLGISH_LANG_FILTER,
				PrefilterType.REPLY_TWEET_FILTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	protected Story process(List<Data> tweets) {
		step3();
		step4();
		step5();
		step6();
		step7();
		step8();
		return step9();
	}

	/**
	 * Compute Features
	 */
	protected void step3() {
		ConfigReader config = ConfigReader.getInstance();
		FeaturesComputation fc = new FeaturesComputation(
				config.getExtractorsList());
		try {
			FeaturesRepository repository = fc.computeFeatures(tweets);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write Tweets and generate graph
	 */
	protected void step4() {
		FeaturesRepository repository = FeaturesRepository.getInstance();
		File tweetFile = null;
		BufferedWriter writer = null;

		try {
			tweetFile = File
					.createTempFile("j001", "tweets", new File("/tmp/"));
			System.out.println("Graph file: " + tweetFile.getAbsolutePath());
			writer = new BufferedWriter(new FileWriter(tweetFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Data t : tweets) {
			Set<String> entities = new HashSet<String>();

			String id = t.getValue(DataType.RECORD_IDENTIFIER);
			FeatureVector fv = repository.getFeatureVector(id);

			// collect NP entities
			for (Variable v : fv
					.getVariablesWithAttributeType(AttributeType.NPENTITY)) {
				Attributes attrs = v.getVariableAttributes();
				List<String> attrNames = attrs
						.getAttributeNames(AttributeType.SYNONYM);
				if (attrNames != null && attrNames.size() > 0) {
					entities.add(attrNames.get(0));
				} else {
					entities.add(v.getVariableName());
				}
			}

			// collect NERs
			for (Variable v : fv
					.getVariablesWithAttributeType(AttributeType.NER_CLASS)) {
				Attributes attrs = v.getVariableAttributes();
				List<String> attrNames = attrs
						.getAttributeNames(AttributeType.SYNONYM);
				if (attrNames != null && attrNames.size() > 0) {
					entities.add(attrNames.get(0));
				} else {
					entities.add(v.getVariableName());
				}
			}

			// form json
			JSONObject jObj = new JSONObject();
			jObj.accumulate("id", t.getValue(DataType.RECORD_IDENTIFIER));
			jObj.accumulate("time", t.getValue(DataType.TIME));
			jObj.accumulate("text", t.getValue(DataType.ORIGINAL_TEXT));
			jObj.accumulate("preprocessed_text", t.getValue(DataType.BODY));
			JSONArray jEntityArr = new JSONArray();
			for (String e : entities) {
				if (!e.equals("")) {
					jEntityArr.add(e);
				}
			}
			jObj.accumulate("entities", jEntityArr);

			try {
				writer.write(jObj.toString() + "\n");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// compute graph
		String cmd = "perl " + scriptsDir + "/page_rank.pl "
				+ tweetFile.getAbsolutePath();
		exec(cmd);
	}

	/**
	 * Compute pagerank
	 */
	protected void step5() {
		// `R --no-save < pg.R`;
		String cmd = "R CMD BATCH --slave " + scriptsDir + "/pg.R";
		exec(cmd);
	}

	/**
	 * Compute top pagerank nodes
	 */
	protected void step6() {
		String cmd = "perl " + scriptsDir
				+ "/top_pagerank_nodes.pl /tmp/pagerank /tmp/eids";
		exec(cmd);
		cmd = "R CMD BATCH --slave " + scriptsDir + "/top_pg.R";
		exec(cmd);
	}

	/**
	 * Filter graph (top pagerank)
	 */
	protected void step7() {
		String cmd = "perl " + scriptsDir
				+ "/filter_graph_top_nodes.pl /tmp/pagerank.nodes /tmp/graph";
	}

	/**
	 * Community detection
	 */
	protected void step8() {
		String cmd = "R CMD BATCH --slave " + scriptsDir + "/community.R";
		exec(cmd);
		cmd = "perl " + scriptsDir
				+ "/community.pl /tmp/pagerank.nodes /tmp/membership";
		exec(cmd);
	}

	// protected List<Cluster> step9() {
	// List<Cluster> clusters = new ArrayList<Cluster>();
	// // tmp/membership
	// HashMap<String, List<String>> memberships = new HashMap<String,
	// List<String>>();
	// try {
	// BufferedReader br = new BufferedReader(new FileReader(
	// "/tmp/community"));
	// String line;
	// while ((line = br.readLine()) != null) {
	// String[] split = line.split("#");
	// if (split.length != 2)
	// continue;
	// if (memberships.containsKey(split[0])) {
	// memberships.get(split[0]).add(split[1].trim());
	// } else {
	// List<String> members = new ArrayList<String>();
	// members.add(split[1].trim());
	// memberships.put(split[0], members);
	// }
	// }
	// for (String key : memberships.keySet()) {
	// List<Data> data = new ArrayList<Data>();
	// HashSet<String> ids = new HashSet<String>();
	// for (String member : memberships.get(key)) {
	// for (Data d : tweets) {
	// if (!ids.contains(d
	// .getValue(DataType.RECORD_IDENTIFIER))) {
	// data.add(d);
	// ids.add(d.getValue(DataType.RECORD_IDENTIFIER));
	// }
	// }
	// }
	// BaselineClustering bc = new BaselineClustering();
	// List<Cluster> community_clusters = bc.cluster(data);
	// if (community_clusters != null) {
	// clusters.addAll(community_clusters);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return clusters;
	// }

	protected Story step9() {
		HashMap<String, List<String>> memberships = new HashMap<String, List<String>>();
		HashMap<String, Double> pageranks = new HashMap<String, Double>();
		List<List<Data>> communitySelectedDataChunks = new ArrayList<List<Data>>();

		// read community file and form membership entity map
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/tmp/community"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split("#");
				if (split.length != 2)
					continue;
				if (memberships.containsKey(split[0])) {
					memberships.get(split[0]).add(split[1].trim());
				} else {
					List<String> members = new ArrayList<String>();
					members.add(split[1].trim());
					memberships.put(split[0], members);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// read eids
		HashMap<Integer, String> eidMap = new HashMap<Integer, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("/tmp/eids"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" '");
				if (split.length < 2) {
					continue;
				}
				String entity = split[1];
				// chomp
				entity = entity.replaceAll("'$", "");
				eidMap.put(Integer.valueOf(split[0]), entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// read pageranks and map to eids in 'pageranks' data struct
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/tmp/pagerank"));
			String line;
			boolean skip = true;
			while ((line = br.readLine()) != null) {
				if (skip) {
					skip = false;
					continue;
				}

				String[] split = line.split("[ ]+");
				if (split.length < 2) {
					continue;
				}
				int eid = Integer.valueOf(split[0].replaceAll("\"", ""));
				double pg = Double.valueOf(split[1]);

				if (eidMap.containsKey(eid)) {
					String entity = eidMap.get(eid);
					pageranks.put(entity, pg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// for all communities
		int clusterNum = 1;
		for (String communityId : memberships.keySet()) {
			System.out.println("processing cluster " + clusterNum++ + " of "
					+ memberships.keySet().size());
			List<String> members = memberships.get(communityId);
			// form community query
			Query query = new Query(members);
			// rank the data wrt query
			IRanking ranking = new ClusterEntityPagerankSumRanking(this.tweets,
					pageranks);
			List<Data> rankedData = ranking.rank(query);

			// select
			ISelector selector = new BaselineMMRSelector(rankedData, pageranks);
			// TODO default size 10
			List<Data> data = selector.select(10);

			// add to community data chunks
			communitySelectedDataChunks.add(data);

			if (clusterNum == 11) {
				break;
			}
		}

		// form story from community data chunks
		StringBuffer sb = new StringBuffer();
		String title = null;
		double maxScore = Double.MIN_VALUE;
		if (communitySelectedDataChunks != null) {
			for (List<Data> dataChunk : communitySelectedDataChunks) {
				for (Data d : dataChunk) {
					sb.append(d.getValue(DataType.ORIGINAL_TEXT) + "\t"
							+ d.getValue(DataType.SCORE));
					double score = Double.valueOf(d.getValue(DataType.SCORE));
					sb.append("\n");

					if (score > maxScore) {
						maxScore = score;
						title = d.getValue(DataType.ORIGINAL_TEXT);
					}
				}

				// add new paragraph
				sb.append("\n");
			}

			return new Story(title, sb.toString());
		}

		return null;
	}

	protected void exec(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			String s;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("usage: java jar ..... <scripts dir> <dataset>");
			System.exit(1);
		}

		Journalist001 j = new Journalist001(args[0]);
		j.run(args[1]);
	}

}
