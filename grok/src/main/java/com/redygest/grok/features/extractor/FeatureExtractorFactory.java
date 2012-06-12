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
		case NPENTITY:
			return new NPEntityExtractor();
		case POSFEATURE:
			return new POSFeatureExtractor();
		case SENTIMENTFEATURE:
			return new SentimentFeatureExtractor();
		case PUNCTUATIONCOUNTFEATURE:
			return new PunctuationCountFeatureExtractor();
		case PPRONOUNCOUNTFEATURE:
			return new PPronounCountFeatureExtractor();
		case NER:
			return new NERFeatureExtractor();
		case SYNONYM:
			return new SynonymFeatureExtractor();
		case ENTITY:
			return new EntityFeatureExtractor();
		}

		return null;
	}

}
