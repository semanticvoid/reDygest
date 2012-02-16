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

public class Tagger {
	
	public class TaggedToken {
		private String word;
		private String posTag;
		private String ner;
		
		public TaggedToken(String word, String posTag, String ner) {
			this.word = word;
			this.posTag = posTag;
			this.ner = ner;
		}

		public String getWord() {
			return word;
		}

		public String getPosTag() {
			return posTag;
		}

		public String getNer() {
			return ner;
		}
	}

	private static StanfordCoreNLP pipeline = null;
	private static Tagger tagg = null;
	
	static {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		pipeline = new StanfordCoreNLP(props);
	}
	
	private Tagger() {
	}
	
	public synchronized static Tagger getInstance() {
		if(tagg == null) {
			tagg = new Tagger();
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
