package com.redygest.commons.db.similarity;

import junit.framework.TestCase;

public class VerbSimilatritySimpleDbTest extends TestCase {
	
	ISimilarityDb db;
	
	public void setUp() {
		db = SimilarityDbFactory.getInstance().produce(SimilarityDbType.VERBSDB);
	}
	
	public void testGetSimilarity() {
		assertNotNull(db);
		assertNotNull(db.getSimilarity("increase", "aah"));
		assertEquals(0.065991, db.getSimilarity("increase", "aah").get("aah#v#1~increase#v#2"));
	}
}
