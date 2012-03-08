/**
 * 
 */
package com.redygest.grok.journalist;

import java.util.List;

import com.redygest.commons.config.ConfigReader;
import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
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
			for (Data t : tweets) {
				String id = t.getValue(DataType.RECORD_IDENTIFIER);
				FeatureVector fv = repository.getFeatureVector(id);
				for (Variable v : fv
						.getVariablesWithAttributeType(AttributeType.NER_CLASS)) {
					System.out.println(v.getVariableName());
					Attributes attrs = v.getVariableAttributes();
					if (attrs != null) {
						List<String> names = attrs
								.getAttributeNames(AttributeType.NER_CLASS);
						for (String s : names) {
							System.out.println("\tattr: " + s);
						}
					}
				}
			}
			// for (long id : repository.getIdentifiers()) {
			// FeatureVector fv = repository.getFeatureVector(String
			// .valueOf(id));
			// Collection<Variable> variables = fv.getVariables();
			// for (Variable v : variables) {
			// System.out.println(v.getVariableName());
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		Journalist001 j = new Journalist001();
		j.run("/Users/akishore/projects/redygest/reDygest/datasets/t");
	}

}
