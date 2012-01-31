package com.redygest.commons.db.synonym;

import junit.framework.TestCase;

public class NounSynonymWikipediaRedirectsTest extends TestCase {
	
	ISynonymDb db;
	
	public void setUp() {
		db = SynonymDbFactory.getInstance().produce(SynonymDbType.NOUNWIKIPEDIAREDIRECT);
	}
	
	public void testGetSimilarity() {
		assertNotNull(db);
		assertNotNull(db.getSynonym("obama"));
		assertEquals("Barack_Obama", db.getSynonym("obama"));
	}
}
