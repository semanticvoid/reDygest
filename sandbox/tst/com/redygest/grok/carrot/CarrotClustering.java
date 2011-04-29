package com.redygest.grok.carrot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;
import org.carrot2.core.attribute.CommonAttributesDescriptor;



public class CarrotClustering {

	public List<Cluster> cluster(List<String> data){
		final ArrayList<Document> documents = new ArrayList<Document>();
		for(String record : data){
			documents.add(new Document("", record));
		}
//        final Map<String, Object> attributes = new HashMap<String, Object>();
//        attributes.put("LingoClusteringAlgorithm.desiredClusterCountBase", 15);
        
		final Controller controller = ControllerFactory.createSimple();
		final ProcessingResult byTopicClusters = controller.process(documents, null, LingoClusteringAlgorithm.class);
		final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
		return clustersByTopic;
	}
	
	public void writeClustersToFile(List<Cluster> clusters, String outputFile){
		ConsoleFormatter.displayClusters(clusters, outputFile);
	}
	
	public static void main(String args[]) {
		int count =0;
		CarrotClustering cc = new CarrotClustering();
		try {
			List<String> tweets = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			String line = "";
			System.out.println("Reading data...");
			while((line=br.readLine())!=null){
				tweets.add(line);
			}
			
			NounPhraseExtractor npe = new NounPhraseExtractor();
			System.out.println("Getting features and processing clusters...");
			List<Cluster> clusters = cc.cluster(npe.getFeatures(tweets));
			cc.writeClustersToFile(clusters, "test_clusters.txt");
			System.out.println("Finished writing clusters to ");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
