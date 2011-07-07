package com.redygest.grok.knowledge.graph;

/**
 * Knowledge Represenatation Factory
 *
 */
public class RepresentationFactory {

	public static enum RepresentationType {
		NEO4J;
	}
	
	private static RepresentationFactory instance = null;
	
	public static synchronized RepresentationFactory getInstance() {
		if(instance == null) {
			instance = new RepresentationFactory();
		}
		
		return instance;
	}
	
	private RepresentationFactory() {
	}
	
	public IRepresentation produceRepresentation(RepresentationType type) {
		if(type == RepresentationType.NEO4J) {
			return new Neo4jRepresentation();
		}
		
		return null;
	}
}
