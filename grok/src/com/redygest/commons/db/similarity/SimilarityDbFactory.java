/**
 * 
 */
package com.redygest.commons.db.similarity;


public class SimilarityDbFactory {

	// singleton instance
	private static SimilarityDbFactory instance = null;
	
	// private constructor
	private SimilarityDbFactory() {
	}
	
	public static SimilarityDbFactory getInstance() {
		if(instance == null) {
			instance = new SimilarityDbFactory();
		}
		
		return instance;
	}
	
	/**
	 * Produce similarity db of given type
	 * @param type
	 * @return
	 */
	public ISimilarityDb produce(SimilarityDbType type) {
		switch (type) {
			case VERBSDB:
				return new VerbSimilaritySimpleDb();
				
			case NOUNSDB:
				return new NounSimilaritySimpleDb();
				
			default:
				break;
		}
		
		return null;
	}
}
