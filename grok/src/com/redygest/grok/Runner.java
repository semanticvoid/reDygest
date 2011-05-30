/**
 * 
 */
package com.redygest.grok;

import com.redygest.grok.journalist.BaselineClusteringJournalist;

/**
 * Grok Invoker
 * @author semanticvoid
 *
 */
public class Runner {

	public static void main(String[] args) {
//		EchoJournalist j = new EchoJournalist();
		BaselineClusteringJournalist j = new BaselineClusteringJournalist();
		j.run(args[0]);
	}

}
