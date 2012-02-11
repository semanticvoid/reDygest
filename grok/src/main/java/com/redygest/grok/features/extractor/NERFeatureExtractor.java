package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.NamedEntity;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NERFeatureExtractor extends AbstractFeatureExtractor {

	private static StanfordCoreNLP pipeline = null;
	static {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		pipeline = new StanfordCoreNLP(props);
	}

	List<NamedEntity> getEntities(String data) {
		List<NamedEntity> entities = new ArrayList<NamedEntity>();

		Annotation document = new Annotation(data);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String ne = token.get(NamedEntityTagAnnotation.class);
				entities.add(new NamedEntity(word, ne));
			}
		}

		return entities;
	}

	public NamedEntity correctBadEntries(String text) {
		List<String> labels = new ArrayList<String>();
		labels.add("PERSON");
		labels.add("LOCATION");
		labels.add("ORGANIZATION");
		NamedEntity ne = null;

		for (String label : labels) {
			if (text.contains(label)) {
				String[] split = text.split("/" + label);
				ne = new NamedEntity(split[0], label);
			}
		}
		return ne;
	}

	@Override
	protected FeatureVector extract(Data d, IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		List<NamedEntity> nes = getEntities(d.getValue(DataType.BODY));
		for (NamedEntity ne : nes) {
			Variable var = fVector.getVariable(new DataVariable(ne.getText(),
					id));
			if (var == null) {
				var = new DataVariable(ne.getText(), id);
			}
			var.addAttribute(ne.getEntityClass(), AttributeType.NER_CLASS);
			fVector.addVariable(var);
		}

		return fVector;
	}

}
