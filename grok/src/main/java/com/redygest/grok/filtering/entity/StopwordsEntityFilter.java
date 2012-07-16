package com.redygest.grok.filtering.entity;

import java.util.Arrays;
import java.util.List;

import com.redygest.commons.data.Entity;

public class StopwordsEntityFilter implements IEntityFilter {

	final static List<String> stopWords = Arrays.asList("a", "an", "and",
			"are", "as", "be", "but", "by", "for", "if", "in", "into", "is",
			"it", "no", "not", "on", "or", "such", "that", "the", "their",
			"then", "there", "these", "they", "this", "to", "was", "will",
			"with", "most", "needs", "http", "com", "rt", "news");

	// if the entity contains stop words.
	// TODO need to improve based on the position
	public boolean pass(Entity e) {
		String text = e.getValue();
		if (text == null) {
			return false;
		}
		String[] entityTokens = text.split("[\\s+.]");
		for (String token : entityTokens) {
			if (stopWords.contains(token.toLowerCase())) {
				return false;
			}
		}
		return true;
	}
}
