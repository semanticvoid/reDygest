package com.redygest.commons.nlp;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.redygest.commons.config.ConfigReader;

/**
 * Language Detector Wrapper Class
 * 
 */
public class LanguageDetector {

	private Detector detector = null;

	/**
	 * Constructor
	 * 
	 * @throws LangDetectException
	 */
	public LanguageDetector() throws LangDetectException {
		try {
			ConfigReader config = ConfigReader.getInstance();
			DetectorFactory.loadProfile(config.getLangProfilesPath());
		} catch (LangDetectException e) {
			throw e;
		}
	}

	/**
	 * Detect language
	 * 
	 * @param text
	 * @return language code
	 */
	public String detect(String text) throws LangDetectException {
		try {
			detector = DetectorFactory.create();
			detector.append(text);
			return detector.detect();
		} catch (LangDetectException e) {
			throw e;
		}
	}

}
