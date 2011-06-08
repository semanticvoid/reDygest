package com.redygest.grok.classifier;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;

/**
 * Vowpal Wabbit Classifier
 * @author semanticvoid
 *
 */
public class VWClassifier extends AbstractClassifier {

	protected final String EXEC = "/usr/bin/vw";
	protected String model;
	
	public VWClassifier() {
		model = null;
	}
	
	public VWClassifier(String model) {
		this.model = model;
	}
	
	public List<String> classify(List<Data> corpus) {
		List<String> classifications = new ArrayList<String>();
		
		try {
			File corpusFile = File.createTempFile("corpus", "vw");
			corpusFile.deleteOnExit();
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(corpusFile));
			for(int i=0; i<corpus.size(); i++) {
				Data d = corpus.get(i);
				StringBuffer instance = new StringBuffer("0 1 " + i);
				instance.append(getFeatures(d));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected String getFeatures(Data d) {
		return "";
	}
}
