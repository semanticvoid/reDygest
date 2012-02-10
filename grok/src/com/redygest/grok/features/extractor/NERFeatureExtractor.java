package com.redygest.grok.features.extractor;

import com.redygest.commons.data.NamedEntity;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.FeaturesRepository;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

public class NERFeatureExtractor extends AbstractFeatureExtractor{

	private static final String serializedClassifier = "grok/lib/conll.4class.distsim.crf.ser.gz";
	private static final AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

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

	@Override
	protected FeatureVector extract(Data d, FeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		String ner_output= classifier.classifyToString(d.getValue(DataType.BODY));
		List<NamedEntity> nes = getEntities(ner_output);
		for(NamedEntity ne : nes){
			Variable var = fVector.getVariable(new DataVariable(ne.text,id));
			if(var==null){
				var = new DataVariable(ne.text, id);
			} 
			var.addAttribute(ne.ner_class, AttributeType.NER_CLASS);
			fVector.addVariable(var);
		}

		return fVector;
	}

}
