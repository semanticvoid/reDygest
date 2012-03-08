package com.redygest.redundancy.extractor;

public class RedundancyExtractorFactory {

	public enum Extractor {
		EXACTDUP, NEARDUP, SEMANTICDUP;
	}

	public static IRedundancyExtractor produceExtractor(Extractor e) {
		if (e == Extractor.EXACTDUP) {
			return new ExactDupExtractor();
		} else if (e == Extractor.NEARDUP) {
			return new ExactDupExtractor();
		} else if (e == Extractor.SEMANTICDUP) {
			return new SemanticDupExtractor();
		} else {
			// return default
			return new ExactDupExtractor();
		}
	}
}
