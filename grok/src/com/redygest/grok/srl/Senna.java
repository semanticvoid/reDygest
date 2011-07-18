package com.redygest.grok.srl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.redygest.commons.cache.CacheFactory;
import com.redygest.commons.cache.ICache;
import com.redygest.commons.cache.CacheFactory.CacheType;

public class Senna {

	String[] lineArr;
	File dir;
	ICache cache;

	/**
	 * Constructor
	 * 
	 * @param path
	 *            - the path to the senna directory
	 */
	public Senna(String path) {
		dir = new File(path);
		init();
	}

	public Senna() {
		init();
	}

	private void init() {
		cache = CacheFactory.getInstance().produceCache(CacheType.MEMORY,
				"senna");
	}

	public List<Verb> getVerbs(String line) {
		String allLines = getSennaOutput(line);
		// System.out.println(allLines);
		return parseSennaLines(allLines, line);
	}

	private String getSennaOutput(String line) {
		try {
			String cmd = "echo " + line + " | " + dir + "/senna ";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			pb.directory(dir);
			Process shell = pb.start();
			InputStream shellIn = shell.getInputStream();
			int shellExitStatus = shell.waitFor();
			int c;
			StringBuffer s = new StringBuffer();
			while ((c = shellIn.read()) != -1) {
				s.append((char) c);
			}
			return s.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Verb> parseSennaLines(String allText, String sentence) {
		lineArr = allText.split("\n");
		HashMap<String, Verb> verbsToArgs = new HashMap<String, Verb>();
		ArrayList<Verb> verbs = new ArrayList<Verb>();
		int verbCount = 0;
		for (int i = 0; i < lineArr.length; i++) {
			// String[] line = lineArr[i].trim().split("\t");
			String line = lineArr[i].trim();
			if (line.length() == 0)
				continue;
			lineArr[i] = line;
			Pattern p = Pattern.compile("VB[A-Z]?\t");
			// System.out.println(line);
			if ((!line.split("\\s+")[4].trim().equalsIgnoreCase("-"))) {
				// Verb v = getVerbArguments(++verbCount,
				// line.split("\\s+")[0].trim(), sentence);
				// SennaVerb v = getVerbArgumentNPs(++verbCount, line
				// .split("\\s+")[0].trim(), sentence);

				Verb v = getVerbArguments(++verbCount,
						line.split("\\s+")[0].trim(), sentence);
				verbs.add(v);
				verbsToArgs.put(v.getText(), v);
			}
		}
		return verbs;
	}

	public Verb getVerbArgumentNPs(int index, String verb, String sentence) {
		Verb v = new Verb(verb);
		HashMap<String, List<String>> argumentToText = new HashMap<String, List<String>>();
		index = index + 4;

		for (int i = 0; i < lineArr.length; i++) {
			try {
				String[] lineTokens = lineArr[i].trim().split("\\s+");
				String token = lineTokens[0].trim();
				String pos = lineTokens[1].trim();
				String value = lineTokens[index].trim();

				if (value.equals("O")) {
					continue;
				} else if (value.startsWith("S-") && !value.contains("S-V")
						&& pos.contains("NN")) {
					String arg = value.split("S-")[1];
					ArrayList<String> arr_token = new ArrayList<String>();
					arr_token.add(token);
					argumentToText.put(arg, arr_token);
					// } else if(value.startsWith("B-") &&
					// Character.isDigit(value.charAt(value.length()-1))) {
				} else if (value.startsWith("B-")) {
					String arg = value.split("B-")[1];
					StringBuilder text = new StringBuilder();
					boolean flag = false;
					if (pos.contains("NN")) {
						text.append(token);
						flag = true;
					}
					while (!value.startsWith("E-")) {
						i++;
						lineTokens = lineArr[i].trim().split("\\s+");
						token = lineTokens[0].trim();
						pos = lineTokens[1].trim();
						value = lineTokens[index];
						if (pos.contains("NN")) {
							text.append(" " + token);
							flag = true;
						} else if (flag == true) {
							if (argumentToText.containsKey(arg))
								argumentToText.get(arg).add(
										text.toString().trim());
							else {
								ArrayList<String> nps = new ArrayList<String>();
								nps.add(text.toString().trim());
								argumentToText.put(arg, nps);
							}
							text = new StringBuilder();
							flag = false;
						}
					}

					if (value.startsWith("E-") && flag == true) {
						if (argumentToText.containsKey(arg))
							argumentToText.get(arg).add(text.toString().trim());
						else {
							ArrayList<String> nps = new ArrayList<String>();
							nps.add(text.toString().trim());
							argumentToText.put(arg, nps);
						}
						flag = false;
					}

					if (argumentToText.containsKey(arg))
						arg = arg + "-1";

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		v.setArgumentToNPs(argumentToText);
		return v;

	}

	// get the arguments of a SennaVerb
	public Verb getVerbArguments(int index, String verb, String sentence) {
		Verb v = new Verb(verb, index);
		HashMap<String, List<String>> argumentToText = new HashMap<String, List<String>>();
		index = index + 4;

		for (int i = 0; i < lineArr.length; i++) {
			try {
				String[] lineTokens = lineArr[i].trim().split("\\s+");
				String token = lineTokens[0].trim();
				String pos = lineTokens[1].trim();
				String value = lineTokens[index].trim();

				if (value.equals("O")) {
					continue;
				} else if (value.startsWith("S-") && value.contains("S-V")) { 
					v.setPosition(i);
				} else if (value.startsWith("S-") && !value.contains("S-V")
						&& pos.contains("NN")) {
					String arg = value.split("S-")[1];
					ArrayList<String> arr_token = new ArrayList<String>();
					arr_token.add(token);
					argumentToText.put(arg, arr_token);
				} else if (value.startsWith("B-")) {
					String arg = value.split("B-")[1];
					StringBuilder text = new StringBuilder();
					boolean flag = false;
					if (pos.contains("NN")) {
						text.append(token);
						flag = true;
					}
					while (!value.startsWith("E-")) {
						i++;
						lineTokens = lineArr[i].trim().split("\\s+");
						token = lineTokens[0].trim();
						pos = lineTokens[1].trim();
						value = lineTokens[index];
						if (pos.contains("NN")) {
							text.append(" " + token);
							flag = true;
						} else if (flag == true) {
							if (argumentToText.containsKey(arg))
								argumentToText.get(arg).add(
										text.toString().trim());
							else {
								ArrayList<String> nps = new ArrayList<String>();
								nps.add(text.toString().trim());
								argumentToText.put(arg, nps);
							}
							text = new StringBuilder();
							flag = false;
						}
					}

					if (value.startsWith("E-") && flag == true) {
						if (argumentToText.containsKey(arg))
							argumentToText.get(arg).add(text.toString().trim());
						else {
							ArrayList<String> nps = new ArrayList<String>();
							nps.add(text.toString().trim());
							argumentToText.put(arg, nps);
						}
						flag = false;
					}

					if (argumentToText.containsKey(arg))
						arg = arg + "-1";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		v.setArgumentToText(argumentToText);
		return v;

	}

	/**
	 * 
	 * @param sennaOutput
	 *            - senna output for a sentence
	 * @return
	 */
	public ArrayList<String> getNounPhrases(String sennaOutput) {
		// System.out.println(sennaOutput);
		ArrayList<String> nounPhrases = new ArrayList<String>();
		String[] lineArr = sennaOutput.split("\n");

		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> posTags = new ArrayList<String>();
		ArrayList<String> chunkerTokens = new ArrayList<String>();
		try {
			for (String line : lineArr) {
				String[] tokens = line.trim().split("[ \t]+");
				if (tokens.length < 3)
					continue;
				words.add(tokens[0].trim());
				posTags.add(tokens[1].trim());
				chunkerTokens.add(tokens[2].trim());
			}

			boolean flag = false;
			StringBuffer npBuf = new StringBuffer();
			for (int i = 0; i < posTags.size(); i++) {
				if (posTags.get(i).startsWith("NN")
						|| posTags.get(i).startsWith("JJ")) {
					flag = true;
					npBuf.append(words.get(i) + " ");
				} else if (flag == true) {
					flag = false;
					nounPhrases.add(npBuf.toString().trim().toLowerCase());
					npBuf = new StringBuffer();
				}
			}
			if (flag == true)
				nounPhrases.add(npBuf.toString().trim().toLowerCase());

			// for (int i = 0; i < chunkerTokens.size(); i++) {
			// if (chunkerTokens.get(i).equalsIgnoreCase("s-np")) {
			// nounPhrases.add(words.get(i).toLowerCase());
			// } else if (chunkerTokens.get(i).equalsIgnoreCase("b-np")) {
			// String np = "";
			// while (!chunkerTokens.get(i).equalsIgnoreCase("e-np")) {
			// //System.out.println(chunkerTokens.get(i));
			// if (posTags.get(i).contains("NN")
			// || posTags.get(i).contains("JJ"))
			// np += (words.get(i) + " ");
			// i++;
			// }
			// np += (words.get(i) + " ");
			// nounPhrases.add(np.trim().toLowerCase());
			// }
			// }
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return nounPhrases;
	}
}
