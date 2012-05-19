package com.redygest.grok.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Utils {
	
	public static List<String> getPairwaiseOccurences(List<String> ents) {
		Collections.sort(ents);
		List<String> combs = new ArrayList<String>();
		// generate 2-itemsets
		for (int i = 0; i < ents.size(); i++) {
			for (int j = i + 1; j < ents.size(); j++) {
				combs.add(ents.get(i) + " " + ents.get(j));
			}
		}
		return combs;
	}
	
	public static List<String> getTripleOccurences(List<String> ents) {
		Collections.sort(ents);
		List<String> combs = new ArrayList<String>();
		// generate 3-itemsets
		for (int i = 0; i < ents.size(); i++) {
			for (int j = i + 1; j < ents.size(); j++) {
				for(int k= j+1; k < ents.size(); k++){
					combs.add(ents.get(i) + " " + ents.get(j)+" "+ents.get(k));
				}
			}
		}
		return combs;
	}
	
}
