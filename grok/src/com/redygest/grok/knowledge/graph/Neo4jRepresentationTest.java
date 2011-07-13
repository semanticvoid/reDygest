package com.redygest.grok.knowledge.graph;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.Features;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;

public class Neo4jRepresentationTest extends TestCase {
	
	IRepresentation repr;
	
	protected void setUp() {
		repr = RepresentationFactory.getInstance().produceRepresentation(RepresentationType.NEO4J);
	}

	protected void tearDown() {
		// do nothing
	}

	public void testAddNode() {
		Node n = new Node(NodeType.NOUN, "clinton");
		if(!repr.addNode(n)) {
			fail();
		} else {
			assertEquals("1", n.get(NodeProperty.ID));
			return;
		}
	}
	
	public void testUpdateNode() {
		Node n = new Node(NodeType.NOUN, "clinton");
		if(!repr.addNode(n)) {
			fail();
		} else {
			n.put(NodeProperty.NAME, "clinton1");
			repr.updateNode(n);
			Node n1 = repr.getNodeWithId(n.get(NodeProperty.ID));
			assertEquals("clinton1", n1.get(NodeProperty.NAME));
			return;
		}
	}
	
	public void testGetNode() {
		Node n1 = new Node(NodeType.NOUN, "clinton");
		Node n2 = new Node(NodeType.NOUN, "clinton1");
		if(!repr.addNode(n1) || !repr.addNode(n2) ) {
			fail();
		} else {
			Node n = repr.getNode("start q=(1,2) where (q.NAME = \"clinton\") return q");
			assertEquals("clinton", n.get(NodeProperty.NAME));
			return;
		}
	}
}
