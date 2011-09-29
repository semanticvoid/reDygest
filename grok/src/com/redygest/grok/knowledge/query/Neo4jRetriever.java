package com.redygest.grok.knowledge.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.Node;
import com.redygest.grok.knowledge.graph.NodeProperty;
import com.redygest.grok.knowledge.query.datatype.Entity;
import com.redygest.grok.knowledge.query.datatype.HeadWordEntity;
import com.redygest.grok.knowledge.query.datatype.Result;

public class Neo4jRetriever implements IRetriever {

	private IRepresentation representation = null;

	public Neo4jRetriever(IRepresentation representation) {
		this.representation = representation;
	}

	@Override
	public Collection<Result> query(String freeText) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get Cypher query
	 * 
	 * @param n1
	 * @param r
	 * @param n2
	 * @return cypher query as String
	 */
	private String getCypherQuery(Entity n1, Entity r, Entity n2) {
		List<String> nonNullNodes = new ArrayList<String>();
		if (n1 != null) {
			nonNullNodes.add(n1.getValue());
		}
		if (n2 != null) {
			nonNullNodes.add(n2.getValue());
		}
		if (r != null) {
			nonNullNodes.add(r.getValue());
		}

		List<Node> nonNullNodeIds = new ArrayList<Node>();
		for (String node : nonNullNodes) {
			Node n = representation.getNodeWithName(node);
			if(n!=null){
				nonNullNodeIds.add(representation.getNodeWithName(node));
			}
		}
		
		StringBuilder query = new StringBuilder();
		if (nonNullNodeIds.size() == 1) {
			query.append("start n=(");
			query.append(nonNullNodeIds.get(0).get(NodeProperty.ID));
			query.append(") match (n)--(x)--(q) return q");
			return query.toString();
		} else if(nonNullNodeIds.size()==2){
			query.append("start n=(");
			query.append(nonNullNodeIds.get(0).get(NodeProperty.ID));
			query.append("), m=(");
			query.append(nonNullNodeIds.get(1).get(NodeProperty.ID));
			query.append(") match (n)--(x), (m)--(x), (q)--(x) return q");
			return query.toString();
		} else{
			return null;
		}
	}

	@Override
	public Collection<Result> query(Entity n1, Entity r, Entity n2) {
		Collection<Result> results = new ArrayList<Result>();
		List<Node> nodes = this.representation.getNodes(getCypherQuery(n1, r,
				n2));

		if (nodes != null) {
			for (Node n : nodes) {
				// TODO add generic Entity
				results.add(new Result(new HeadWordEntity(n
						.get(NodeProperty.NAME))));
			}

			return results;
		}

		return null;
	}
}
