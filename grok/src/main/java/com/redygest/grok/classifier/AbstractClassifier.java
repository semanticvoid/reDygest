package com.redygest.grok.classifier;

import java.util.List;

import com.redygest.commons.data.Data;

public abstract class AbstractClassifier implements IClassifier {
	
	public boolean train() {
		return false;
	}
	
	public boolean test() {
		return false;
	}

	public List<String> classifiy(List<Data> corpus) {
		return null;
	}

}
