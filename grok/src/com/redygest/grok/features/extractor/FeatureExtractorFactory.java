package com.redygest.grok.features.extractor;

public class FeatureExtractorFactory {

	private static FeatureExtractorFactory instance = null;

	private FeatureExtractorFactory() {
	}

	public static synchronized FeatureExtractorFactory getInstance() {
		if (instance == null) {
			instance = new FeatureExtractorFactory();
		}

		return instance;
	}

	public IFeatureExtractor getFeatureExtractor(FeatureExtractorType type) {
		switch (type) {
			case NGRAMSYNONYM:
				return new NGramSynonymExtractor();
			case NPCOOCCURRENCE:
				return new NPCooccurrenceExtractor();
			case POSFEATURE:
				return new POSFeatureExtractor();
			case SENTIMENTFEATURE:
				return new SentimentFeatureExtractor();
			case PUNCTUATIONCOUNTFEATURE:
				return new PunctuationCountFeatureExtractor();
			case PPRONOUNCOUNTFEATURE:
				return new PPronounCountFeatureExtractor();
			case SRL:
				return new SRLFeatureExtractor();
			case NER:
				return new NERFeatureExtractor();
			case DBSYNONYM:
				return new SynonymFeatureExtractor();
		}
		
		return null;
	}

}
