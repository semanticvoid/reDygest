package com.entityextractionevaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Tweet;
import com.redygest.commons.preprocessor.twitter.ITweetPreprocessor;
import com.redygest.commons.util.Counter;
import com.redygest.commons.util.CounterMap;
import com.redygest.commons.util.PriorityQueue;
import com.redygest.grok.features.computation.FeaturesComputation;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.filtering.data.preextraction.PreExtractionPrefilterRunner;

/**
 * 
 * @author tejaswi contains all counts that the Filtering algo can use
 */
public class EvaluationMetrics {

	protected List<Data> tweets;
	protected ITweetPreprocessor preprocessor = null;
	protected PreExtractionPrefilterRunner prefilterRunner = null;

	public Counter<String> npCounts = new Counter<String>();
	public Counter<String> nerCounts = new Counter<String>();

	public CounterMap<String, String> entityCooccurance = new CounterMap<String, String>();
	public CounterMap<String, String> ner_ner_entityCooccurance = new CounterMap<String, String>();
	public CounterMap<String, String> np_ner_entityCooccurance = new CounterMap<String, String>();

	private FeaturesRepository repository = null;

	private static EvaluationMetrics em = null;

	public static EvaluationMetrics getInstance(String file) {
		if (em == null) {
			em = new EvaluationMetrics(file);
			return em;
		} else {
			return em;
		}
	}

	protected final void read(String file) {
		this.tweets = new ArrayList<Data>();
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
					if (prefilterRunner != null) {
						pass = prefilterRunner.runFilters(t
								.getValue(DataType.ORIGINAL_TEXT));
					}

					if (pass && t.getValue(DataType.BODY) != null) {
						this.tweets.add(t);
					}

					i++;
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private EvaluationMetrics(String file) {
		read(file);
		ConfigReader conf = ConfigReader.getInstance();
		FeaturesComputation fc = new FeaturesComputation(
				conf.getExtractorsList());
		try {
			this.repository = fc.computeFeatures(tweets);
		} catch (Exception e) {
			e.printStackTrace();
		}

		runMetrics();
	}

	private void runMetrics() {
		for (Data t : tweets) {
			int retweetCount = ((Tweet) t).getRetweetCount();
			Set<String> np_entities = new HashSet<String>();
			Set<String> ner_entities = new HashSet<String>();

			String id = t.getValue(DataType.RECORD_IDENTIFIER);
			FeatureVector fv = this.repository.getFeatureVector(id);

			// collect NP entities
			for (IVariable v : fv
					.getVariablesWithAttributeType(AttributeId.NPENTITY)) {
				Attributes attrs = v.getVariableAttributes();
				List<String> attrNames = attrs
						.getAttributeNames(AttributeId.SYNONYM);
				if (attrNames != null && attrNames.size() > 0) {
					np_entities.add(attrNames.get(0).toLowerCase());
					npCounts.incrementCount(attrNames.get(0).toLowerCase(),
							retweetCount);
				} else {
					np_entities.add(v.getVariableName().toLowerCase());
					npCounts.incrementCount(v.getVariableName().toLowerCase(),
							retweetCount);
				}
			}

			// collect NERs
			for (IVariable v : fv
					.getVariablesWithAttributeType(AttributeId.NER_CLASS)) {
				Attributes attrs = v.getVariableAttributes();
				List<String> attrNames = attrs
						.getAttributeNames(AttributeId.SYNONYM);
				if (attrNames != null && attrNames.size() > 0) {
					ner_entities.add(attrNames.get(0).toLowerCase());
					nerCounts.incrementCount(attrNames.get(0).toLowerCase(),
							retweetCount);
				} else {
					ner_entities.add(v.getVariableName().toLowerCase());
					nerCounts.incrementCount(v.getVariableName().toLowerCase(),
							retweetCount);
				}
			}

			List<String> ner_ents = new ArrayList<String>(ner_entities);
			for (int i = 0; i < ner_ents.size(); i++) {
				for (int j = i + 1; j < ner_ents.size(); j++) {
					ner_ner_entityCooccurance.incrementCount(ner_ents.get(i),
							ner_ents.get(j), retweetCount);
					ner_ner_entityCooccurance.incrementCount(ner_ents.get(j),
							ner_ents.get(i), retweetCount);
				}
			}

			List<String> np_ents = new ArrayList<String>(np_entities);
			for (int i = 0; i < np_ents.size(); i++) {
				for (int j = 0; j < ner_ents.size(); j++) {
					if (np_ents.get(i).equalsIgnoreCase(ner_ents.get(j)))
						continue;
					np_ner_entityCooccurance.incrementCount(np_ents.get(i),
							ner_ents.get(j), retweetCount);
				}
			}

		}
		mergeNEsAndNPs();

	}

	private void mergeNEsAndNPs() {
		for (String np : npCounts.keySet()) {
			if (nerCounts.keySet().contains(np)) {
				nerCounts.incrementCount(np, npCounts.getCount(np));
				// npCounts.setCount(np, 0);
				Counter<String> np_counter = np_ner_entityCooccurance
						.getCounter(np);
				Set<String> keys = np_counter.keySet();
				for (String ner : keys) {
					ner_ner_entityCooccurance.incrementCount(np, ner,
							np_counter.getCount(ner));
					// np_ner_entityCooccurance.setCount(np, ner, 0);
				}
			}
		}
	}

	private HashMap<String, Double> getTopN(Counter<String> counts, int n) {
		HashMap<String, Double> topN = new HashMap<String, Double>();
		PriorityQueue<String> pq = counts.asPriorityQueue();
		int count = 0;
		while (pq.hasNext() && count < n) {
			String key = pq.next();
			topN.put(key, counts.getCount(key));
		}
		return topN;
	}

}
