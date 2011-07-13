package com.redygest.commons.db.graph;

import java.util.Iterator;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.SyntaxError;
import org.neo4j.cypher.commands.Query;
import org.neo4j.cypher.parser.CypherParser;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.redygest.grok.knowledge.graph.NodeProperty;

/**
 * Neo4j graph db interface
 * 
 */
public class Neo4jGraphDb {

	private static Neo4jGraphDb instance = null;
	
	private GraphDatabaseService db = null;
	private CypherParser parser = null;
	private ExecutionEngine engine = null;
	
	public Neo4jGraphDb(String dbName) throws Exception {
		db = new EmbeddedGraphDatabase("/tmp/var/" + dbName);
		parser = new CypherParser();
		engine = new ExecutionEngine(db);
	}

	public boolean setNodeProperty(Node n, String key, String value) {
		Transaction tx = db.beginTx();
		try {
			n.setProperty(key, value);
			tx.success();
			return true;
		} catch(Exception e)  {
			return false;
		} finally {
			tx.finish();
		}
	}
	
	public Node createNode() {
		Transaction tx = db.beginTx();
		try {
			Node node = db.createNode();
			node.setProperty(NodeProperty.ID.toString(), node.getId());
			tx.success();
			return node;
		} catch(Exception e)  {
			return null;
		} finally {
			tx.finish();
		}
	}
	
	public Iterator<Node> queryNode(String queryStr) {
		Query query;
		try {
			query = parser.parse(queryStr);
			ExecutionResult result = engine.execute( query );
			Iterator<Node> n_column = (Iterator<Node>) result.columnAs("n");
			return n_column;
		} catch (SyntaxError e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Iterator<Relationship> queryRelationship(String queryStr) {
		Query query;
		try {
			query = parser.parse(queryStr);
			ExecutionResult result = engine.execute( query );
			Iterator<Relationship> n_column = (Iterator<Relationship>) result.columnAs("r");
			return n_column;
		} catch (SyntaxError e) {
			e.printStackTrace();
		}
		
		return null;
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
