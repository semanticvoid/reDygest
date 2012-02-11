package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class POSFeatureExtractor extends AbstractFeatureExtractor {

	private static StanfordCoreNLP pipeline = null;
	static {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);
	}

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

	private List<String> getPOSTags(Annotation document) {
		List<String> tags = new ArrayList<String>();
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				tags.add(word + "\005" + pos);
			}
		}

		return tags;
	}

	@Override
	public FeatureVector extract(Data d, IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		Annotation document = new Annotation(d.getValue(DataType.BODY));
		pipeline.annotate(document);
		List<String> tags = getPOSTags(document);
		String prevTag = null;
		for (String tag : tags) {
			String[] tokens = tag.split("\005");

			// bigram
			if (prevTag != null) {
				String bigram = prevTag + " " + tokens[1];
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
			Variable var = fVector.getVariable(new DataVariable(tokens[1], id));
			if (var == null) {
				var = new DataVariable(tokens[1], id);
				Attributes attrs = var.getVariableAttributes();
				attrs.put(AttributeType.POSUNIGRAMCOUNT, "1");
			} else {
				Attributes attrs = var.getVariableAttributes();
				int count = Integer.valueOf(attrs.getAttributeNames(
						AttributeType.POSUNIGRAMCOUNT).get(0));
				count += 1;
				// attrs.remove(String.valueOf(count-1));
				attrs.remove(AttributeType.POSUNIGRAMCOUNT);
				attrs.put(AttributeType.POSUNIGRAMCOUNT, String.valueOf(count));
			}
			fVector.addVariable(var);

			// pos
			Variable queryVar = new DataVariable(tokens[0], id);
			var = fVector.getVariable(queryVar);
			if (var == null) {
				var = queryVar;
			}
			Attributes attrs = var.getVariableAttributes();
			attrs.put(AttributeType.POS, tokens[1]);
			fVector.addVariable(var);

			prevTag = tokens[1];
		}

		return fVector;
	}

}
