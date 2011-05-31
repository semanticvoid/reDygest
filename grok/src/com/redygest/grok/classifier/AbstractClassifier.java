package com.redygest.grok.classifier;

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
	public int classifiy(Data d) {
		return -1;
	}

}
