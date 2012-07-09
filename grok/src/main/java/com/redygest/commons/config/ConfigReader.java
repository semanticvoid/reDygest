package com.redygest.commons.config;

import java.io.File;

import org.ini4j.Wini;

/**
 * Class to read configuration
 * 
 */
public class ConfigReader {

	private static final String CONFIG_FILE = "/Users/tejaswi/Documents/workspace/reDygest/grok/conf/grok.ini";

	private static final String EXTRACTOR_SECTION = "extractor";
	private static final String SENNA_PATH_KEY = "sennadir";
	private static final String SENNA_EXEC_KEY = "sennaexec";
	private static final String SENTINET_PATH_KEY = "sentinet";
	private static final String PCFG_PATH_KEY = "pcfg";
	private static final String EXTRACTOR_LIST_KEY = "extractors";
	private static final String AWS_CRED_PATH_KEY = "awscred";
	private static final String WORDNET_DICT_PATH_KEY = "wordnetdict";
	private static final String NER_CLASSIFIER_KEY = "nerclassifier";
	private static final String LANGDETECT_PROFILES_PATH_KEY = "langdetectprofiles";

	private static final String FACOPFILTER_SECTION = "facopfilter";
	private static final String FACOP_THRESHOLD_KEY = "threshold";
	private static final String FACOP_MODEL_KEY = "model";

	private static final String ENTITYFILTER_SECTION = "entityfilter";
	private static final String FILTER_LIST_KEY = "filters";
	private static final String MIN_LENGTH_KEY = "minlength";
	private static final String MAX_LENGTH_KEY = "maxlength";
	private static final String PERCENTILE_KEY = "percentile";

	private static ConfigReader instance = null;

	private final Wini ini;

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

	public String getSennaExec() {
		return ini.get(EXTRACTOR_SECTION, SENNA_EXEC_KEY);
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

	public String getLangProfilesPath() {
		return ini.get(EXTRACTOR_SECTION, LANGDETECT_PROFILES_PATH_KEY);
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

	public String getFacOpModel() {
		return ini.get(FACOPFILTER_SECTION, FACOP_MODEL_KEY);
	}

	public double getFacOpThreshold() {
		try {
			return Double.valueOf(ini.get(FACOPFILTER_SECTION,
					FACOP_THRESHOLD_KEY));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}

		return 1.0; // default
	}

	public String[] getEntityFilters(){
		String str = ini.get(ENTITYFILTER_SECTION, FILTER_LIST_KEY);
		if(str!=null){
			String[] list = str.split(",");
			return list;
		}
		throw new RuntimeException("No filters provided");
	}
	
	public int getEntityMinimumLength() {
		try {
			return Integer.valueOf(ini
					.get(ENTITYFILTER_SECTION, MIN_LENGTH_KEY));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 2;
	}

	public int getEntityMaximumLength() {
		try {
			return Integer.valueOf(ini
					.get(ENTITYFILTER_SECTION, MAX_LENGTH_KEY));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 4;
	}

	public double getPercentileThreshold() {
		try {
			return Double
					.valueOf(ini.get(ENTITYFILTER_SECTION, PERCENTILE_KEY));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 30.0;
	}
}
