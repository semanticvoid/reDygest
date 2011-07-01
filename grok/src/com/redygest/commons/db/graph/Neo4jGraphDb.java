package com.redygest.commons.db.graph;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Neo4j graph db interface
 * 
 */
public class Neo4jGraphDb {

	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	private static final String NODE_ENTRY_POINT_ROOT_URI = SERVER_ROOT_URI
			+ "node/";

	private static Neo4jGraphDb instance = null;

	private Neo4jGraphDb() throws Exception {
		WebResource resource = Client.create().resource(SERVER_ROOT_URI);
		ClientResponse response = resource.get(ClientResponse.class);
		if (response == null || response.getStatus() != 200) {
			throw new Exception("Error connecting to neo4j server");
		}
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

	public long createNode() {
		WebResource resource = Client.create().resource(
				NODE_ENTRY_POINT_ROOT_URI);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity("{}")
				.post(ClientResponse.class);
		if (response != null && response.getStatus() == 201) {
			URI u = response.getLocation();
			String s = u.toString();
			String[] tokens = s.split("/");
			long nodeId = Long.valueOf(tokens[tokens.length - 1]);
			// add the id as a property for that node
			addPropertyToNode(nodeId, "id", String.valueOf(nodeId));
			return nodeId;
		} else {
			return -1;
		}
	}

	public boolean addPropertyToNode(long nodeId, String name, String value) {
		String propertyUri = NODE_ENTRY_POINT_ROOT_URI + nodeId
				+ "/properties/" + name;
		WebResource resource = Client.create().resource(propertyUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.entity(toJsonStringLiteral(value)).put(ClientResponse.class);

		if(response != null && response.getStatus() == 204) {
			return true;
		}
		
		return false;
	}

	private static String toJsonStringLiteral(String str) {
		return "\"" + str + "\"";
	}

	public static void main(String[] args) {
		Neo4jGraphDb db = Neo4jGraphDb.getInstance();
		long id = db.createNode();
		System.out.println(id);
		db.addPropertyToNode(id, "name", "node" + id);
	}

}
