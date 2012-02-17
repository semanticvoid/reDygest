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

public class Tokenizer {

	private static StanfordCoreNLP pipeline = null;
	private static Tokenizer tagg = null;
	
	static {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");
		pipeline = new StanfordCoreNLP(props);
	}
	
	private Tokenizer() {
	}
	
	public synchronized static Tokenizer getInstance() {
		if(tagg == null) {
			tagg = new Tokenizer();
		}
		
		return tagg;
	}
	
	public synchronized List<TaggedToken> tokenize(String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<TaggedToken> tags = new ArrayList<TaggedToken>();
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				tags.add(new TaggedToken(word, null, null));
			}
		}
		
		return tags;
	}
}
