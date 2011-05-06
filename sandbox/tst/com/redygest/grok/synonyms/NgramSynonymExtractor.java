package tst.com.redygest.grok.collapseSimilarEntities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import tst.com.redygest.grok.cs224n.util.Counter;
import tst.com.redygest.grok.cs224n.util.CounterMap;
import tst.com.redygest.grok.srl.BinaryRelations;

public class NgramCollapser {
	
	public static String readFile(String file){
		StringBuilder content = new StringBuilder();
		try{
			BufferedReader br= new BufferedReader(new FileReader(file));
			String line="";
			while((line=br.readLine())!=null){
				content.append(line+"\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return content.toString();
	}
	
	public static HashSet<String> flattenCounterMap(CounterMap<String, String> map){
		HashSet<String> entities = new HashSet<String>();
		for(String entity : map.keySet()){
			entities.add(entity);
			for(String l_entity : map.getCounter(entity).keySet()){
				entities.add(l_entity);
			}
		}
		return entities;
	}
	
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
	
	public void collapseSimilarEntities(List<String> entities){
		CounterMap<String, String> clusters = new CounterMap<String, String>();
		HashMap<String, ArrayList<String>> entityToPotentialClusters = new HashMap<String, ArrayList<String>>();
		HashMap<String, String> entityToCorrectCluster = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> finalClusters = new HashMap<String, ArrayList<String>>();

		for(String entity : entities){
			List<String> potentialClusters = getNGrams(entity, 2);
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
			entityToCorrectCluster.put(entity, argMax);
			if(finalClusters.containsKey(argMax)){
				finalClusters.get(argMax).add(entity);
			} else{
				ArrayList<String> entites = new ArrayList<String>();
				entites.add(entity);
				finalClusters.put(argMax, entites);
			}
		}
		for(String cluster : finalClusters.keySet()){
			System.out.println("Cluster: "+cluster+" "+"\nEntites: "+finalClusters.get(cluster)+"\n");
		}
	}
	/**
	 * @param args
	 * collapse similar entities containing common tokens
	 */
	public static void main(String[] args) {
		NgramCollapser ngc = new NgramCollapser();
		
//		CounterMap<String, String> biRelations = BinaryRelations.buildBinaryRelations(args[0]);
//		HashSet<String> entities = flattenCounterMap(biRelations);
//		System.out.println("start");
//		for(String entity: entities){
//			System.out.println(entity);
//		}
		
		
		String fcontents = readFile(args[0]);
		List<String> entities = new ArrayList<String>();
		for(String entity : fcontents.split("\n")){
			entities.add(entity);
		}
		ngc.collapseSimilarEntities(entities);

	}

}
