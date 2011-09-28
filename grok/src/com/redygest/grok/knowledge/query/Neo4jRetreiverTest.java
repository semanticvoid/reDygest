package com.redygest.grok.knowledge.query;

import java.util.List;

import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.Node;
import com.redygest.grok.knowledge.graph.NodeProperty;
import com.redygest.grok.knowledge.graph.Relation;
import com.redygest.grok.knowledge.graph.RelationProperty;
import com.redygest.grok.knowledge.graph.RepresentationFactory;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.Relation.Relationship;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;
import com.redygest.grok.knowledge.query.datatype.Entity;
import com.redygest.grok.knowledge.query.datatype.HeadWordEntity;

import junit.framework.TestCase;

public class Neo4jRetreiverTest extends TestCase{
	
	IRepresentation repr;

	protected void setUp() {
		String name = String.valueOf(new Double(Math.random()).hashCode());
		repr = RepresentationFactory.getInstance().produceRepresentation(name, RepresentationType.NEO4J);
		
		Node n1 = new Node(NodeType.NOUN, "clinton");
		Node n2 = new Node(NodeType.NOUN, "hospital");
		Node n3 = new Node(NodeType.VERB, "sent");
		Node n4 = new Node(NodeType.EVENT, "ev");
		
		if(!repr.addNode(n1) || !repr.addNode(n2) || !repr.addNode(n3) || !repr.addNode(n4)) {
			fail();
		} else {
			Relation r1 = new Relation(Relationship.A0, n4, n1);
			Relation r2 = new Relation(Relationship.A1, n4, n2);
			Relation r3 = new Relation(Relationship.ACTION, n4, n3);
			
			if(!repr.addRelation(r1) || !repr.addRelation(r2) || !repr.addRelation(r3)) {
				fail();
			} 
			return;
		}
	}
	
	public void testCypherQuery(){
		Neo4jRetriever nr = new Neo4jRetriever(repr);
		Entity e1 = new HeadWordEntity("sent");
		Entity e2 = new HeadWordEntity("hospital");
		String query = nr.getCypherQuery(e1, null, e2);
		List<Node> nodes = repr.getNodes(query);
		for(Node n : nodes){
			assertEquals("clinton", n.get(NodeProperty.NAME));
		}
		
	}

	
}
