package com.redygest.grok.carrot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.redygest.grok.twitter.TwitterTokenizer;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class NounPhraseExtractor  {

	private static LexicalizedParser lp;

	static {
		try {
			lp = new LexicalizedParser("/Users/tejaswi/Documents/workspace/PersonalityExtraction/lair/englishPCFG.ser.gz");
			lp.setOptionFlags(new String[] { "-maxLength", "80",
					"-retainTmpSubcategories" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getFeatures(List<String> sentences){
		List<String> feats = new ArrayList<String>();
		TwitterTokenizer tweetTokenizer = new TwitterTokenizer();
		NounPhraseExtractor npe = new NounPhraseExtractor();
		for (String tweet : sentences) {
			StringBuilder doc = new StringBuilder();
			for (String cleanTweet : tweetTokenizer.tokenize(tweet)) {
				for (String s : npe.extract(cleanTweet)) {
					doc.append(s+"\t");
				}
			}
			feats.add(doc.toString());
		}
		return feats;
	}
	
	public List<String> extract(String text) {
		String[] lines = text.split("[:;'\"?/><,\\.!@#$%^&()-+=~`{}|]+");
		ArrayList<String> phrases = new ArrayList<String>();
		
		for (String line : lines) {
			if(line.trim().length()==0)
				continue;
			String[] sent = line.split(" ");
			Tree parse = (Tree) lp.apply(Arrays.asList(sent));
			// TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
			// tp.printTree(parse);
			ArrayList<Tree> queue = new ArrayList<Tree>();
			queue.add(parse);

			StringBuffer str = new StringBuffer();
			boolean flag = false;
			while (!queue.isEmpty()) {
				Tree topNode = queue.remove(0);

				if (topNode.isPreTerminal()) {
					if (topNode.value().startsWith("NN")) {
						str.append(topNode.children()[0].value() + " ");
						flag = true;
					} else if (flag == true) {
						flag = false;
						phrases.add(str.toString().trim().replaceAll("\\.", ""));
						str = new StringBuffer();
					}
				} else if (flag == true) {
					flag = false;
					phrases.add(str.toString().trim().replaceAll("\\.", ""));
					str = new StringBuffer();
				}
				// add all children to queue regardless
				for (Tree c : topNode.children()) {
					queue.add(c);
				}
			}

			if (flag == true) {
				phrases.add(str.toString().trim().replaceAll("\\.", ""));
			}
		}
		return phrases;
	}
}
