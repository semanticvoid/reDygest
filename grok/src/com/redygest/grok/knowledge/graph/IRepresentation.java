package com.redygest.grok.knowledge.graph;

import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;

/**
 * 
 * knowledge representation interface
 *
 */
public interface IRepresentation {
	
	/**
	 * Get the representation type
	 * @return	RepresentationType
	 */
	public RepresentationType getType();

	/**
	 * Add node to representation
	 * @param node
	 * @return true on success false otherwise
	 */
	public boolean addNode(Node node);
	
	/**
	 * Update node in representation
	 * @param node
	 * @return true on success false otherwise
	 */
	public boolean updateNode(Node node);
	
	/**
	 * Get Node that matches query string
	 * @param query
	 * @return the Node or null
	 */
	public Node getNode(String query);
	
	/**
	 * Get Node with id
	 * @param id
	 * @return the Node or null
	 */
	public Node getNodeWithId(String id);
	
	/**
	 * Get Node with name
	 * @param name
	 * @return the Node or null
	 */
	public Node getNodeWithName(String name);
	
	/**
	 * Add relation to representation
	 * @param r
	 * @return true on success false otherwise
	 */
	public boolean addRelation(Relation r);
	
	/**
	 * Get Relation that matches query string
	 * @param query
	 * @return the Relation or null
	 */
	public Relation getRelation(String query);
	
	/**
	 * Update relation in representation
	 * @param r
	 * @return true on success false otherwise
	 */
	public boolean updateRelation(Relation r);

}

