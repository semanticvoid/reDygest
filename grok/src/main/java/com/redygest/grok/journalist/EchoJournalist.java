/**
 * 
 */
package com.redygest.grok.journalist;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.Story;

/**
 * Echo Journalist - just echos everything out
 * 
 * @author semanticvoid
 * 
 */
public class EchoJournalist extends BaseJournalist {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	protected Story process(List<Data> tweets) {
		StringBuffer body = new StringBuffer();
		Story s = new Story();

		for (Data t : tweets) {
			s.addLine(t);
		}

		return s;
	}

}
