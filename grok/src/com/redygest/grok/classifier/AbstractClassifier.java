package com.redygest.grok.classifier;

import java.util.List;

import com.redygest.commons.data.Data;

public abstract class AbstractClassifier implements IClassifier {
	
	@Override
	public boolean train() {
		return false;
	}
	
	@Override
	public boolean test() {
		return false;
	}

	@Override
	public List<String> classifiy(List<Data> corpus) {
		return null;
	}

}
