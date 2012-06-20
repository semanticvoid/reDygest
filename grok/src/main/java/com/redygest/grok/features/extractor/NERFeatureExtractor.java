package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.NERTagger;
import com.redygest.commons.nlp.TaggedToken;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.BooleanAttribute;
import com.redygest.grok.features.data.attribute.StringAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class NERFeatureExtractor extends AbstractFeatureExtractor {

	@Override
	protected FeatureVectorCollection extract(Data d,
			IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVectorCollection fCollection = new FeatureVectorCollection();
		FeatureVector fLocal = new FeatureVector();
		NERTagger tagger = NERTagger.getInstance();
		List<TaggedToken> tokens = tagger.tag(d.getValue(DataType.BODY));

		StringBuffer entity = new StringBuffer();
		String prevNerClass = null;

		for (TaggedToken token : tokens) {
			String nerClass = token.getNer();
			String word = token.getWord();

			if (prevNerClass != null && !prevNerClass.equals(nerClass)) {
				IVariable var = fLocal.getVariable(new DataVariable(entity
						.toString().trim(), id));

				if (var == null) {
					var = new DataVariable(entity.toString().trim(), id);
				}

				var.addAttribute(new StringAttribute(AttributeId.NER_CLASS,
						prevNerClass));
				var.addAttribute(new BooleanAttribute(AttributeId.NERENTITY,
						true));

				fLocal.addVariable(var);

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
			IVariable var = fLocal.getVariable(new DataVariable(entity
					.toString().trim(), id));

			if (var == null) {
				var = new DataVariable(entity.toString().trim(), id);
			}

			var.addAttribute(new StringAttribute(AttributeId.NER_CLASS,
					prevNerClass));
			var.addAttribute(new BooleanAttribute(AttributeId.NERENTITY, true));

			fLocal.addVariable(var);
		}

		// add feature vector to collection to be returned
		fCollection.put(id, fLocal);

		return fCollection;
	}
}
