package com.redygest.grok.features.extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.nlp.POSTagger;
import com.redygest.commons.nlp.TaggedToken;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

public class POSFeatureExtractor extends AbstractFeatureExtractor {

	@Override
	public FeatureVectorCollection extract(List<Data> dataList,
			IFeaturesRepository repository) {
		FeatureVectorCollection features = new FeatureVectorCollection();

		for (Data d : dataList) {
			long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
			FeatureVector fVector = extract(d, repository);

			Map<Long, FeatureVector> map = new HashMap<Long, FeatureVector>();
			map.put(id, fVector);
			features.addFeatures(map);
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
				Variable var = fVector
						.getVariable(new DataVariable(bigram, id));
				if (var == null) {
					var = new DataVariable(bigram, id);
					Attributes attrs = var.getVariableAttributes();
					attrs.put(AttributeType.POSBIGRAMCOUNT, "1");
				} else {
					Attributes attrs = var.getVariableAttributes();
					int count = Integer.valueOf(attrs.getAttributeNames(
							AttributeType.POSBIGRAMCOUNT).get(0));
					count += 1;
					// attrs.remove(String.valueOf(count-1));
					attrs.remove(AttributeType.POSBIGRAMCOUNT);
					attrs.put(AttributeType.POSBIGRAMCOUNT,
							String.valueOf(count));
				}

				fVector.addVariable(var);
			}

			// unigram
			Variable var = fVector.getVariable(new DataVariable(tag, id));
			if (var == null) {
				var = new DataVariable(tag, id);
				Attributes attrs = var.getVariableAttributes();
				attrs.put(AttributeType.POSUNIGRAMCOUNT, "1");
			} else {
				Attributes attrs = var.getVariableAttributes();
				if(attrs.getAttributeNames(
						AttributeType.POSUNIGRAMCOUNT)==null || attrs.getAttributeNames(
								AttributeType.POSUNIGRAMCOUNT).size()==0){
					attrs.put(AttributeType.POSUNIGRAMCOUNT, "1");	
				}
				int count = Integer.valueOf(attrs.getAttributeNames(
						AttributeType.POSUNIGRAMCOUNT).get(0));
				count += 1;
				// attrs.remove(String.valueOf(count-1));
				attrs.remove(AttributeType.POSUNIGRAMCOUNT);
				attrs.put(AttributeType.POSUNIGRAMCOUNT, String.valueOf(count));
			}
			fVector.addVariable(var);

			// pos
			Variable queryVar = new DataVariable(word, id);
			var = fVector.getVariable(queryVar);
			if (var == null) {
				var = queryVar;
			}
			Attributes attrs = var.getVariableAttributes();
			attrs.put(AttributeType.POS, tag);
			fVector.addVariable(var);

			prevTag = tag;
		}

		return fVector;
	}

}
