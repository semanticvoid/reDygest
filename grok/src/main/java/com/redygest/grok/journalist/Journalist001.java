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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.redygest.commons.data.Community;
import com.redygest.commons.data.Community.CommunityAttribute;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Entity;
import com.redygest.commons.data.Entity.EntityType;
import com.redygest.commons.data.EntitySet;
import com.redygest.commons.data.Query;
import com.redygest.commons.data.Story;
import com.redygest.commons.preprocessor.twitter.PreprocessorZ20120215;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.IAttribute;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.filtering.data.postextraction.PostExtractionPrefilterRunner;
import com.redygest.grok.filtering.data.postextraction.PostExtractionPrefilterType;
import com.redygest.grok.filtering.data.preextraction.PreExtractionPrefilterRunner;
import com.redygest.grok.filtering.data.preextraction.PreExtractionPrefilterType;
import com.redygest.grok.filtering.entity.EntityFilterRunner;
import com.redygest.grok.filtering.entity.EntityFilterType;
import com.redygest.grok.ranking.community.EntityPagerankCommunityRanking;
import com.redygest.grok.ranking.community.ICommunityRanking;
import com.redygest.grok.ranking.data.IRanking;
import com.redygest.grok.ranking.data.RetweetCountPagerankSumRanking;
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
 * Step 3: Feature Extraction & post filter (done in BaseJournalist)
 * 
 * Step 4a: Entity Collection & Filtering
 * 
 * Step 4b: Generate graph
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
		// pre-extraction filter setup
		this.preExtractionFilterRunner = new PreExtractionPrefilterRunner(
				PreExtractionPrefilterType.NONENLGISH_LANG_FILTER,
				PreExtractionPrefilterType.REPLY_TWEET_FILTER);
		// post-extraction filter setup
		this.postExtractionfilterRunner = new PostExtractionPrefilterRunner(
				PostExtractionPrefilterType.FACT_OPINION_FILTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	protected Story process(List<Data> tweets) {
		// step4a();
		step4();
		step5();
		step6();
		step7();
		step8();
		return step9();
	}

	protected void step4a() {
		FeaturesRepository repository = FeaturesRepository.getInstance();

		// entity set
		EntitySet entitySet = new EntitySet();

		// Entity Collection

		// get global feature vectors
		FeatureVector globalFeatureVector = repository
				.getFeatureVector(FeatureVectorCollection.GLOBAL_RECORD_IDENTIFIER);
		// get ENTITY variables
		List<IVariable> variables = globalFeatureVector
				.getVariablesWithAttributeType(AttributeId.ENTITY);
		for (IVariable var : variables) {
			String entityName = var.getVariableName();
			Attributes attrs = var.getVariableAttributes();
			long frequency = 1;
			List<IVariable> coOccurrences = null;
			EntityType type = null;

			if (attrs != null
					&& attrs.containsAttributeType(AttributeId.NPENTITY)) {
				type = EntityType.NP;
			} else {
				type = EntityType.NE;
			}

			if (attrs != null
					&& attrs.containsAttributeType(AttributeId.FREQUENCY)
					&& attrs.getAttributes(AttributeId.FREQUENCY) != null) {
				frequency = attrs.getAttributes(AttributeId.FREQUENCY)
						.getLong();

			}
			if (attrs != null
					&& attrs
							.containsAttributeType(AttributeId.ENTITYCOOCCURENCE)
					&& attrs.getAttributes(AttributeId.ENTITYCOOCCURENCE) != null) {
				coOccurrences = attrs.getAttributes(
						AttributeId.ENTITYCOOCCURENCE).getList();
			}
			List<Entity> coOccurringEntities = null;
			if (coOccurrences != null) {
				coOccurringEntities = new ArrayList<Entity>();
				for (IVariable coOccurrenceVar : coOccurrences) {
					Attributes coOccurrenceVarAttrs = coOccurrenceVar
							.getVariableAttributes();
					EntityType coOccurringVarType = null;
					if (coOccurrenceVarAttrs != null
							&& coOccurrenceVarAttrs
									.containsAttributeType(AttributeId.NPENTITY)) {
						coOccurringVarType = EntityType.NP;
					} else {
						coOccurringVarType = EntityType.NE;
					}
					if (coOccurrenceVarAttrs != null
							&& coOccurrenceVarAttrs
									.containsAttributeType(AttributeId.FREQUENCY)
							&& coOccurrenceVarAttrs
									.getAttributes(AttributeId.FREQUENCY) != null) {
						Entity e = new Entity(coOccurringVarType,
								coOccurrenceVar.getVariableName(),
								coOccurrenceVarAttrs.getAttributes(
										AttributeId.FREQUENCY).getLong());
						coOccurringEntities.add(e);
					}
				}
			}
			Entity e = new Entity(type, entityName, frequency);
			if (coOccurringEntities != null && coOccurringEntities.size() > 0) {
				e.setCoOccurrences(coOccurringEntities);
			}
			entitySet.add(e);
		}

		// Entity Filtering
		EntityFilterRunner filterRunner = new EntityFilterRunner(
				EntityFilterType.MINLENGTH_FILTER,
				EntityFilterType.ALPHANUMERIC_FILTER,
				EntityFilterType.FREQUENCY_FILTER,
				EntityFilterType.LENGTH_FILTER,
				EntityFilterType.STOPWORDS_FILTER,
				EntityFilterType.EQUALCOOCCURRENCE_FILTER);

		entitySet = (EntitySet) filterRunner.runFilters(entitySet);
		/**
		 * @TODO clean up coOccurances : for each entity remove co-occurring
		 *       entities that did not pass the filter
		 */

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

			long id = Long.valueOf(t.getValue(DataType.RECORD_IDENTIFIER));
			FeatureVector fv = repository.getFeatureVector(id);

			// collect NP entities
			for (IVariable v : fv
					.getVariablesWithAttributeType(AttributeId.NPENTITY)) {
				Attributes attrs = v.getVariableAttributes();
				IAttribute synonymAttr = attrs
						.getAttributes(AttributeId.SYNONYM);
				if (synonymAttr != null) {
					entities.add(synonymAttr.getString().toLowerCase());
				} else {
					entities.add(v.getVariableName().toLowerCase());
				}
			}

			// collect NERs
			for (IVariable v : fv
					.getVariablesWithAttributeType(AttributeId.NER_CLASS)) {
				Attributes attrs = v.getVariableAttributes();
				IAttribute synonymAttr = attrs
						.getAttributes(AttributeId.SYNONYM);
				if (synonymAttr != null) {
					entities.add(synonymAttr.getString().toLowerCase());
				} else {
					entities.add(v.getVariableName().toLowerCase());
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
		List<Community> communities = new ArrayList<Community>();
		HashMap<String, Double> pageranks = new HashMap<String, Double>();
		List<List<Data>> communitySelectedDataChunks = new ArrayList<List<Data>>();

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

		// read community file and form communities
		Map<Integer, Community> communityMap = new LinkedHashMap<Integer, Community>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/tmp/community"));
			String line;

			while ((line = br.readLine()) != null) {
				String[] split = line.split("#");

				if (split.length != 2) {
					continue;
				}

				String id = split[0];
				String member = split[1].trim();
				double pg = 0;

				if (pageranks.containsKey(member)) {
					pg = pageranks.get(member);
				}

				Map<CommunityAttribute, Double> attrs = new HashMap<Community.CommunityAttribute, Double>();
				attrs.put(CommunityAttribute.PAGERANK, pg);

				if (!communityMap.containsKey(Integer.valueOf(id))) {
					communityMap.put(Integer.valueOf(id), new Community(id));
				}

				communityMap.get(Integer.valueOf(id)).add(member, attrs);
			}

			// write back as a list
			communities = new ArrayList<Community>(communityMap.values());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// TODO community ranking here
		ICommunityRanking commRanking = new EntityPagerankCommunityRanking(
				pageranks);
		communities = commRanking.rank(communities, this.tweets);

		// for all communities
		int clusterNum = 1;
		List<Data> selectedData = new ArrayList<Data>();
		for (Community community : communities) {
			System.out.println("processing cluster " + clusterNum++ + " of "
					+ communities.size());

			List<String> members = community.getMembers();

			// form community query
			Query query = new Query(members);

			// rank the data wrt query
			IRanking ranking = new RetweetCountPagerankSumRanking(this.tweets,
					pageranks);
			List<Data> rankedData = ranking.rank(query);

			// select
			ISelector selector = new BaselineMMRSelector(rankedData, pageranks);

			// TODO default size 10
			List<Data> data = selector.select(selectedData, 2);

			// add to community data chunks
			communitySelectedDataChunks.add(data);
			// add to selected data for global MMR
			selectedData.addAll(data);

			if (clusterNum == 11) {
				break;
			}
		}

		// form story from community data chunks
		Story s = new Story();
		double maxScore = Double.MIN_VALUE;
		if (communitySelectedDataChunks != null) {

			for (List<Data> dataChunk : communitySelectedDataChunks) {
				for (Data d : dataChunk) {
					s.addLine(d);
					double score = Double.valueOf(d.getValue(DataType.SCORE));

					if (score > maxScore) {
						maxScore = score;
						s.setTitle(d);
					}
				}
			}

			return s;
		}

		return null;
	}

	protected void exec(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (InterruptedException e) {
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
