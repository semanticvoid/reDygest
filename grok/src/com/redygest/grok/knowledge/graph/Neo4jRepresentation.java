/**
 * 
 */
package com.redygest.grok.knowledge.graph;

import java.util.Iterator;

import org.neo4j.graphdb.RelationshipType;
import org.neo4j.kernel.impl.cache.StrongReferenceCache;

import com.redygest.commons.db.graph.Neo4jGraphDb;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.Relation.Relationship;

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
		org.neo4j.graphdb.Node neo4jNode1 = null;
		org.neo4j.graphdb.Node neo4jNode2 = null;
		
		Node node1 = r.getNode1();
		Node node2 = r.getNode2();
		
		StringBuffer query = new StringBuffer("start n=(");
		query.append(node1.get(NodeProperty.ID));
		query.append(") return n");
		
		Iterator<org.neo4j.graphdb.Node> nodes = db.queryNode(query.toString());
		if(nodes != null) {
			neo4jNode1 = nodes.next();
		}
		
		query = new StringBuffer("start n=(");
		query.append(node2.get(NodeProperty.ID));
		query.append(") return n");
		
		nodes = db.queryNode(query.toString());
		if(nodes != null) {
			neo4jNode2 = nodes.next();
		}
		
		if(neo4jNode1 != null && neo4jNode2 != null) {
			neo4jNode1.createRelationshipTo(neo4jNode2, Relationship.getType((r.get(RelationProperty.TYPE))));
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.redygest.grok.knowledge.graph.IGraphRepresentation#updateNode(com.redygest.grok.knowledge.graph.Node)
	 */
	@Override
	public boolean updateNode(Node node) {
		// query the node
		StringBuffer query = new StringBuffer("start n=(");
		query.append(node.get(NodeProperty.ID));
		query.append(") return n");
		Iterator<org.neo4j.graphdb.Node> nodes = db.queryNode(query.toString());
		
		if(nodes != null) {
			org.neo4j.graphdb.Node n = nodes.next();
			for(NodeProperty prop : node.keySet()) {
				n.setProperty(prop.toString(), node.get(prop));
			}
			
			return true;
		}
		
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

	@Override
	public Node getNode(String query) {
		// query the node
		Iterator<org.neo4j.graphdb.Node> nodes = db.queryNode(query);
		
		if(nodes != null) {
			// get the first node match and create a Node
			// by filling it with existing properties
			org.neo4j.graphdb.Node n = nodes.next();
			Node node = new Node(NodeType.getType((String) n.getProperty(NodeProperty.TYPE.toString())));
			for(NodeProperty prop : NodeProperty.values()) {
				String value = (String) n.getProperty(prop.toString());
				if(value != null) {
					node.put(prop, value);
				}
			}
			
			return node;
		}
		
		return null;
	}

}
