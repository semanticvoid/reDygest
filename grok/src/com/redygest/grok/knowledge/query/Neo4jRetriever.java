package com.redygest.grok.knowledge.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.Node;
import com.redygest.grok.knowledge.graph.NodeProperty;
import com.redygest.grok.knowledge.query.datatype.Entity;
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
	
	
	
	public String getCypherQuery(Entity n1, Entity r, Entity n2){
		List<String> nonNullNodes = new ArrayList<String>();
		if(n1!=null){
			nonNullNodes.add(n1.getValue());
		} 
		if(n2!=null){
			nonNullNodes.add(n2.getValue());
		}
		if(r!=null){
			nonNullNodes.add(r.getValue());
		}
		
		List<Node> nonNullNodeIds = new ArrayList<Node>();
		for(String node : nonNullNodes){
			nonNullNodeIds.add(representation.getNodeWithName(node));
		}
		StringBuilder query = new StringBuilder();
		if(nonNullNodeIds.size()==1){
			query.append("start n=(");
			query.append(nonNullNodeIds.get(0).get(NodeProperty.ID));
			query.append(") match (n)--(x)--(q) return q");
			return query.toString();
		} else {
			query.append("start n=(");
			query.append(nonNullNodeIds.get(0).get(NodeProperty.ID));
			query.append("), m=(");
			query.append(nonNullNodeIds.get(1).get(NodeProperty.ID));
			query.append(") match (n)--(x), (m)--(x), (q)--(x) return q");
			return query.toString();
		}
	}

	@Override
	public Collection<Result> query(Entity n1, Entity r, Entity n2) {

				
		return null;
	}
}
