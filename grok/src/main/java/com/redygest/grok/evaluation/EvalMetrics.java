
package com.redygest.grok.evaluation;

public class EvalMetrics {

	double fp;
	double fn;
	double tp;
	double tn;
	double f1;
	double err;

	public EvalMetrics() {
		fp = 0;
		fn = 0;
		tp = 0;
		tn = 0;
		err = 0;
	}

	public void incrFP() {
		fp++;
	}

	public void incrFN() {
		fn++;
	}

	public void incrTP() {
		tp++;
	}

	public void incrTN() {
		tn++;
	}

	public void incrFP(double count) {
		fp += count;
	}

	public void incrFN(double count) {
		fn += count;
	}

	public void incrTP(double count) {
		tp += count;
	}

	public void incrTN(double count) {
		tn += count;
	}

	public double getFp() {
		return fp;
	}

	public double getFn() {
		return fn;
	}

	public double getTp() {
		return tp;
	}

	public double getTn() {
		return tn;
	}

	public double calculateF1Measure() {
		double precision = calculatePrecision();
		double recall = calculateRecall();

		if ((precision + recall) == 0) {
			return 0;
		}

		f1 = 2 * (precision * recall) / (precision + recall);
		return f1;
	}

	public double calculateError() {
		if ((tp + fp) == 0) {
			return 0;
		}

		err = (fp / (tp + fp));
		return err;
	}

	public double calculatePrecision() {
		try {
			if ((tp + fp) == 0) {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}

		return 1.0 * tp / (1.0 * tp + 1.0 * fp);
	}

	public double calculateRecall() {
		try {
			if ((tp + fn) == 0) {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}

		return 1.0 * tp / (1.0 * tp + 1.0 * fn);
	}
}
