package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.NamedEntity;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.features.repository.IFeaturesRepository;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

public class NERFeatureExtractor extends AbstractFeatureExtractor {

	private static final AbstractSequenceClassifier classifier = CRFClassifier
			.getClassifierNoExceptions(config.getNERClassifierPath());

	List<NamedEntity> getEntities(String nerOutput) {
		List<NamedEntity> entities = new ArrayList<NamedEntity>();
		String curLabel = "null";
		String prevLabel = "null";

		String[] tokens = nerOutput.split("\\s+");
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for (String token : tokens) {
			String[] entityClass = token.split("/");
			String ent = "";
			String label = "";
			if (entityClass.length < 2)
				continue;

			if (entityClass.length > 2) {
				NamedEntity ne = correctBadEntries(token);
				if (ne == null || ne.getText() == null
						|| ne.getEntityClass() == null) {
					continue;
				}
				ent = ne.getText();
				label = ne.getEntityClass();
			} else {
				ent = entityClass[0].trim();
				label = entityClass[1].trim();
			}

			if (label.equalsIgnoreCase("O")) {
				if (flag == true) {
					NamedEntity ne = new NamedEntity(sb.toString().trim(),
							prevLabel);
					entities.add(ne);
					flag = false;
					sb = new StringBuffer();
					prevLabel = "null";
				}
				continue;
			}

			curLabel = label;
			if (prevLabel.equalsIgnoreCase("null")
					|| prevLabel.equalsIgnoreCase(curLabel)) {
				sb.append(ent + " ");
				flag = true;
			} else if (flag == true) {
				NamedEntity ne = new NamedEntity(sb.toString().trim(),
						prevLabel);
				entities.add(ne);
				flag = false;
				sb = new StringBuffer();
				sb.append(ent + " ");
			}
			prevLabel = curLabel;
		}
		if (flag == true && sb.length() > 0) {
			NamedEntity ne = new NamedEntity(sb.toString().trim(), prevLabel);
			entities.add(ne);
		}

		return entities;
	}

	public NamedEntity correctBadEntries(String text) {
		List<String> labels = new ArrayList<String>();
		labels.add("PERSON");
		labels.add("LOCATION");
		labels.add("ORGANIZATION");
		NamedEntity ne = null;

		for (String label : labels) {
			if (text.contains(label)) {
				String[] split = text.split("/" + label);
				ne = new NamedEntity(split[0], label);
			}
		}
		return ne;
	}

	@Override
	protected FeatureVector extract(Data d, IFeaturesRepository repository) {
		long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
		FeatureVector fVector = new FeatureVector();
		String ner_output = classifier.classifyToString(d
				.getValue(DataType.BODY));
		List<NamedEntity> nes = getEntities(ner_output);
		for (NamedEntity ne : nes) {
			Variable var = fVector.getVariable(new DataVariable(ne.getText(),
					id));
			if (var == null) {
				var = new DataVariable(ne.getText(), id);
			}
			var.addAttribute(ne.getEntityClass(), AttributeType.NER_CLASS);
			fVector.addVariable(var);
		}

		return fVector;
	}

}
