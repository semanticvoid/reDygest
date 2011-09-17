package com.redygest.grok.knowledge.query;

import java.util.Collection;

import com.redygest.grok.knowledge.query.datatype.Entity;
import com.redygest.grok.knowledge.query.datatype.Result;

public interface IQueryTransformer {
	Collection<Result> query(String freeText);
	
	Collection<Result> query(Entity n1, Entity r, Entity n2);
}
