package com.redygest.commons.db.graph;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.commands.Query;
import org.neo4j.cypher.parser.CypherParser;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import scala.collection.Iterator;

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
		AutoIndexer nodeAutoIndexer = db.index().getNodeAutoIndexer();
		nodeAutoIndexer.startAutoIndexingProperty(NodeProperty.NAME.toString());
		nodeAutoIndexer.setEnabled(true);
		AutoIndexer relAutoIndexer = db.index().getRelationshipAutoIndexer();
//		nodeAutoIndexer.startAutoIndexingProperty(NodeProperty.NAME.toString());
		relAutoIndexer.setEnabled(true);
//		System.out.println("NODE INDEX:\t" + nodeAutoIndexer.getAutoIndex().getName());
//		Set<String> keys = nodeAutoIndexer.getAutoIndexedProperties();
//		if(keys != null) {
//			System.out.println(keys.iterator().next());
//		}
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
			node.setProperty(NodeProperty.ID.toString(), String.valueOf(node.getId()));
			tx.success();
			return node;
		} catch(Exception e)  {
			return null;
		} finally {
			tx.finish();
		}
	}
	
	public Relationship createRelationship(Node n1, Node n2, RelationshipType t) {
		Transaction tx = db.beginTx();
		try {
			Relationship r = n1.createRelationshipTo(n2, t);
			tx.success();
			return r;
		} catch(Exception e)  {
			return null;
		} finally {
			tx.finish();
		}
	}
	
	public boolean setRelationshipProperty(Relationship r, String key, String value) {
		Transaction tx = db.beginTx();
		try {
			r.setProperty(key, value);
			tx.success();
			return true;
		} catch(Exception e)  {
			return false;
		} finally {
			tx.finish();
		}
	}
	
	public Iterator<Object> query(String queryStr) {
		Query query;
		try {
			query = parser.parse(queryStr);
			ExecutionResult result = engine.execute( query );
			scala.collection.Iterator<Object> n_column = result.columnAs("q");
			return n_column;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return null;
	}
	
//	public Iterator<Object> queryRelationship(String queryStr) {
//		Query query;
//		try {
//			query = parser.parse(queryStr);
//			ExecutionResult result = engine.execute( query );
//			Iterator<Object> n_column = result.columnAs("r");
//			return n_column;
//		} catch (SyntaxError e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
	
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
