package com.redygest.commons.config;

import java.io.File;

import org.ini4j.Wini;

/**
 * Class to read configuration
 * 
 */
public class ConfigReader {

	private static final String CONFIG_FILE = "/Users/semanticvoid/projects/reDygest/grok/conf/grok.ini";

	private static final String EXTRACTOR_SECTION = "extractor";
	private static final String SENNA_PATH_KEY = "senna";
	private static final String SENTINET_PATH_KEY = "sentinet";
	private static final String PCFG_PATH_KEY = "pcfg";
	private static final String EXTRACTOR_LIST_KEY = "extractors";
	private static final String AWS_CRED_PATH_KEY = "awscred";
	private static final String WORDNET_DICT_PATH_KEY = "wordnetdict";
	private static final String NER_CLASSIFIER_KEY = "nerclassifier";

	private static ConfigReader instance = null;

	private Wini ini;

	private ConfigReader() throws Exception {
		ini = new Wini(new File(CONFIG_FILE));
	}

	public static synchronized ConfigReader getInstance() {
		if (instance == null) {
			try {
				instance = new ConfigReader();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	public String getSennaPath() {
		return ini.get(EXTRACTOR_SECTION, SENNA_PATH_KEY);
	}

	public String getSentiWordnetPath() {
		return ini.get(EXTRACTOR_SECTION, SENTINET_PATH_KEY);
	}

	public String getPCFGPath() {
		return ini.get(EXTRACTOR_SECTION, PCFG_PATH_KEY);
	}

	public String getWordNetDictPath() {
		return ini.get(EXTRACTOR_SECTION, WORDNET_DICT_PATH_KEY);
	}
	
	public String getNERClassifierPath() {
		return ini.get(EXTRACTOR_SECTION, NER_CLASSIFIER_KEY);
	}

	public String getAWSCredentialsPath() {
		return ini.get(EXTRACTOR_SECTION, AWS_CRED_PATH_KEY);
	}

	public String[] getExtractorsList() {
		String str = ini.get(EXTRACTOR_SECTION, EXTRACTOR_LIST_KEY);
		if (str != null) {
			String[] list = str.split(",");
			return list;
		}
		throw new RuntimeException("No extractors provided");
	}
}
