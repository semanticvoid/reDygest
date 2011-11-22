package com.redygest.webservice;

import java.util.Collection;

import com.redygest.grok.knowledge.Curator;
import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.query.IRetriever;
import com.redygest.grok.knowledge.query.RetrieverFactory;
import com.redygest.grok.knowledge.query.datatype.HeadWordEntity;
import com.redygest.grok.knowledge.query.datatype.RelationEntity;
import com.redygest.grok.knowledge.query.datatype.Result;
import com.redygest.webservice.model.Results;

public class Neo4jRedygestService implements RedygestService {
	private static Neo4jRedygestService service = null;
	private IRetriever retriever = null;
	public static synchronized Neo4jRedygestService getInstance() {
		if(service == null) {
			service = new Neo4jRedygestService();
		}
		return service;
	}
	
	private Neo4jRedygestService() {
		Curator c = new Curator("green-house");
		IRepresentation model = c.getModel();
		retriever = RetrieverFactory.getInstance().produceRetriever(model);
	}
	
	@Override
	public Results getQueryResults(String hw1, String r, String hw2) {
		if(hw1.equals("*")) {
			hw1 = "";
		}
		if(hw2.equals("*")) {
			hw2 = "";
		}
		if(r.equals("*")) {
			r = "";
		}		
		Collection<Result> results = retriever.query(new HeadWordEntity(hw1), new RelationEntity(r), new HeadWordEntity(hw2));
		
		//setting the data model
		Results res= new Results();
		res.setResultCode("SUCCESS");
		res.setQueryResults(results);
		
		return res;
	}

}
