package com.redygest.grok.features.computation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.extractor.NPCooccurrenceExtractor;

public class FeaturesComputation extends AbstractFeaturesComputation {

	public static void main(String[] args) {
		// Variable v1 = new DataVariable("a", (long)1);
		// v1.addAttribute("aa", AttributeType.POS);
		// FeatureVector fv1 = new FeatureVector();
		// fv1.addVariable(v1);
		//
		// final Map<Long, FeatureVector> m1 = new HashMap<Long,
		// FeatureVector>();
		// m1.put((long)1, fv1);
		//
		// Variable v2 = new DataVariable("a", (long)1);
		// v2.addAttribute("bb", AttributeType.FREQUENCY);
		// FeatureVector fv2 = new FeatureVector();
		// fv2.addVariable(v2);
		//
		// final Map<Long, FeatureVector> m2 = new HashMap<Long,
		// FeatureVector>();
		// m2.put((long)1, fv2);
		//
		// FeaturesComputation fc = new FeaturesComputation();
		// fc.addFeatures(m1);
		// fc.addFeatures(m2);
		// List<FeatureVector> fv = fc.getFeatures();
		// List<Variable> vs = new
		// ArrayList<Variable>(fv.get(0).getVariables());
		// System.out.println(fv.get(0).getVariables().size());
		// System.out.println(vs.get(0).getVariableAttributes());

		FeaturesComputation fc = new FeaturesComputation();
		NPCooccurrenceExtractor ext = new NPCooccurrenceExtractor();
		
		try {
			BufferedReader rdr = new BufferedReader(new FileReader(new File(
					args[0])));
			String line;
			int i = 0;
			while ((line = rdr.readLine()) != null) {
				try {
					Tweet t = new Tweet(line);
					if (t.getText() != null) {
						FeatureVector fv = ext.extract(t);
						final Map<Long, FeatureVector> m1 = new HashMap<Long, FeatureVector>();
						m1.put((long)i++, fv);
						fc.addFeatures(m1);
					}
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
}
