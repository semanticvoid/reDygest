/**
 * 
 */
package com.redygest.commons.util;

/**
 * @author semanticvoid
 * 
 */
public class Math {

	/**
	 * Sigmoid function
	 * 
	 * @param x
	 * @param weight
	 * @param threshold
	 */
	public static double sigmoid(double x, double weight, double threshold) {
		return 1 / (1 + java.lang.Math.pow(java.lang.Math.E,
				(-1 * weight * (x - threshold))));
	}
}
