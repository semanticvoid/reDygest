/**
 * 
 */
package com.redygest.grok.knowledge.graph;

import com.redygest.commons.db.graph.Neo4jGraphDb;

/**
 *	Neo4j backing store knowledge representation
 */
public class Neo4jRepresentation implements IRepresentation {
	
	private Neo4jGraphDb db;
	
	public Neo4jRepresentation() {
		this.db = Neo4jGraphDb.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.redygest.grok.knowledge.graph.IGraphRepresentation#addNode(com.redygest.grok.knowledge.graph.Node)
	 */
	@Override
	public boolean addNode(Node node) {
		org.neo4j.graphdb.Node n = db.createNode();
		
		for(NodeProperty key : node.keySet()) {
			n.setProperty(key.toString(), node.get(key));
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.redygest.grok.knowledge.graph.IGraphRepresentation#addRelation(com.redygest.grok.knowledge.graph.Relation)
	 */
	@Override
	public boolean addRelation(Relation r) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.redygest.grok.knowledge.graph.IGraphRepresentation#updateNode(com.redygest.grok.knowledge.graph.Node)
	 */
	@Override
	public boolean updateNode(Node node) {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.redygest.grok.knowledge.graph.IGraphRepresentation#updateRelation(com.redygest.grok.knowledge.graph.Relation)
	 */
	@Override
	public boolean updateRelation(Relation r) {
		// TODO Auto-generated method stub
		return false;
	}

}
