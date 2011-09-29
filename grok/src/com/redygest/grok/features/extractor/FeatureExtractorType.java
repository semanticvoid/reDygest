package com.redygest.grok.features.extractor;

public enum FeatureExtractorType {
	NGRAMSYNONYM,
	NPCOOCCURRENCE,
	POSFEATURE,
	SENTIMENTFEATURE,
	PUNCTUATIONCOUNTFEATURE,
	PPRONOUNCOUNTFEATURE,
	SRL;
	
	/**
	 * Get {@link FeatureExtractorType} from string
	 * @param str
	 * @return	{@link FeatureExtractorType}
	 */
	public static FeatureExtractorType getType(String str) {
		for(FeatureExtractorType t : FeatureExtractorType.values()) {
			if(t.toString().equalsIgnoreCase(str)) {
				return t;
			}
		}
		
		return null;
	}
}
