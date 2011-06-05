package com.redygest.grok.features.extractor;

public class FeatureExtractorFactory {
	public IFeatureExtractor getFeatureExtractor(FeatureExtractorType type) throws Exception  {
		switch (type) {
		case NGRAMSYNONYM :
			return new NGramSynonymExtractor();
		case NPCOOCCURRENCE :
			return new NPCooccurrenceExtractor();
		case POSFEATURE :
			return new POSFeatureExtractor();
		case SENTIMENTFEATURE :
			return new SentimentFeatureExtractor();
		}
		return null;
	}
}
