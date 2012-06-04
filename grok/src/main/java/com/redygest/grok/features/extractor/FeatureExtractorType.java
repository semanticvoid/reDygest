package com.redygest.grok.features.extractor;

public enum FeatureExtractorType {
	NGRAMSYNONYM,
	NPCOOCCURRENCE,
	NPENTITY,
	POSFEATURE,
	SENTIMENTFEATURE,
	PUNCTUATIONCOUNTFEATURE,
	PPRONOUNCOUNTFEATURE,
	SRL,
	NER,
	SYNONYM,
	ENTITY;
	
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
