package com.entityextractionevaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.redygest.commons.util.Counter;
import com.redygest.commons.util.PriorityQueue;

/**
 * 
 * @author tejaswi filtering algorithm
 */
public class FilterEntities {

	final static List<String> stopWords = Arrays.asList("a", "an", "and",
			"are", "as", "at", "be", "but", "by", "for", "if", "in", "into",
			"is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
			"their", "then", "there", "these", "they", "this", "to", "was",
			"will", "with", "most", "needs", "http", "com", "rt", "news");

	HashSet<String> stopEntities = new HashSet<String>();
	EvaluationMetrics em = null;

	/*
	 * 
	 */
	public FilterEntities(String file) {
		em = EvaluationMetrics.getInstance(file);
	}

	/*
	 * contains rules to recognize bad entities
	 */
	public boolean isStopEntity(String entity) {
		if (entity.length() <= 2) {
			return true;
		}

		String[] entityTokens = entity.split("\\s+ .");
		for (String token : entityTokens) {
			if (stopWords.contains(token)) {
				return true;
			}
		}

		String[] split = entity.split("[^A-Za-z ]");
		if (split.length > 2) {
			return true;
		}
		return false;
	}

	/*
	 * 
	 */
	public boolean isCapitalized(String entity) {
		if (Character.isUpperCase(entity.charAt(0))) {
			return true;
		}
		return false;
	}
	

	/*
	 * if entity doesn't co-occur or has same co-occurrence frequents as its
	 * total count
	 */
	public boolean isSpam(String entity) {
		double totalCount = em.ner_counts.getCount(entity);
		if (em.ner_ner_entityCooccurance.keySet().contains(entity)) {
			Counter<String> entities = em.ner_ner_entityCooccurance
					.getCounter(entity);
			if (entities.size() <= 2) {
				return true;
			}

			PriorityQueue<String> pq = entities.asPriorityQueue();
			String key1 = pq.next();
			if (entities.getCount(key1) == totalCount) {
				String key2 = pq.next();
				if (entities.getCount(key2) == totalCount) {
					return true;
				}
			}
		}

		if (em.np_ner_entityCooccurance.keySet().contains(entity)) {
			Counter<String> entities = em.np_ner_entityCooccurance
					.getCounter(entity);
			if (entities.size() <= 3) {
				return true;
			}

			PriorityQueue<String> pq = entities.asPriorityQueue();
			// while (pq.hasNext()) {
			pq.next();
			String key1 = pq.next();
			if (entities.getCount(key1) == totalCount) {
				String key2 = pq.next();
				if (entities.getCount(key2) == totalCount) {
					return true;
				}
			}
			// }
		}
		return false;
	}

	public List<String> filterEntities() {
		HashSet<String> filteredWords = new HashSet<String>();
		for (String ent : em.ner_counts.keySet()) {
			if (isStopEntity(ent) || isSpam(ent)) {
				stopEntities.add(ent);
			}
		}

		for (String ent : em.np_counts.keySet()) {
			if (isStopEntity(ent) || isSpam(ent)) {
				stopEntities.add(ent);
			}
		}

		return new ArrayList<String>(filteredWords);
	}

	Counter<String> getValidEntities() {
		Counter<String> validEntities = new Counter<String>();
		for (String key : em.ner_counts.keySet()) {
			if ((!stopEntities.contains(key) && em.ner_counts.getCount(key) >= 10)) {
				validEntities.setCount(key, em.ner_counts.getCount(key));
			}
		}
		for (String key : em.np_counts.keySet()) {
			if ((!stopEntities.contains(key) && em.np_counts.getCount(key) >= 10)) {
				validEntities.setCount(key, em.np_counts.getCount(key));
			}
		}
		return validEntities;
	}
	

	public static void main(String[] args) {
		try {
			//output file
			BufferedWriter bw = new BufferedWriter(new BufferedWriter(new FileWriter("/Users/tejaswi/Documents/workspace/reDygest/datasets/dedup/lokpal_1_2.entities.txt")));
			FilterEntities fe = new FilterEntities("/Users/tejaswi/Documents/workspace/reDygest/datasets/dedup/lokpal_1_2");
			fe.filterEntities();
			StringBuffer consoleMessages = new StringBuffer();
			System.out.println("============filtered entities============");
			consoleMessages.append("============filtered entities============\n");
			for (String ent : fe.stopEntities) {
				System.out.println("Filtered: " + ent);
				consoleMessages.append("Filtered: " + ent);
				consoleMessages.append("\n");
			}
			
			System.out.println("============Valid entities============");
			consoleMessages.append("============Valid entities============\n");
			Counter<String> validEntityCounter = fe.getValidEntities();
			PriorityQueue<String> validEntities = validEntityCounter
					.asPriorityQueue();
			while (validEntities.hasNext()) {
				String key = validEntities.next();
				System.out.println("Valid: " + key + " Count: "
						+ validEntityCounter.getCount(key));
				consoleMessages.append("Valid: " + key + " Count: "
						+ validEntityCounter.getCount(key));
				consoleMessages.append("\n");
				
			}
			//write to the file
			bw.write(consoleMessages.toString());
			bw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
