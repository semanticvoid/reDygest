package com.redygest.grok.knowledge.graph;

import java.util.HashMap;

import org.neo4j.graphdb.RelationshipType;

import com.redygest.grok.knowledge.graph.Node.NodeType;

/**
 *  Relation
 *
 */
public class Relation extends HashMap<RelationProperty, String> {

	public static enum Relationship implements RelationshipType {
		TMP, LOC, MNR, PNC, A0, A1, A2, EVENT, ACTION;
		
		public static Relationship getType(String str) {
			for(Relationship type : Relationship.values()) {
				if(type.toString().equalsIgnoreCase(str)) {
					return type;
				}
			}
			
			return null;
		}
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
