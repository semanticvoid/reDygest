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
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.redygest.grok.knowledge.graph.NodeProperty;
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

	public static synchronized Neo4jGraphDb getInstance() {
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
		node.setProperty(NodeProperty.ID.toString(), node.getId());
		return node;
	}
	
	public void query(String queryStr) {
		Query query;
		try {
			query = parser.parse(queryStr);
			ExecutionResult result = engine.execute( query );
			System.out.println();
		} catch (SyntaxError e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		db.shutdown();
	}
	
	protected void finalize() {
		if(db != null) {
			try {
				close();
			} catch(Exception e) {
				// do nothing
			}
		}
	}
}
