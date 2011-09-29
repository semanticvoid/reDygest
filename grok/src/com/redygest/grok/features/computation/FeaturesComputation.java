package com.redygest.grok.features.computation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.extractor.FeatureExtractorFactory;
import com.redygest.grok.features.extractor.FeatureExtractorType;
import com.redygest.grok.features.extractor.IFeatureExtractor;
import com.redygest.grok.features.extractor.POSFeatureExtractor;
import com.redygest.grok.repository.FeaturesRepository;

public class FeaturesComputation {
	
	FeaturesRepository repository = FeaturesRepository.getInstance();
	
	public FeaturesComputation() {
	}
	
	public void computeFeatures(List<Data> data) throws Exception {
		FeatureExtractorFactory featureExtractorFactory = FeatureExtractorFactory.getInstance();
		IFeatureExtractor featureExtractor = featureExtractorFactory.getFeatureExtractor(FeatureExtractorType.SRL);
		repository.addFeatures(featureExtractor.extract(data));
	}

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

		Features fc = new Features();
		
		try {
			BufferedReader rdr = new BufferedReader(new FileReader(new File(
					args[0])));
			String line;
			int i = 0;
			while ((line = rdr.readLine()) != null) {
				try {
					Data t = new Tweet(line, String.valueOf(i));
					if (t.getValue(DataType.BODY) != null) {
						IFeatureExtractor ext = new POSFeatureExtractor();
						List<Data> list = new ArrayList<Data>();
						list.add(t);
						Features fv = ext.extract(list);
//						fc.addGlobalFeatures(fv., true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
}
