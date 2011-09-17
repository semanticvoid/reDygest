/**
 * 
 */
package com.redygest.grok.knowledge.graph;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.NotFoundException;

import scala.collection.Iterator;

import com.redygest.commons.db.graph.Neo4jGraphDb;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.Relation.Relationship;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;

/**
 * Neo4j backing store knowledge representation
 */
public class Neo4jRepresentation implements IRepresentation {

	private Neo4jGraphDb db;
	private Node root;

	public Neo4jRepresentation(String name) {
		try {
			this.db = new Neo4jGraphDb(name);
			root = new Node(NodeType.ROOT);
			this.addNode(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.knowledge.graph.IGraphRepresentation#addNode(com.redygest
	 * .grok.knowledge.graph.Node)
	 */
	@Override
	public boolean addNode(Node node) {
		org.neo4j.graphdb.Node n = db.createNode();

		for (NodeProperty key : node.keySet()) {
			db.setNodeProperty(n, key.toString(), node.get(key));
		}

		node.put(NodeProperty.ID, String.valueOf(n.getId()));

		addRelation(new Relation(Relationship.ROOT, root, node));

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.knowledge.graph.IGraphRepresentation#addRelation(com
	 * .redygest.grok.knowledge.graph.Relation)
	 */
	@Override
	public boolean addRelation(Relation r) {
		org.neo4j.graphdb.Node neo4jNode1 = null;
		org.neo4j.graphdb.Node neo4jNode2 = null;

		Node node1 = r.getNode1();
		Node node2 = r.getNode2();

		StringBuffer query = new StringBuffer("start q=(");
		query.append(node1.get(NodeProperty.ID));
		query.append(") return q");

		Iterator<Object> nodes = db.query(query.toString());
		if (nodes != null) {
			neo4jNode1 = (org.neo4j.graphdb.Node) nodes.next();
		}

		query = new StringBuffer("start q=(");
		query.append(node2.get(NodeProperty.ID));
		query.append(") return q");

		nodes = db.query(query.toString());
		if (nodes != null) {
			neo4jNode2 = (org.neo4j.graphdb.Node) nodes.next();
		}

		if (neo4jNode1 != null && neo4jNode2 != null) {
			org.neo4j.graphdb.Relationship relationship = db
					.createRelationship(neo4jNode1, neo4jNode2, Relationship
							.getType((r.get(RelationProperty.TYPE))));
			r.put(RelationProperty.ID, String.valueOf(relationship.getId()));
			// add properties
			for (RelationProperty prop : r.keySet()) {
				db.setRelationshipProperty(relationship, prop.toString(),
						r.get(prop));
			}

			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.knowledge.graph.IGraphRepresentation#updateNode(com
	 * .redygest.grok.knowledge.graph.Node)
	 */
	@Override
	public boolean updateNode(Node node) {
		// query the node
		if (node != null) {
			StringBuffer query = new StringBuffer("start q=(");
			query.append(node.get(NodeProperty.ID));
			query.append(") return q");
			Iterator<Object> nodes = db.query(query.toString());

			if (nodes != null) {
				org.neo4j.graphdb.Node n = (org.neo4j.graphdb.Node) nodes
						.next();
				for (NodeProperty prop : node.keySet()) {
					db.setNodeProperty(n, prop.toString(), node.get(prop));
				}

				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.redygest.grok.knowledge.graph.IGraphRepresentation#updateRelation
	 * (com.redygest.grok.knowledge.graph.Relation)
	 */
	@Override
	public boolean updateRelation(Relation r) {
		// query the relation
		if (r != null) {
			StringBuffer query = new StringBuffer("start n=(");
			query.append(r.getNode1().get(NodeProperty.ID));
			query.append(") match (n)-[q, :");
			query.append(r.get(RelationProperty.TYPE));
			// query.append("[" + r.get(RelationProperty.ID) + "]");
			query.append("]-() return q");
			Iterator<Object> relations = db.query(query.toString());

			if (relations != null) {
				org.neo4j.graphdb.Relationship rel = null;
				while (relations.hasNext()) {
					rel = (org.neo4j.graphdb.Relationship) relations.next();
					if (rel != null
							&& rel.hasProperty(RelationProperty.ID.toString())
							&& rel.getProperty(RelationProperty.ID.toString())
									.equals(r.get(RelationProperty.ID))) {
						for (RelationProperty prop : r.keySet()) {
							db.setRelationshipProperty(rel, prop.toString(),
									r.get(prop));
						}
					}
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public List<Node> getNodes(String query) {
		List<Node> results = new ArrayList<Node>();

		// query the node
		Iterator<Object> nodes = db.query(query);

		if (nodes != null && !nodes.isEmpty()) {
			// get the first node match and create a Node
			// by filling it with existing properties
			org.neo4j.graphdb.Node n = null;
			while (nodes.hasNext()) {
				n = (org.neo4j.graphdb.Node) nodes.next();
				Node node = new Node(NodeType.getType((String) n
						.getProperty(NodeProperty.TYPE.toString())));
				for (NodeProperty prop : NodeProperty.values()) {
					String value = null;
					try {
						value = (String) n.getProperty(prop.toString());
					} catch (NotFoundException nfe) {
						continue;
					}
					if (value != null) {
						node.put(prop, value);
					}
				}
				results.add(node);
			}
		}

		return results;
	}

	@Override
	public Node getNodeWithId(String id) {
		StringBuffer query = new StringBuffer("start q=(");
		query.append(id);
		query.append(") return q");
		List<Node> nodes = getNodes(query.toString());
		if (nodes != null && nodes.size() >= 1) {
			return nodes.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Relation> getRelations(String query) {
		List<Relation> results = new ArrayList<Relation>();

		// query the relationship
		Iterator<Object> relatioships = db.query(query);

		if (relatioships != null && !relatioships.isEmpty()) {
			// get the first relationship match and create a Relation
			// by filling it with existing properties
			while (relatioships.hasNext()) {
				org.neo4j.graphdb.Relationship r = (org.neo4j.graphdb.Relationship) relatioships
						.next();
				org.neo4j.graphdb.Node sNode = r.getStartNode();
				org.neo4j.graphdb.Node eNode = r.getEndNode();
				Node n1 = getNodeWithId(String.valueOf(sNode.getId()));
				Node n2 = getNodeWithId(String.valueOf(eNode.getId()));
				if (n1 != null && n2 != null) {
					Relation relation = new Relation(
							Relationship.getType((String) r
									.getProperty(RelationProperty.TYPE
											.toString())), n1, n2);
					for (RelationProperty prop : RelationProperty.values()) {
						String value = null;
						try {
							value = (String) r.getProperty(prop.toString());
						} catch (NotFoundException nfe) {
							continue;
						}
						if (value != null) {
							relation.put(prop, value);
						}
					}

					results.add(relation);
				}
			}
		}

		return results;
	}

	@Override
	public Node getNodeWithName(String name) {
		StringBuffer query = new StringBuffer(
				"start q=(node_auto_index, \"NAME:");
		query.append(name);
		query.append("\") return q");
		List<Node> nodes = getNodes(query.toString());
		if (nodes != null && nodes.size() >= 1) {
			return nodes.get(0);
		} else {
			return null;
		}
	}

	@Override
	public RepresentationType getType() {
		return RepresentationType.NEO4J;
	}
}
