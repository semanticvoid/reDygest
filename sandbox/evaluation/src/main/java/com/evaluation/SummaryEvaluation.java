package com.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.evaluation.Data.Record;
import com.evaluation.util.Counter;
import com.evaluation.util.Counters;

public class SummaryEvaluation {
	List<Record> records = new ArrayList<Record>();

	public List<String> getAllCombinations(List<String> ents) {
		Collections.sort(ents);
		List<String> combs = new ArrayList<String>();
		// generate 2-itemsets
		for (int i = 0; i < ents.size(); i++) {
			combs.add(ents.get(i));
			for (int j = i + 1; j < ents.size(); j++) {
				combs.add(ents.get(i) + " " + ents.get(j));
			}
		}

		return combs;
	}

	/*
	 * compare how many entities are common in the entire list
	 */
	public void compareCoverage(List<Record> goldset_records,
			List<Record> generated_records) {
		EvalMetrics em = new EvalMetrics();
		HashSet<String> goldset_entities = new HashSet<String>();
		HashSet<String> generated_entities = new HashSet<String>();

		for (Record r1 : goldset_records) {
			goldset_entities.addAll(r1.entities);
		}

		for (Record r2 : generated_records) {
			generated_entities.addAll(r2.entities);
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

		System.out.println("Precision: " + em.calculatePrecision());
		System.out.println("Recall: " + em.calculateRecall());

	}

	/*
	 * include frequency of occurrence in the goldset as weight
	 */
	public void compareCoverageFrequency(List<Record> goldset_records,
			List<Record> generated_records) {
		EvalMetrics em = new EvalMetrics();
		Counter<String> goldset_entities = new Counter<String>();
		List<String> generated_entities = new ArrayList<String>();

		for (Record r1 : goldset_records) {
			goldset_entities.incrementAll(r1.entities, 1.0);
		}
		goldset_entities = Counters.normalize(goldset_entities);

		for (Record r2 : generated_records) {
			generated_entities.addAll(r2.entities);
		}

		for (String ent : generated_entities) {
			if (goldset_entities.containsKey(ent)) {
				em.incrTP(goldset_entities.getCount(ent));
			} else {
				em.incrFP(goldset_entities.getCount(ent));
			}
		}
	}

	/*
	 * compare how many entities are common in the entire list
	 */
	public void compareCoOccurenceCoverage(List<Record> goldset_records,
			List<Record> generated_records) {
		EvalMetrics em = new EvalMetrics();
		Counter<String> gold_coocs = new Counter<String>();
		Counter<String> gen_coocs = new Counter<String>();
		for (Record r : goldset_records) {
			gold_coocs.incrementAll(getAllCombinations(r.entities), 1.0);
		}

		for (Record r : generated_records) {
			gen_coocs.incrementAll(getAllCombinations(r.entities), 1.0);
		}

		for (String ent : gen_coocs.keySet()) {
			if (gold_coocs.containsKey(ent)) {
				em.incrTP();
			} else {
				em.incrFP();
			}
		}

		for (String ent : gold_coocs.keySet()) {
			if (!gen_coocs.containsKey(ent)) {
				em.incrFN();
			}
		}
		System.out.println("Precision: " + em.calculatePrecision());
		System.out.println("Recall: " + em.calculateRecall());
	}

	/*
	 * compare custom entities
	 */
	public void compareCustomEntities(List<String> target_entities,
			List<Record> generated_records, int n) {
		EvalMetrics em = new EvalMetrics();
		HashSet<String> generated_entities = new HashSet<String>();

		int count = 0;
		for (Record r2 : generated_records) {
			count++;
			generated_entities.addAll(r2.entities);
			if (count == n)
				break;
		}

		for (String ent : generated_entities) {
			if (target_entities.contains(ent)) {
				em.incrTP();
			} else {
				em.incrFP();
			}
		}

		for (String ent : target_entities) {
			if (!generated_entities.contains(ent))
				em.incrFN();
		}

		System.out.println("Precision: " + em.calculatePrecision());
		System.out.println("Recall: " + em.calculateRecall());
	}

	public static void main(String[] args) {
		Data u = new Data();
		SummaryEvaluation se = new SummaryEvaluation();
		// reuters2.annotated.txt
		try {
			List<Record> goldSummary = u
					.readDataSetDirectory("datasets/goldsets/clinton_input");
			List<Record> generatedSummary = u
					.readDataSet("datasets/testsets/clinton/tweet_ranks.clinton.annotated.txt");
			se.compareCoverage(generatedSummary, goldSummary);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
