package graphbuilder.pajek;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Entity;
import com.redygest.commons.data.EntitySet;
import com.redygest.commons.data.Tweet;
import com.redygest.commons.data.Entity.EntityType;
import com.redygest.commons.preprocessor.twitter.ITweetPreprocessor;
import com.redygest.commons.preprocessor.twitter.PreprocessorZ20120215;
import com.redygest.grok.features.computation.FeaturesComputation;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
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

public class Pajek {
	protected List<Data> tweets;
	protected ITweetPreprocessor preprocessor = null;
	protected PreExtractionPrefilterRunner preExtractionFilterRunner = null;
	protected PostExtractionPrefilterRunner postExtractionfilterRunner = null;

	public Pajek() {
		// default twitter preprocessor
		this.preprocessor = new PreprocessorZ20120215();
		// pre-extraction filter setup
		this.preExtractionFilterRunner = new PreExtractionPrefilterRunner(
				PreExtractionPrefilterType.REPLY_TWEET_FILTER);
		// post-extraction filter setup
		this.postExtractionfilterRunner = new PostExtractionPrefilterRunner(
				PostExtractionPrefilterType.FACT_OPINION_FILTER);
	}

	protected void addTweet(Data t) {
		tweets.add(t);
	}

	/**
	 * Extract features
	 */
	protected final boolean extractFeatures(List<Data> tweets) {
		ConfigReader config = ConfigReader.getInstance();
		FeaturesComputation fc = new FeaturesComputation(config
				.getExtractorsList());
		try {
			fc.computeFeatures(tweets);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected final List<Data> read(String file) {
		try {
			BufferedReader rdr = new BufferedReader(new FileReader(new File(
					file)));
			String line;
			long i = 0;
			while ((line = rdr.readLine()) != null) {
				try {
					boolean pass = true;
					Tweet t = new Tweet(line, String.valueOf(i), preprocessor);
					// prefilter code
					if (preExtractionFilterRunner != null) {
						pass = preExtractionFilterRunner.runFilters(t
								.getValue(DataType.ORIGINAL_TEXT));
					}

					if (pass && t.getValue(DataType.BODY) != null) {
						addTweet(t);
					}

					i++;
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tweets;
	}

	protected final boolean postFilter(List<Data> tweets) {
		List<Data> filteredData = new ArrayList<Data>();
		PostExtractionPrefilterRunner filterRunner = new PostExtractionPrefilterRunner(
				PostExtractionPrefilterType.FACT_OPINION_FILTER);
		for (Data d : tweets) {
			if (filterRunner.runFilters(d)) {
				filteredData.add(d);
			}
		}
		this.tweets = filteredData;
		return true;
	}

	protected EntitySet filterEntities() {
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
				EntityFilterType.ALPHANUMERIC_FILTER,
				EntityFilterType.FREQUENCY_FILTER,
				EntityFilterType.LENGTH_FILTER,
				EntityFilterType.STOPWORDS_FILTER,
				EntityFilterType.EQUALCOOCCURRENCE_FILTER);

		entitySet = (EntitySet) filterRunner.runFilters(entitySet);
		entitySet = cleanCoOccurrances(entitySet);
		return entitySet;
	}

	public EntitySet cleanCoOccurrances(EntitySet entitySet) {
		List<String> entities = new ArrayList<String>();
		for (Entity e : entitySet) {
			entities.add(e.getValue());
		}
		for (Entity e : entitySet) {
			List<Entity> coOccurringEntities = e.getCoOccurrences();
			List<Entity> copyOfCoOccurringEntities = new ArrayList<Entity>(
					coOccurringEntities);
			for (Entity eCoOccur : copyOfCoOccurringEntities) {
				if (!entities.contains(eCoOccur.getValue())) {
					coOccurringEntities.remove(eCoOccur);
				}
			}
		}
		return entitySet;
	}

	public String buildGraph() {
		HashSet<String> edges = new HashSet<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		int entityIDCounter = 0;

		StringBuffer sb = new StringBuffer();
		EntitySet es = filterEntities();
		for (Entity entity : es) {
			if (entity.getValue().equalsIgnoreCase("")) {
				continue;
			}
			String entityValue = entity.getValue();
			if (!map.containsKey(entityValue)) {
				entityIDCounter++;
				map.put(entityValue, Integer.toString(entityIDCounter));
			}
			String entityID = map.get(entityValue);
			List<Entity> coOccurances = entity.getCoOccurrences();
			for (Entity coOccuringEntity : coOccurances) {
				if (coOccuringEntity.getValue().equalsIgnoreCase("")) {
					continue;
				}
				String coOccurringEntityValue = coOccuringEntity.getValue();
				if (!map.containsKey(coOccurringEntityValue)) {
					entityIDCounter++;
					map.put(coOccurringEntityValue, Integer
							.toString(entityIDCounter));
				}
				String coOccuringentityID = map.get(coOccurringEntityValue);
				edges.add(entityID + " " + coOccuringentityID);
			}
		}
		sb.append("*Vertices ");
		sb.append(map.keySet().size());
		sb.append("\n");
		for (String key : map.keySet()) {
			sb.append(map.get(key));
			sb.append(" ");
			String[] split = key.split("\\s+");
			StringBuffer sb1 = new StringBuffer();
			for (String tok : split) {
				sb1.append(tok);
				sb1.append("_");
			}
			sb.append(sb1.toString());
			sb.append("\n");
		}
		sb.append("*Edges\n");
		for (String key : edges) {
			sb.append(key);
			sb.append("\n");
		}
		return sb.toString();
	}

	public void writeGraph(String graph, String file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(graph);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final void run(String tweetsFile, String graphFile) {
		tweets = new ArrayList<Data>();
		tweets = read(tweetsFile);
		if (tweets != null && extractFeatures(tweets) && postFilter(tweets)) {
			String flatGraph = buildGraph();
			writeGraph(flatGraph, graphFile);
		}
	}

	/***
	 * 
	 * @param args
	 *            arg1: tweets file arg2: output file to write the graph. the
	 *            file is a csv, the first column in each line is the entity
	 *            followed by its co-occurring entities
	 */

	public static void main(String[] args) {
		Pajek b = new Pajek();
		b
				.run(
						"/Users/tejaswi/Documents/workspace/reDygest/datasets/dedup/clinton_1_10",
						"/Users/tejaswi/Documents/workspace/reDygest/datasets/junggraphfiles/clinton_1_10.graph");
	}

}
