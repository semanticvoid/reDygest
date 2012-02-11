package com.redygest.commons.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.redygest.commons.config.ConfigReader;

public class SentiWordNet {

	public static SentiWordNet instance = null;
	
	private String pathToSWN;
	private HashMap<String, String> _dict;

	public static SentiWordNet getInstance() {
		if(instance == null) {
			try {
				instance = new SentiWordNet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	private SentiWordNet() throws Exception {
		pathToSWN = new java.io.File(".").getCanonicalPath() + "/data/SentiWordNet.txt";
		_dict = new HashMap<String, String>();
		HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
		
		ConfigReader config = ConfigReader.getInstance();
		if(config != null) {
			pathToSWN = config.getSentiWordnetPath();
		}
		
		try {
			BufferedReader csv = new BufferedReader(new FileReader(pathToSWN));
			String line = "";
			while ((line = csv.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				} else {
					// System.out.println(line);
				}

				String[] data = line.split("\t");
				Double score = Double.parseDouble(data[2])
						- Double.parseDouble(data[3]);
				String[] words = data[4].split(" ");
				for (String w : words) {
					String[] w_n = w.split("#");
					w_n[0] += "#" + data[0];
					int index = Integer.parseInt(w_n[1]) - 1;
					if (_temp.containsKey(w_n[0])) {
						Vector<Double> v = _temp.get(w_n[0]);
						if (index > v.size()) {
							for (int i = v.size(); i < index; i++) {
								v.add(0.0);
							}
						}
						v.add(index, score);
						_temp.put(w_n[0], v);
					} else {
						Vector<Double> v = new Vector<Double>();
						for (int i = 0; i < index; i++) {
							v.add(0.0);
						}
						v.add(index, score);
						_temp.put(w_n[0], v);
					}
				}
			}
			Set<String> temp = _temp.keySet();
			for (Iterator<String> iterator = temp.iterator(); iterator
					.hasNext();) {
				String word = (String) iterator.next();
				Vector<Double> v = _temp.get(word);
				double score = 0.0;
				double sum = 0.0;
				for (int i = 0; i < v.size(); i++) {
					score += ((double) 1 / (double) (i + 1)) * v.get(i);
				}
				for (int i = 1; i <= v.size(); i++) {
					sum += (double) 1 / (double) i;
				}
				score /= sum;
				String sent = "";
				if (score >= 0.75) {
					sent = "strong_positive";
				} else if (score > 0.25 && score <= 0.5) {
					sent = "positive";
				} else if (score > 0 && score >= 0.25) {
					sent = "weak_positive";
				} else if (score < 0 && score >= -0.25) {
					sent = "weak_negative";
				} else if (score < -0.25 && score >= -0.5) {
					sent = "negative";
				} else if (score <= -0.75) {
					sent = "strong_negative";
				} else {
					sent = "neutral";
				}
				_dict.put(word, sent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String extract(String query, String posTag) {
		return _dict.get(query.toLowerCase() + transformPOSTag(posTag));
	}

	public boolean contains(String query, String posTag) {
		return _dict.containsKey(query.toLowerCase() + transformPOSTag(posTag));
	}

	private String transformPOSTag(String posTag) {
		if (posTag.startsWith("J")) {
			return "#a";
		} else {
			return "#" + posTag.charAt(0);
		}
	}

}
