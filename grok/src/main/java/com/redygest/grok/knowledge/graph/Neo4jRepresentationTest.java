package com.redygest.grok.knowledge.graph;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Tweet;
import com.redygest.grok.features.computation.FeatureVectorCollection;
import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.DataVariable;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.Relation.Relationship;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;

public class Neo4jRepresentationTest extends TestCase {
	
	IRepresentation repr;
	
	protected void setUp() {
		String name = String.valueOf(new Double(Math.random()).hashCode());
		repr = RepresentationFactory.getInstance().produceRepresentation(name, RepresentationType.NEO4J);
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
			List<Node> nodes = repr.getNodes("start q=(1,2) where (q.NAME = \"clinton\") return q");
			if(nodes != null && nodes.size() >= 1) {
				Node n = nodes.get(0);
				assertEquals("clinton", n.get(NodeProperty.NAME));
				return;
			} else {
				fail();
			}
		}
	}
	
	public void testAddRelation() {
		Node n1 = new Node(NodeType.NOUN, "node 1");
		Node n2 = new Node(NodeType.NOUN, "node 2");
		if(!repr.addNode(n1) || !repr.addNode(n2) ) {
			fail();
		} else {
			Relation r = new Relation(Relationship.LOC, n1, n2);
			if(!repr.addRelation(r)) {
				fail();
			} else {
				assertEquals("0", r.get(RelationProperty.ID));
			}
			return;
		}
	}
	
	public void testGetRelation() {
		Node n1 = new Node(NodeType.NOUN, "node 1");
		Node n2 = new Node(NodeType.NOUN, "node 2");
		if(!repr.addNode(n1) || !repr.addNode(n2) ) {
			fail();
		} else {
			Relation r = new Relation(Relationship.LOC, n1, n2);
			if(!repr.addRelation(r)) {
				fail();
			}
			
			List<Relation> relations = repr.getRelations("start n=(1) match (n)-[q]->() return q");
			if(relations != null && relations.size() >= 1) {
				r = relations.get(0);
				assertEquals(Relation.Relationship.LOC.toString(), r.get(RelationProperty.TYPE));
				return;
			}
		}
		
		fail();
	}
	
	public void testUpdateRelation() {
		Node n1 = new Node(NodeType.NOUN, "node 1");
		Node n2 = new Node(NodeType.NOUN, "node 2");
		if(!repr.addNode(n1) || !repr.addNode(n2) ) {
			fail();
		} else {
			Relation r = new Relation(Relationship.LOC, n1, n2);
			r.put(RelationProperty.COUNT, "1");
			if(!repr.addRelation(r)) {
				fail();
			}
			
			r.put(RelationProperty.COUNT, "2");
			repr.updateRelation(r);
			
			List<Relation> relations = repr.getRelations("start n=(1) match (n)-[q]->() return q");
			if(relations != null && relations.size() >= 1) {
				r = relations.get(0);
				assertEquals("2", r.get(RelationProperty.COUNT));
				return;
			}
		}
		
		fail();
	}
	
	public void testGetNodeWithName() {
		Node n1 = new Node(NodeType.NOUN, "clinton");
		if(!repr.addNode(n1)) {
			fail();
		} else {
			Node n = repr.getNodeWithName("clinton");
			assertEquals("clinton", n.get(NodeProperty.NAME));
			return;
		}
		
		fail();
	}
}
