package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.NERTagger;
import com.redygest.commons.nlp.TaggedToken;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class NERFeatureExtractor extends AbstractFeatureExtractor {

//	public NamedEntity correctBadEntries(String text) {
//		List<String> labels = new ArrayList<String>();
//		labels.add("PERSON");
//		labels.add("LOCATION");
//		labels.add("ORGANIZATION");
//		NamedEntity ne = null;
//
//		for (String label : labels) {
//			if (text.contains(label)) {
//				String[] split = text.split("/" + label);
//				ne = new NamedEntity(split[0], label);
//			}
//		}
//		return ne;
//	}

	@Override
	protected FeatureVector extract(Data d, IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		NERTagger tagger = NERTagger.getInstance();
		List<TaggedToken> tokens = tagger.tag(d.getValue(DataType.BODY));
		for (TaggedToken token : tokens) {
			Variable var = fVector.getVariable(new DataVariable(token.getWord(),
					id));
			if (var == null) {
				var = new DataVariable(token.getWord(), id);
			}
			var.addAttribute(token.getNer(), AttributeType.NER_CLASS);
			fVector.addVariable(var);
		}

		return fVector;
	}

}
