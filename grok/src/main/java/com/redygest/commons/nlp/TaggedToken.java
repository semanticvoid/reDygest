package com.redygest.commons.nlp;

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

	public void setWord(String word) {
		this.word = word;
	}
}
