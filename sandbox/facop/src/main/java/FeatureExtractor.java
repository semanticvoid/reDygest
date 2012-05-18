import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.redygest.commons.nlp.POSTagger;
import com.redygest.commons.nlp.TaggedToken;

/**
 * 
 * @author semanticvoid
 */
public class FeatureExtractor {

	private static final double TEST_PERCENT = 0.1;
	private static final SWN3 sentiWordNet = new SWN3();

	private static String memoizeText = null;
	private static List<TaggedToken> memoizeTokens = null;

	public Corpus training;
	public Corpus test;

	public FeatureExtractor() {
		training = new Corpus();
		test = new Corpus();
	}

	public void readInstances(String corpusPath) {
		try {
			BufferedReader rdr = new BufferedReader(new FileReader(corpusPath));
			int trainId = 0;
			int testId = 0;

			String line = null;
			while ((line = rdr.readLine()) != null) {
				String[] tokens = line.split("\t");

				if (Math.random() > TEST_PERCENT) {
					training.addInstance(new Instance(trainId++, tokens[0],
							tokens[1]));
				} else {
					test.addInstance(new Instance(testId++, tokens[0],
							tokens[1]));
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(FeatureExtractor.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	/**
	 * Extract only Personal Pronouns
	 * 
	 * @param instances
	 */
	public void extractFeaturesI(List<Instance> instances) {
		String[] ppronouns = { "i", "you", "he", "she", "it", "me", "him",
				"her", "myself", "yourself", "himself", "herself", "itself",
				"we", "they", "us", "them", "mine", "your", "yourself", "our",
				"ours", "yours" };

		for (Instance i : instances) {
			String text = i.line;
			String[] tokens = text.toLowerCase().split("[!@#$%^&*(),.:'\"; ]+");

			HashMap<String, Integer> ppFeatures = new HashMap<String, Integer>();
			for (String t : tokens) {
				for (String pp : ppronouns) {
					if (t.equals(pp)) {
						if (!ppFeatures.containsKey(pp)) {
							ppFeatures.put(pp, 0);
						}

						ppFeatures.put(pp, ppFeatures.get(pp) + 1);
					}
				}
			}

			StringBuffer buf = new StringBuffer("|ppfeatures ");
			for (String key : ppFeatures.keySet()) {
				buf.append(key + ":" + ppFeatures.get(key) + " ");
			}

			i.features += buf.toString();
		}
	}

	/**
	 * Extract only Punctuations
	 * 
	 * @param instances
	 */
	public void extractFeaturesII(List<Instance> instances) {
		char[] puncts = { '.', '!', '?', '@', '#', ',' };

		for (Instance i : instances) {
			String text = i.line;
			char[] characters = text.toLowerCase().toCharArray();

			HashMap<Character, Integer> pFeatures = new HashMap<Character, Integer>();
			for (Character c : characters) {
				for (Character p : puncts) {
					if (c == p) {
						if (!pFeatures.containsKey(p)) {
							pFeatures.put(p, 0);
						}

						pFeatures.put(p, pFeatures.get(p) + 1);
					}
				}
			}

			StringBuffer buf = new StringBuffer("|punctfeatures ");
			for (Character key : pFeatures.keySet()) {
				buf.append(key + ":" + pFeatures.get(key) + " ");
			}

			i.features += buf.toString();
		}
	}

	/**
	 * Extract number of adjectives
	 * 
	 * @param instances
	 */
	public void extractFeaturesIII(List<Instance> instances) {
		for (Instance i : instances) {
			String text = i.line;
			if (text.length() == 0) {
				continue;
			}

			List<TaggedToken> posTags = getPOSTags(text);
			List<String> adjs = getAdjectives(posTags);

			StringBuffer buf = new StringBuffer("|adjfeatures ");
			buf.append("adjs:" + adjs.size() + " ");

			i.features += buf.toString();
		}
	}

	/**
	 * Extract sentiwords from sentiwordnet
	 * 
	 * @param instances
	 */
	public void extractFeaturesIV(List<Instance> instances) {
		for (Instance i : instances) {
			String text = i.line;
			if (text.length() == 0) {
				continue;
			}

			List<TaggedToken> posTags = getPOSTags(text);
			List<String> words = getPOSForSentiWordnet(posTags);

			StringBuffer buf = new StringBuffer("|sentifeatures ");
			HashMap<String, Integer> sentCount = new HashMap<String, Integer>();
			for (String word : words) {
				if (sentiWordNet.contains(word)) {
					String sent = sentiWordNet.extract(word);
					if (!sentCount.containsKey(sent)) {
						sentCount.put(sent, 1);
					} else {
						sentCount.put(sent, sentCount.get(sent) + 1);
					}
				}
			}
			for (String key : sentCount.keySet()) {
				buf.append(key + ":" + sentCount.get(key) + " ");
			}

			i.features += buf.toString();
		}
	}

	/**
	 * Extract pos tags
	 * 
	 * @param instances
	 */
	public void extractFeaturesV(List<Instance> instances) {
		for (Instance i : instances) {
			String text = i.line;
			if (text.length() == 0) {
				continue;
			}

			List<TaggedToken> posTags = getPOSTags(text);

			StringBuffer buf = new StringBuffer("|tagfeatures ");
			HashMap<String, Integer> counts = new HashMap<String, Integer>();
			String ptag = null;
			for (TaggedToken t : posTags) {
				if (ptag != null) {
					String key = ptag + "_" + t.getPosTag();
					if (counts.containsKey(key)) {
						counts.put(key, counts.get(key) + 1);
					} else {
						counts.put(key, 1);
					}
				}

				ptag = t.getPosTag();
			}

			for (String key : counts.keySet()) {
				buf.append(key + ":" + counts.get(key) + " ");
			}

			i.features += buf.toString();
		}
	}

	public List<String> getAdjectives(List<TaggedToken> posTags) {
		List<String> adjs = new ArrayList<String>();

		for (TaggedToken t : posTags) {
			String tag = t.getPosTag();
			if (tag.startsWith("JJ")) {
				adjs.add(t.getWord().toLowerCase());
			}
		}

		return adjs;
	}

	public List<String> getPOSForSentiWordnet(List<TaggedToken> posTags) {
		List<String> words = new ArrayList<String>();

		for (TaggedToken t : posTags) {
			String tag = t.getPosTag();
			if (tag.startsWith("J")) {
				words.add(t.getWord().toLowerCase() + "#a");
			} else {
				words.add(t.getWord().toLowerCase() + "#" + tag.charAt(0));
			}
		}

		return words;
	}

	public List<TaggedToken> getPOSTags(String text) {
		if (memoizeText != null && memoizeTokens != null
				&& memoizeText.equals(text)) {
			return memoizeTokens;
		}

		POSTagger tagger = POSTagger.getInstance();
		memoizeText = text;
		memoizeTokens = tagger.tag(text);

		return memoizeTokens;
	}

	public void printTrainCorpus() {
		List<Instance> trainingInstances = training.getInstances();
		// Collections.shuffle(trainingInstances);
		for (Instance i : trainingInstances) {
			System.out.println(i.toVWString());
		}
	}

	public void printTestCorpus() {
		List<Instance> testInstances = test.getInstances();
		for (Instance i : testInstances) {
			System.out.println(i.toVWString());
		}
	}

	public static void main(String[] args) {
		FeatureExtractor fe = new FeatureExtractor();
		fe.readInstances(args[0]);

		// extract personal pronoun features
		fe.extractFeaturesI(fe.training.getInstances());
		fe.extractFeaturesI(fe.test.getInstances());

		// extract puntuation features
		fe.extractFeaturesII(fe.training.getInstances());
		fe.extractFeaturesII(fe.test.getInstances());

		// extract adjectives features
		fe.extractFeaturesIII(fe.training.getInstances());
		fe.extractFeaturesIII(fe.test.getInstances());

		// extract sentiment features
		fe.extractFeaturesIV(fe.training.getInstances());
		fe.extractFeaturesIV(fe.test.getInstances());

		// extract pos tag features
		fe.extractFeaturesV(fe.training.getInstances());
		fe.extractFeaturesV(fe.test.getInstances());

		fe.printTrainCorpus();
		System.out.println("\n-----------------\n");
		fe.printTestCorpus();
	}
}
