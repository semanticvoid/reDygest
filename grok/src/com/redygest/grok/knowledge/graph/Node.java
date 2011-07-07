/**
 * 
 */
package com.redygest.grok.knowledge.graph;

import java.util.HashMap;

/**
 *	Node
 */
public class Node extends HashMap<NodeProperty, String> {

	public static enum NodeType {
		NOUN;
	}
		
	public Node(NodeType type) {
		super();
		this.put(NodeProperty.TYPE, type.toString());
	}
	
	public Node(NodeType type, String name) {
		super();
		this.put(NodeProperty.TYPE, type.toString());
		this.put(NodeProperty.NAME, name);
	}
	
}
