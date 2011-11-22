package com.redygest.webservice.model;

import java.util.Collection;

import com.redygest.grok.knowledge.query.datatype.Result;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("results")
public class Results {
	private String resultCode = null;
	private Collection<Result> queryResults = null;
	
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public void setQueryResults(Collection<Result> queryResults) {
		this.queryResults = queryResults;
	}
}
