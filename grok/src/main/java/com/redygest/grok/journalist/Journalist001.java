/**
 * 
 */
package com.redygest.grok.journalist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Story;
import com.redygest.commons.preprocessor.twitter.PreprocessorZ20120215;
import com.redygest.grok.features.computation.FeaturesComputation;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.FeaturesRepository;
import com.redygest.grok.prefilter.PrefilterRunner;
import com.redygest.grok.prefilter.PrefilterType;

/**
 * Journalist 001 (Naming convention 'Journalist 0.0.1')
 * 
 * This is the first version of the production journalist for alpha release
 * 
 */
public class Journalist001 extends BaseJournalist {

	/**
	 * Constructor
	 */
	public Journalist001() {
		super();
		// default twitter preprocessor
		this.preprocessor = new PreprocessorZ20120215();
		// prefilter setup
		this.prefilterRunner = new PrefilterRunner(
				PrefilterType.NONENLGISH_LANG_FILTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	protected Story process(List<Data> tweets) {
		ConfigReader config = ConfigReader.getInstance();
		FeaturesComputation fc = new FeaturesComputation(
				config.getExtractorsList());
		try {
			FeaturesRepository repository = fc.computeFeatures(tweets);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected boolean write(Story s) {
		FeaturesRepository repository = FeaturesRepository.getInstance();

		for (Data t : tweets) {
			Set<String> entities = new HashSet<String>();

			String id = t.getValue(DataType.RECORD_IDENTIFIER);
			FeatureVector fv = repository.getFeatureVector(id);

			// collect NP entities
			for (Variable v : fv
					.getVariablesWithAttributeType(AttributeType.NPENTITY)) {
				entities.add(v.getVariableName());
			}

			// collect NERs
			for (Variable v : fv
					.getVariablesWithAttributeType(AttributeType.NER_CLASS)) {
				entities.add(v.getVariableName());
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

			System.out.println(jObj.toString());
		}

		return true;
	}

	public static void main(String[] args) {
		Journalist001 j = new Journalist001();
		j.run("/Users/akishore/projects/redygest/reDygest/datasets/t");
	}

}
