package com.redygest.grok.knowledge;

import java.util.List;
import java.util.Set;

import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.Node;
import com.redygest.grok.knowledge.graph.Relation;
import com.redygest.grok.knowledge.graph.RepresentationFactory;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.Relation.Relationship;
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
		if(events != null) {
			// add node for sentence
			Node sentence = new Node(NodeType.SENTENCE);
			kmodel.addNode(sentence);
			for(Event e : events) {
				// add node for event
				Node event = new Node(NodeType.EVENT);
				kmodel.addNode(event);
				
				// attach event to sentence
				Relation rel = new Relation(Relationship.EVENT, sentence, event);
				kmodel.addRelation(rel);
				
				// iterate over arguments and create/add entities
				Set<String> keys = e.getArgumentToText().keySet();
				if(keys != null) {
					for(String key : keys) {
						
					}
				}
			}
		}
		
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
