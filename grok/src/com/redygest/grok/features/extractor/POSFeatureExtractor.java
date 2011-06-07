package com.redygest.grok.features.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class POSFeatureExtractor extends AbstractFeatureExtractor {

	private static final LexicalizedParser parser = new LexicalizedParser(
			"/Users/semanticvoid/projects/reDygest/grok/data/englishPCFG.ser.gz");

	@Override
	public Features extract(List<Data> dataList) {
		Features features = new Features();

		for (Data d : dataList) {
			long id = Long.valueOf(d.getValue(DataType.RECORD_IDENTIFIER));
			FeatureVector fVector = new FeatureVector();
			// TODO use cleaned/preprocessed data
			parser.parse(d.getValue(DataType.BODY));
			Tree t = parser.getBestParse();
			List<String> tags = getPOSTags(t);
			String prevTag = null;
			for (String tag : tags) {
				String[] tokens = tag.split("::");
				
				//bigram
				if(prevTag != null) {
					DataVariable var = new DataVariable(tokens[0], id);
					Attributes attrs = var.getVariableAttributes();
					attrs.put(prevTag + " " + tokens[1], AttributeType.POSBIGRAM);
					fVector.addVariable(var);
				}
				
				// unigram
				DataVariable var = new DataVariable(tokens[0], id);
				Attributes attrs = var.getVariableAttributes();
				attrs.put(tokens[1], AttributeType.POS);
				fVector.addVariable(var);
			}

			Map<Long, FeatureVector> map = new HashMap<Long, FeatureVector>();
			map.put(id, fVector);
			features.addFeatures(map);
		}

		return features;
	}

	private List<String> getPOSTags(Tree tree) {
		Tree root = tree;
		List<String> tags = new ArrayList<String>();
		ArrayList<Tree> queue = new ArrayList<Tree>();
		queue.add(tree);

		while (queue.size() > 0) {
			Tree node = queue.remove(0);

			if (!node.isLeaf() && node.firstChild().isLeaf()) {
				tags.add(node.firstChild() + "::" + node.value());
			}

			List<Tree> children = node.getChildrenAsList();
			for (Tree child : children) {
				queue.add(child);
			}
		}

		return tags;
	}

	@Override
	public FeatureVector extract(Data t) {
		// TODO Auto-generated method stub
		return null;
	}

}
