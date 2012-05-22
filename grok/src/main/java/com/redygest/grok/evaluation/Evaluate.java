package com.redygest.grok.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.redygest.grok.evaluation.Data.Record;
import com.redygest.grok.evaluation.EvaluatorFactory.EvaluationType;

public class Evaluate {

	public EvalMetrics averageMetrics(List<EvalMetrics> evalMetrics) {
		EvalMetrics avg_em = new EvalMetrics();
		for (EvalMetrics em : evalMetrics) {
			avg_em.incrFP(em.getFp());
			avg_em.incrFN(em.getFn());
			avg_em.incrTP(em.getTp());
			avg_em.incrTN(em.getTn());
		}
		int num_files = evalMetrics.size();
		avg_em.fn = avg_em.getFn() / num_files;
		avg_em.fp = avg_em.getFp() / num_files;
		avg_em.tn = avg_em.getTn() / num_files;
		avg_em.tp = avg_em.getTp() / num_files;
		return avg_em;

	}

	public static void main(String[] args) {
		Evaluate e = new Evaluate();

		String evalType = args[0];
		IEvaluator evaluator = null;
		if (evalType.equalsIgnoreCase("bigram")) {
			evaluator = EvaluatorFactory
					.produceEvaluator(EvaluationType.BIGRAM);
		} else {
			evaluator = EvaluatorFactory
					.produceEvaluator(EvaluationType.UNIGRAM);
		}

		Data data = new Data();
		String generatedSummary = args[1];
		List<Record> generated_records = null;
		try {
			generated_records = data.readDataSet(generatedSummary);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (generated_records == null) {
			System.err.println("Check the generated summary");
			System.exit(0);
		}

		List<EvalMetrics> ems = new ArrayList<EvalMetrics>();
		for (int i = 2; i < args.length; i++) {
			List<Record> gold_records = null;
			try {
				gold_records = data.readDataSet(args[i]);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (gold_records == null) {
				System.err.println("Check the gold summary: " + args[i]);
				continue;
			}
			EvalMetrics em = evaluator
					.evaluate(gold_records, generated_records);
			ems.add(em);
		}
		EvalMetrics avg_metrics = e.averageMetrics(ems);
		System.out.println("Average F1 : " + avg_metrics.calculateF1Measure());
		System.out.println("Average Precision: "
				+ avg_metrics.calculatePrecision());
		System.out.println("Average Recall: " + avg_metrics.calculateRecall());
	}

}
