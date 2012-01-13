import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import cs224n.util.CounterMap;
import cs224n.util.PriorityQueue;

public class Factoids {

	public String readFile(String fileName, int start_index, int end_index) throws IOException {
		StringBuilder text = new StringBuilder();
		String nl = System.getProperty("line.separator");
		Scanner scanner = new Scanner(new FileInputStream(fileName));
		int cursor = 0;
		try {
			while (scanner.hasNextLine() && cursor < start_index) {
				scanner.nextLine();
				cursor++;
			}
			while (scanner.hasNextLine() && cursor < end_index) {
				text.append(scanner.nextLine() + nl);
				cursor++;
			}
		} finally {
			scanner.close();
		}
		
		return text.toString();
	}
	
	public void writeFile(String fileName, String content){
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		    out.write(content);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	List<NamedEntity> getEntities(String ner_output){
		List<NamedEntity> entities = new ArrayList<NamedEntity>();
		String cur_label = "null";
		String prev_label = "null";
		
		String[] tokens = ner_output.split("\\s+");
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for(String token : tokens){
			String[] entity_class = token.split("/");
			String ent = "";
			String label = "";
			if(entity_class.length<2)
				continue;
			
			if(entity_class.length>2){
				NamedEntity ne = correctBadEntries(token);
				if(ne ==null || ne.text==null || ne.ner_class==null){
					continue;
				}
				ent = ne.text;
				label = ne.ner_class;
			} else{
				ent = entity_class[0].trim();
				label = entity_class[1].trim();
			}
			
			if(label.equalsIgnoreCase("O")){
				if(flag==true){
					NamedEntity ne = new NamedEntity(sb.toString().trim(), prev_label);
					entities.add(ne);
					flag = false;
					sb = new StringBuffer();
					prev_label= "null";
				}
				continue;
			}
			
			cur_label = label;
			if(prev_label.equalsIgnoreCase("null") || prev_label.equalsIgnoreCase(cur_label)){
				sb.append(ent+" ");
				flag = true;
			} else if(flag == true){
				NamedEntity ne = new NamedEntity(sb.toString().trim(), prev_label);
				entities.add(ne);
				flag = false;
				sb = new StringBuffer();
				sb.append(ent+" ");
			}
			prev_label = cur_label;
		}
		if(flag==true && sb.length()>0){
			NamedEntity ne = new NamedEntity(sb.toString().trim(), prev_label);
			entities.add(ne);			
		}
		
		return entities;
	}
	
	public NamedEntity correctBadEntries(String text){
		List<String> labels = new ArrayList<String>();
		labels.add("PERSON");
		labels.add("LOCATION");
		labels.add("ORGANIZATION");
		NamedEntity ne = null;
		
		for(String label : labels){
			if(text.contains(label)){
				String[] split = text.split("/"+label);
				ne = new NamedEntity(split[0], label);
			}
		}
		return ne;
	}

	public static void main(String[] args) throws IOException {
		CounterMap<String, String> map = new CounterMap<String, String>();

		HashMap<String, String> idMap = new HashMap<String, String>();
		HashMap<String, String> invIdMap = new HashMap<String, String>();
		
		Factoids main = new Factoids();
		String serializedClassifier = "data/classifiers/conll.4class.distsim.crf.ser";
		if (args.length > 0) {
			serializedClassifier = args[0];
		}
		
		int id=0;

		AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		String content = main.readFile("lokpal-parsed.txt", 0, 10000);
		String[] lines = content.split("\n");
		StringBuilder sb = new StringBuilder();
		for(String line : lines){
			String output = classifier.classifyToString(line);
			List<NamedEntity> entitiesInLine = main.getEntities(output);
			List<String> idsInLine = new ArrayList<String>();
			for(NamedEntity ne : entitiesInLine){
				if(!idMap.containsKey(ne.text)){
					id++;
					idMap.put(ne.text, Integer.toString(id));
					invIdMap.put(Integer.toString(id), ne.text);
					idsInLine.add(Integer.toString(id));
				} else{
					idsInLine.add(idMap.get(ne.text));
				}
				
			}
			StringBuilder sb1 = new StringBuilder();
			for(String ids : idsInLine){
				sb1.append(ids+" ");
			}
			String fString = sb1.toString().trim();
			if (fString != null && fString.length() != 0) {
				sb.append(fString);
				sb.append("\n");
			}

			for(int i=0; i <entitiesInLine.size(); i++){
				for(int j=i+1; j < entitiesInLine.size(); j++){
					if(entitiesInLine.get(i).text.equalsIgnoreCase(entitiesInLine.get(j).text)){
						continue;
					}
					map.incrementCount(entitiesInLine.get(i).text, entitiesInLine.get(j).text, 1.0);
					map.incrementCount(entitiesInLine.get(j).text, entitiesInLine.get(i).text, 1.0);
				}
			}
		}
		
//		main.writeFile("src/apriori/input.txt", sb.toString());
		
		PriorityQueue<String> pq1=  new PriorityQueue<String>();
		for(String ne : map.keySet()){
			pq1.add(ne, map.getCounter(ne).totalCount());
		}
		
		int count = 0;
		while(pq1.hasNext() && count<100){
			count++;
			String key = pq1.next();
			System.out.println("Entity: "+key);
			System.out.println("Co-occurances "+map.getCounter(key).toString(10));
			
		}
	}

}
