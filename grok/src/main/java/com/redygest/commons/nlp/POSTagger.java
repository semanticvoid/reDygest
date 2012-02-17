package com.redygest.commons.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class POSTagger {

	private static StanfordCoreNLP pipeline = null;
	private static POSTagger tagg = null;
	
	static {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);
	}
	
	private POSTagger() {
	}
	
	public synchronized static POSTagger getInstance() {
		if(tagg == null) {
			tagg = new POSTagger();
		}
		
		return tagg;
	}
	
	public synchronized List<TaggedToken> tag(String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<TaggedToken> tags = new ArrayList<TaggedToken>();
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String ner = token.get(NamedEntityTagAnnotation.class);
				tags.add(new TaggedToken(word, pos, ner));
			}
		}
		
		return tags;
	}
}
