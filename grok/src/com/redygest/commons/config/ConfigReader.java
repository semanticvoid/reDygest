package com.redygest.commons.config;

import java.io.File;

import org.ini4j.Wini;

/**
 * Class to read configuration
 *
 */
public class ConfigReader {

	private static final String CONFIG_FILE = "/home/semanticvoid/projects/redygest/grok/conf/grok.ini";
	
	private static final String EXTRACTOR_SECTION = "extractor";
	private static final String SENNA_PATH_KEY= "senna";
	private static final String SENTINET_PATH_KEY= "sentinet";
	private static final String PCFG_PATH_KEY= "pcfg";

	
	private static ConfigReader instance = null;
	
	private Wini ini;
	
	private ConfigReader() throws Exception {
		ini = new Wini(new File(CONFIG_FILE));
	}
	
	public static synchronized ConfigReader getInstance() {
		if(instance == null) {
			try {
				instance = new ConfigReader();
			} catch(Exception e) {
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
}