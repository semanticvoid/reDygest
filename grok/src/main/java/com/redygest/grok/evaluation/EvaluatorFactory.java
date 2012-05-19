package com.redygest.grok.evaluation;

public class EvaluatorFactory {

	public enum EvaluationType {
		UNIGRAM, BIGRAM;
	}

	public static IEvaluator produceEvaluator(EvaluationType et) {
		if (et == EvaluationType.UNIGRAM) {
			return new UnigramEvaluator();
		}
		if (et == EvaluationType.BIGRAM) {
			return new BigramEvaluator();
		} else {
			return new UnigramEvaluator();
		}
	}
}
