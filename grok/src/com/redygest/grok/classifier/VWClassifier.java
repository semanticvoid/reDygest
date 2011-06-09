package com.redygest.grok.classifier;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;

/**
 * Vowpal Wabbit Classifier
 * 
 * @author semanticvoid
 * 
 */
public class VWClassifier extends AbstractClassifier {

	protected final String EXEC = "/usr/bin/vw";
	protected String model;
	protected double threshold;

	public VWClassifier() {
		model = null;
		threshold = 0.5;
	}

	public VWClassifier(String model, double threshold) {
		this.model = model;
		this.threshold = threshold;
	}

	public List<String> classify(List<Data> corpus) {
		List<String> classifications = new ArrayList<String>();

		try {
			File corpusFile = File.createTempFile("corpus", "vw");
			corpusFile.deleteOnExit();
			File predictionFile = File.createTempFile("prediction", "vw");
			predictionFile.deleteOnExit();
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					corpusFile));
			for (int i = 0; i < corpus.size(); i++) {
				Data d = corpus.get(i);
				StringBuffer instance = new StringBuffer("0 1 " + i);
				instance.append(getFeatures(d) + "\n");
				writer.write(instance.toString());
			}
			writer.close();

			String cmd = EXEC + " -i " + model + " -t -d "
					+ corpusFile.getAbsolutePath() + " -p "
					+ predictionFile.getAbsolutePath();
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			Process shell = pb.start();
			shell.waitFor();
			
			BufferedReader reader = new BufferedReader(new FileReader(
					predictionFile));
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split("[ \t]+");
				if(tokens.length >= 2) {
					double prediction = Double.valueOf(tokens[0]);
					if(prediction >= threshold) {
						classifications.add("1");
					} else {
						classifications.add("0");
					}
				}
			}
			reader.close();
			
			return classifications;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected String getFeatures(Data d) {
		return "";
	}
}
