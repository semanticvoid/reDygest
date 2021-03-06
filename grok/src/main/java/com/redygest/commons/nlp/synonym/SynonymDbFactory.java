/**
 * 
 */
package com.redygest.commons.nlp.synonym;


public class SynonymDbFactory {

	// singleton instance
	private static SynonymDbFactory instance = null;
	
	// private constructor
	private SynonymDbFactory() {
	}
	
	public static synchronized SynonymDbFactory getInstance() {
		if(instance == null) {
			instance = new SynonymDbFactory();
		}
		
		return instance;
	}
	
	/**
	 * Produce synonym db of given type
	 * @param type
	 * @return
	 */
	public ISynonymDb produce(SynonymDbType type) {
		switch (type) {
			case NOUNWIKIPEDIAREDIRECT:
				return new NounSynonymWikipediaRedirects();
				
			default:
				break;
		}
		
		return null;
	}
}
