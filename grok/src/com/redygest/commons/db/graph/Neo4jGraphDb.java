package com.redygest.commons.db.graph;

import java.net.URI;
import java.util.Iterator;

import javax.ws.rs.core.MediaType;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.SyntaxError;
import org.neo4j.cypher.commands.Query;
import org.neo4j.cypher.parser.CypherParser;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Neo4j graph db interface
 * 
 */
public class Neo4jGraphDb {

	private static Neo4jGraphDb instance = null;
	
	private GraphDatabaseService db = null;
	private CypherParser parser = null;
	private ExecutionEngine engine = null;
	
	private Neo4jGraphDb() throws Exception {
		db = new EmbeddedGraphDatabase("var/graphDb");
		parser = new CypherParser();
		engine = new ExecutionEngine(db);
	}

	public static Neo4jGraphDb getInstance() {
		if (instance == null) {
			try {
				instance = new Neo4jGraphDb();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	public Node createNode() {
		Node node = db.createNode();
		node.setProperty("id", node.getId());
		return node;
	}
	
	public void query() {
		Query query;
		try {
			query = parser.parse( "start n=(0) where 1=1 return n" );
			ExecutionResult result = engine.execute( query );
			System.out.println();
		} catch (SyntaxError e) {
			e.printStackTrace();
		}
		 

	}
	
	public void close() {
		db.shutdown();
	}

	public static void main(String[] args) {
		Neo4jGraphDb db = Neo4jGraphDb.getInstance();
//		long id = db.createNode();
//		System.out.println(id);
//		db.addPropertyToNode(id, "name", "node" + id);
		db.query();
		db.close();
	}

}
