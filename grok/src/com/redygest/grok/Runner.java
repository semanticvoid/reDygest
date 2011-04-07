/**
 * 
 */
package com.redygest.grok;

import com.redygest.grok.journalist.EchoJournalist;

/**
 * Grok Invoker
 * @author semanticvoid
 *
 */
public class Runner {

	public static void main(String[] args) {
		EchoJournalist j = new EchoJournalist();
		j.run(args[0]);
	}

}
