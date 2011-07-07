package com.redygest.grok.knowledge.graph;

import java.util.HashMap;

/**
 *  Relation
 *
 */
public class Relation extends HashMap<RelationProperty, String> {

	public static enum Relationship {
		RELATED_TO;
	}
	
	private Node n1, n2;
	
	public Relation(Relationship type, Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
		this.put(RelationProperty.TYPE, type.toString());
	}
	
	public Node getNode1() {
		return n1;
	}
	
	public Node getNode2() {
		return n2;
	}
}
