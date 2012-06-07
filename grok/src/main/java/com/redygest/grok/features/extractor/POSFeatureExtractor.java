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
	public FeatureVectorCollection extract(List<Data> dataList,
			IFeaturesRepository repository) {
		FeatureVectorCollection features = new FeatureVectorCollection();

		for (Data d : dataList) {
			long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
			FeatureVector fVector = extract(d, repository);

			FeatureVectorCollection fVectCollection = new FeatureVectorCollection();
			fVectCollection.put(id, fVector);
			features.addFeatures(fVectCollection);
		}

		return features;
	}

	@Override
	public FeatureVector extract(Data d, IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		POSTagger tagger = POSTagger.getInstance();
		List<TaggedToken> tags = tagger.tag(d.getValue(DataType.BODY));
		String prevTag = null;

		for (TaggedToken token : tags) {
			String word = token.getWord();
			String tag = token.getPosTag();

			// bigram
			if (prevTag != null) {
				String bigram = prevTag + " " + tag;
				IVariable var = fVector
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

				fVector.addVariable(var);
			}

			// unigram
			IVariable var = fVector.getVariable(new DataVariable(tag, id));
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
			fVector.addVariable(var);

			// pos
			IVariable queryVar = new DataVariable(word, id);
			var = fVector.getVariable(queryVar);
			if (var == null) {
				var = queryVar;
			}
			Attributes attrs = var.getVariableAttributes();
			attrs.add(new StringAttribute(AttributeId.POS, tag));
			fVector.addVariable(var);

			prevTag = tag;
		}

		return fVector;
	}
}
