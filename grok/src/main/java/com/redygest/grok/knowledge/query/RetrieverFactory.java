/**
 * 
 */
package com.redygest.grok.knowledge.query;

import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;

/**
 * Retriever Factory
 */
public class RetrieverFactory {

	private static RetrieverFactory instance = null;

	public static synchronized RetrieverFactory getInstance() {
		if (instance == null) {
			instance = new RetrieverFactory();
		}

		return instance;
	}
	
	private RetrieverFactory() {
	}

	public IRetriever produceRetriever(IRepresentation repr) {
		if(repr.getType() == RepresentationType.NEO4J) {
			return new Neo4jRetriever(repr);
		}
		
		return null;
	}
}
