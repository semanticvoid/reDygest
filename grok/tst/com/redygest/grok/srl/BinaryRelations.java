package com.redygest.grok.srl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.redygest.grok.cs224n.util.Counter;
import com.redygest.grok.cs224n.util.CounterMap;

public class BinaryRelations{
	
	/*
	 * returns a ArrayList of lists containing Noun phrases in the arguments of each verb in the line
	 */
	public static List<Verb> extract(String text) {
		Senna senna = new Senna();
		List<Verb> verbs = new ArrayList<Verb>();
		String[] lines = text.split("[:;'\"?/><,\\.!@#$%^&()-+=~`{}|]+");
		for (String line : lines) {
			if((line=line.trim()).length()==0)
				continue;
			String allLines = senna.getSennaOutput(line);
			//System.out.println(allLines);
			HashMap<String, Verb> verbArgs = senna.parseSennaLines(allLines, line);
			for (String s : verbArgs.keySet()) {
				Verb verb = verbArgs.get(s);
				verbs.add(verb);
			}
		}
		return verbs;
	}
	
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
	
	public static CounterMap<String, String> buildBinaryRelations(String trainingFile){
		CounterMap<String, String> biRelations = new CounterMap<String, String>();
		
		String tweets = readFile(trainingFile);
		for(String tweet : tweets.split("\n")){

			String[] lines = tweet.split("[:;'\"?/><,\\.!@#$%^&()-+=~`{}|]+");
			for(String line : lines){
			
				List<Verb> verbs = extract(line);
				for (Verb verb : verbs) {
					String[] args = verb.argumentToNPs.keySet().toArray(new String[verb.argumentToNPs.keySet().size()]);
					if(args.length==0)
						continue;
					Arrays.sort(args);
			
					String head_arg = args[0];
					for (String headargNP : verb.argumentToNPs.get(head_arg)) {
						headargNP = headargNP.trim().toLowerCase();
					
						for (String arg : args) {
							for (String np : verb.argumentToNPs.get(arg)) {
								np = np.toLowerCase().trim();
								if(!np.equalsIgnoreCase(headargNP)){
									biRelations.incrementCount(headargNP, np, 1.0);
								}
							}
						}	
					}
				}
			}
		}
		
		return biRelations;
	}
	
	public static void printBiRelation(CounterMap<String, String> biRelations, int num_results){
		for(String headarg : biRelations.keySet()){
			Counter<String> counter = biRelations.getCounter(headarg);
			System.out.println("Head Word: "+headarg+" Total count: "+counter.totalCount());
			System.out.println("Related NPs: "+counter.toString(num_results)+"\n");
		}
	}
	
	
	public static void main(String args[]){
		BinaryRelations br = new BinaryRelations();
		printBiRelation(buildBinaryRelations(args[0]), 10);

	}

}
