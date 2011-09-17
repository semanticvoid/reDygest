package com.redygest.grok.knowledge.query;

import java.util.Collection;

import com.redygest.grok.knowledge.graph.IRepresentation;
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

	@Override
	public Collection<Result> query(Entity n1, Entity r, Entity n2) {
		// TODO Auto-generated method stub
		return null;
	}
}
