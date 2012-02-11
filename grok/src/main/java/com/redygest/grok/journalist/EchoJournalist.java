/**
 * 
 */
package com.redygest.grok.journalist;

import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Story;

/**
 * Echo Journalist - just echos everything out
 * @author semanticvoid
 *
 */
public class EchoJournalist extends BaseJournalist {

	/* (non-Javadoc)
	 * @see com.redygest.grok.journalist.BaseJournalist#process(java.util.List)
	 */
	@Override
	protected Story process(List<Data> tweets) {
		StringBuffer body = new StringBuffer();
		
		for(Data t : tweets) {
			body.append(t.getValue(DataType.BODY) + " ");
		}
		
		int end = 200;
		if(body.length() < 200) {
			end = body.length();
		}
		
		Story s = new Story(body.substring(0, end), body.toString());
		
		return s;
	}

}
