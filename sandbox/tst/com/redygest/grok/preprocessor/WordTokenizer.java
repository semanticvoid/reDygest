package com.redygest.grok.preprocessor;

import java.util.Arrays;
import java.util.List;

public class WordTokenizer {
	public static List<String> tokenize(String text) {
		return Arrays.asList(text.split("[ ,\\t\\n\\r\\f\\.;:\"\'-]+"));
	}
}
