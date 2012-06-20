package com.redygest.grok.features.extractor;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.POSTagger;
import com.redygest.commons.nlp.TaggedToken;
import com.redygest.grok.features.data.attribute.AttributeId;
import com.redygest.grok.features.data.attribute.Attributes;
import com.redygest.grok.features.data.attribute.LongAttribute;
import com.redygest.grok.features.data.attribute.StringAttribute;
import com.redygest.grok.features.data.variable.DataVariable;
import com.redygest.grok.features.data.variable.IVariable;
import com.redygest.grok.features.data.vector.FeatureVector;
import com.redygest.grok.features.data.vector.FeatureVectorCollection;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class POSFeatureExtractor extends AbstractFeatureExtractor {

	@Override
	public FeatureVectorCollection extract(Data d,
			IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVectorCollection fCollection = new FeatureVectorCollection();
		FeatureVector fLocal = new FeatureVector();
		POSTagger tagger = POSTagger.getInstance();
		List<TaggedToken> tags = tagger.tag(d.getValue(DataType.BODY));
		String prevTag = null;

		for (TaggedToken token : tags) {
			String word = token.getWord();
			String tag = token.getPosTag();

			// bigram
			if (prevTag != null) {
				String bigram = prevTag + " " + tag;
				IVariable var = fLocal
						.getVariable(new DataVariable(bigram, id));

				if (var == null) {
					var = new DataVariable(bigram, id);
					Attributes attrs = var.getVariableAttributes();
					attrs.add(new LongAttribute(AttributeId.POSBIGRAMCOUNT, 1L));
				} else {
					Attributes attrs = var.getVariableAttributes();
					long count = attrs
							.getAttributes(AttributeId.POSBIGRAMCOUNT)
							.getLong();
					count += 1;
					attrs.remove(AttributeId.POSBIGRAMCOUNT);
					attrs.add(new LongAttribute(AttributeId.POSBIGRAMCOUNT,
							count));
				}

				fLocal.addVariable(var);
			}

			// unigram
			IVariable var = fLocal.getVariable(new DataVariable(tag, id));
			if (var == null) {
				var = new DataVariable(tag, id);
				Attributes attrs = var.getVariableAttributes();
				attrs.add(new LongAttribute(AttributeId.POSUNIGRAMCOUNT, 1L));
			} else {
				Attributes attrs = var.getVariableAttributes();
				long count = attrs.getAttributes(AttributeId.POSUNIGRAMCOUNT)
						.getLong();
				count += 1;
				attrs.remove(AttributeId.POSUNIGRAMCOUNT);
				attrs.add(new LongAttribute(AttributeId.POSUNIGRAMCOUNT, count));
			}
			fLocal.addVariable(var);

			// pos
			IVariable queryVar = new DataVariable(word, id);
			var = fLocal.getVariable(queryVar);
			if (var == null) {
				var = queryVar;
			}
			Attributes attrs = var.getVariableAttributes();
			attrs.add(new StringAttribute(AttributeId.POS, tag));
			fLocal.addVariable(var);

			prevTag = tag;
		}

		// add feature vector to collection to be returned
		fCollection.put(id, fLocal);

		return fCollection;
	}
}
