package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.NERTagger;
import com.redygest.commons.nlp.TaggedToken;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.Variable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class NERFeatureExtractor extends AbstractFeatureExtractor {

	// public NamedEntity correctBadEntries(String text) {
	// List<String> labels = new ArrayList<String>();
	// labels.add("PERSON");
	// labels.add("LOCATION");
	// labels.add("ORGANIZATION");
	// NamedEntity ne = null;
	//
	// for (String label : labels) {
	// if (text.contains(label)) {
	// String[] split = text.split("/" + label);
	// ne = new NamedEntity(split[0], label);
	// }
	// }
	// return ne;
	// }

	@Override
	protected FeatureVector extract(Data d, IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		NERTagger tagger = NERTagger.getInstance();
		List<TaggedToken> tokens = tagger.tag(d.getValue(DataType.BODY));

		StringBuffer entity = new StringBuffer();
		String prevNerClass = null;

		for (TaggedToken token : tokens) {
			String nerClass = token.getNer();
			String word = token.getWord();

			if (prevNerClass != null && !prevNerClass.equals(nerClass)) {
				Variable var = fVector.getVariable(new DataVariable(entity
						.toString().trim(), id));

				if (var == null) {
					var = new DataVariable(entity.toString().trim(), id);
				}

				var.addAttribute(prevNerClass, AttributeId.NER_CLASS);
				fVector.addVariable(var);

				entity = new StringBuffer();
			}

			if (nerClass.equalsIgnoreCase("PERSON")
					|| nerClass.equalsIgnoreCase("ORGANIZATION")
					|| nerClass.equalsIgnoreCase("LOCATION")) {
				entity.append(word + " ");
				prevNerClass = nerClass;
			} else {
				prevNerClass = "";
			}
		}

		if (prevNerClass.equalsIgnoreCase("PERSON")
				|| prevNerClass.equalsIgnoreCase("ORGANIZATION")
				|| prevNerClass.equalsIgnoreCase("LOCATION")) {
			Variable var = fVector.getVariable(new DataVariable(entity
					.toString().trim(), id));
			if (var == null) {
				var = new DataVariable(entity.toString().trim(), id);
			}
			var.addAttribute(prevNerClass, AttributeId.NER_CLASS);
			var.addAttribute("true", AttributeId.NERENTITY);
			fVector.addVariable(var);
		}

		return fVector;
	}
}
