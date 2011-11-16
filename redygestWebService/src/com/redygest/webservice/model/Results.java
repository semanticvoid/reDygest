package com.redygest.webservice.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("results")
public class Results {
	private String resultCode = null;
	
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}
