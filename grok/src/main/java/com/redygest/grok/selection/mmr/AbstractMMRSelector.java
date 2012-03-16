package com.redygest.grok.selection.mmr;

import com.redygest.grok.selection.ISelector;

public abstract class AbstractMMRSelector implements ISelector {
	private double initialLambda = 0.3;
	private double maxLambda = 0.4;

	public void setLambdaValues(double initialLambda, double maxLambda) {
		this.initialLambda = initialLambda;
		this.maxLambda = maxLambda;
	}

	public double getInitialLambda() {
		return this.initialLambda;
	}

	public double getMaxLambda() {
		return this.maxLambda;
	}

}
