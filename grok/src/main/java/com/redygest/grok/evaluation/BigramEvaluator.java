package com.redygest.grok.evaluation;

import java.util.HashSet;
import java.util.List;

import com.redygest.grok.evaluation.Data.Record;

public class BigramEvaluator implements IEvaluator {

	public EvalMetrics evaluate(List<Record> goldset_records,
			List<Record> generated_records) {
		EvalMetrics em = new EvalMetrics();
		HashSet<String> goldset_entities = new HashSet<String>();
		HashSet<String> generated_entities = new HashSet<String>();

		for (Record r1 : goldset_records) {
			List<String> pairs = Utils.getPairwaiseOccurences(r1.entities);
			goldset_entities.addAll(pairs);
		}

		for (Record r2 : generated_records) {
			List<String> pairs = Utils.getPairwaiseOccurences(r2.entities);
			generated_entities.addAll(pairs);
		}

		for (String ent : generated_entities) {
			if (goldset_entities.contains(ent)) {
				em.incrTP();
			} else {
				em.incrFP();
			}
		}

		for (String ent : goldset_entities) {
			if (!generated_entities.contains(ent)) {
				em.incrFN();
			}
		}
		return em;
	}

}
