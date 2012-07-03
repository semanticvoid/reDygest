package com.redygest.grok.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.redygest.grok.evaluation.Data.Record;
import com.redygest.grok.evaluation.EvaluatorFactory.EvaluationType;

public class EvaluationRunner {

	public EvalMetrics averageMetrics(List<EvalMetrics> evalMetrics) {
		EvalMetrics avgEm = new EvalMetrics();
		for (EvalMetrics em : evalMetrics) {
			avgEm.incrFP(em.getFp());
			avgEm.incrFN(em.getFn());
			avgEm.incrTP(em.getTp());
			avgEm.incrTN(em.getTn());
		}
		int num_files = evalMetrics.size();
		avgEm.fn = avgEm.getFn() / num_files;
		avgEm.fp = avgEm.getFp() / num_files;
		avgEm.tn = avgEm.getTn() / num_files;
		avgEm.tp = avgEm.getTp() / num_files;
		return avgEm;

	}

	public EvalMetrics run(String evalType, String generatedSummary,
			String... goldSummaries) {
		IEvaluator evaluator = null;
		if (evalType.equalsIgnoreCase("bigram")) {
			evaluator = EvaluatorFactory
					.produceEvaluator(EvaluationType.BIGRAM);
		} else {
			evaluator = EvaluatorFactory
					.produceEvaluator(EvaluationType.UNIGRAM);
		}
		Data data = new Data();
		List<Record> generatedRecords = null;
		try {
			generatedRecords = data.readDataSet(generatedSummary);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (generatedRecords == null) {
			System.err.println("Check the generated summary");
			System.exit(0);
		}

		List<EvalMetrics> ems = new ArrayList<EvalMetrics>();
		for (int i = 0; i < goldSummaries.length; i++) {
			List<Record> goldRecords = null;
			try {
				goldRecords = data.readDataSet(goldSummaries[i]);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (goldRecords == null) {
				System.err.println("Check the gold summary: "
						+ goldSummaries[i]);
				continue;
			}
			EvalMetrics em = evaluator.evaluate(goldRecords, generatedRecords);
			ems.add(em);
		}
		EvalMetrics avgMetrics = averageMetrics(ems);
		return avgMetrics;
	}

	public void printUsage() {
		System.out
				.println("usage: java -jar ... <evaluation type> <generated summary file> <list of gold summary files>");
	}

	public static void main(String[] args) {
		EvaluationRunner e = new EvaluationRunner();
		if (args.length < 3) {
			e.printUsage();
		}
		String evalType = args[0];
		String generatedSummary = args[1];
		List<String> goldSummaries = new ArrayList<String>();
		for (int i = 2; i < args.length; i++) {
			goldSummaries.add(args[i]);
		}

		EvalMetrics avg_metrics = e.run(evalType, generatedSummary,
				goldSummaries.toArray(new String[goldSummaries.size()]));
		System.out.println("Average F1 : " + avg_metrics.calculateF1Measure());
		System.out.println("Average Precision: "
				+ avg_metrics.calculatePrecision());
		System.out.println("Average Recall: " + avg_metrics.calculateRecall());
	}

}
