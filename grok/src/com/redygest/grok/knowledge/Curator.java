package com.redygest.grok.knowledge;

import java.util.List;

import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.RepresentationFactory;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;

/**
 * Class which organizes knowledge events/facts
 *
 */
public class Curator {

	// the internal representation
	IRepresentation kmodel;
	
	/**
	 * Constructor
	 */
	public Curator() {
		kmodel = RepresentationFactory.getInstance().produceRepresentation(RepresentationType.NEO4J);
	}
	
	/**
	 * Add a set of logically grouped events (e.g. events associated with a sentence)
	 * @param events
	 * @return true on sucess false otherwise
	 */
	public boolean addEvents(List<Event> events) {
		return false;
	}
	
	/**
	 * Get the knowledge model
	 * @return the IRepresentation
	 */
	public IRepresentation getModel() {
		return this.kmodel;
	}
	
}
