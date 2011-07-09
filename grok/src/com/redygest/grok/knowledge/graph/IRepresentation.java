package com.redygest.grok.knowledge.graph;

/**
 * 
 * knowledge representation interface
 *
 */
public interface IRepresentation {

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
	 * @return the Node
	 */
	public Node getNode(String query);
	
	/**
	 * Add relation to representation
	 * @param r
	 * @return true on success false otherwise
	 */
	public boolean addRelation(Relation r);
	
	/**
	 * Update relation in representation
	 * @param r
	 * @return true on success false otherwise
	 */
	public boolean updateRelation(Relation r);

}

