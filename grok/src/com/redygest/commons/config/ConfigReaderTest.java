package com.redygest.commons.config;

import junit.framework.TestCase;

public class ConfigReaderTest extends TestCase {
	
	public void testConfigReader() {
		ConfigReader reader = ConfigReader.getInstance();
		assertNotNull(reader.getExtractorsList());
		assertNotNull(reader.getPCFGPath());
		assertNotNull(reader.getSennaPath());
		assertNotNull(reader.getSentiWordnetPath());
		assertNotNull(reader.getAWSCredentialsPath());
		assertNotNull(reader.getWordNetDictPath());
	}
}
