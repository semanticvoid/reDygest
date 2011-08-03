package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.util.CounterMap;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.srl.Senna;
import com.redygest.preprocessor.TwitterPreprocessor;

public class NGramSynonymExtractor extends AbstractFeatureExtractor{

	private static Senna senna = new Senna(config.getSennaPath());

	public List<String> getNGrams(String text, int n){
		List<String> ngrams = new ArrayList<String>();
		String[] split = text.split("\\s+");
		for(int i=0; i<split.length; i++){
			int count=0;
			StringBuffer sb = new StringBuffer();
			for(int j=i; j < split.length && count<n; j++){
				sb.append(" "+split[j]);
				ngrams.add(sb.toString().trim());
				count++;
			}
		}
		return ngrams;
	}
	
	public HashMap<String, ArrayList<String>> collapseSimilarEntities(List<String> entities){
		CounterMap<String, String> clusters = new CounterMap<String, String>();
		HashMap<String, ArrayList<String>> entityToPotentialClusters = new HashMap<String, ArrayList<String>>();
		//HashMap<String, String> entityToCorrectCluster = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> finalClusters = new HashMap<String, ArrayList<String>>();

		for(String entity : entities){
			List<String> potentialClusters = getNGrams(entity, 3);
			entityToPotentialClusters.put(entity, new ArrayList<String>());
			for(String p_cluster : potentialClusters){
				clusters.incrementCount(p_cluster, entity, 1.0);
				entityToPotentialClusters.get(entity).add(p_cluster);
			}
		}
		
		//iterate through entities and find the best cluster i.e the potentialCluster with the highest count. ties are broken arbitly for now
		for(String entity : entities){
			List<String> potentialCluters = entityToPotentialClusters.get(entity);
			double countMax = 0.0;
			String argMax = "";
			for(String p_cluster : potentialCluters){
				double count = clusters.getCounter(p_cluster).totalCount();
				if(count > countMax){
					countMax = count;
					argMax=p_cluster;
				}
			}
			//entityToCorrectCluster.put(entity, argMax);
			if(finalClusters.containsKey(argMax)){
				finalClusters.get(argMax).add(entity);
			} else{
				ArrayList<String> entites = new ArrayList<String>();
				entites.add(entity);
				finalClusters.put(argMax, entites);
			}
		}

		List<List<String>> clustersList= new ArrayList<List<String>>(); 
		for(String clusterLabel : finalClusters.keySet()){
			List<String> cluster = new ArrayList<String>(finalClusters.get(clusterLabel));
			clustersList.add(cluster);
			//System.out.println("Cluster: "+cluster+" "+"\nEntites: "+finalClusters.get(cluster)+"\n");
		}
		
		return finalClusters;
	}

	@Override
	public FeatureVector extract(Data t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Override
	public Features extract(List<Data> dataList) {
		Features features = new Features();
		TwitterPreprocessor preProcessor = new TwitterPreprocessor(); 
		List<String> allNounPhrases = new ArrayList<String>();
		for(Data t : dataList) {
			String tweet= preProcessor.preprocess(t).getValue(DataType.BODY);
			allNounPhrases.addAll(senna.getNounPhrases(tweet));	
		}
		HashMap<String, ArrayList<String>> clusters = collapseSimilarEntities(allNounPhrases);
		
		FeatureVector fVector = new FeatureVector();
		for(String label : clusters.keySet()){
			List<String> synonyms = clusters.get(label);
			for(int i=0; i <synonyms.size(); i++){
				Variable var = fVector.getVariable(new DataVariable(synonyms.get(i), (long) Features.GLOBAL_IDENTIFIER));
				for(int j=0; j < synonyms.size(); j++){
					if(i!=j){
						if (var == null) {
							var = new DataVariable(synonyms.get(i), (long) Features.GLOBAL_IDENTIFIER);
						}
						
						Attributes attrs = var.getVariableAttributes();
						//if (!attrs.containsKey(synonyms.get(j))) {
							attrs.put(AttributeType.SYNONYM, synonyms.get(j));
						//}
					}
				}
				fVector.addVariable(var);
			}
		}
		features.addGlobalFeatures(fVector, true);
		return features;
	}


}
