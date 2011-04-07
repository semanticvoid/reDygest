/**
 * 
 */
package com.redygest.grok.journalist;

import java.util.List;

import com.redygest.commons.data.Story;
import com.redygest.commons.data.Tweet;

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
	protected Story process(List<Tweet> tweets) {
		StringBuffer body = new StringBuffer();
		
		for(Tweet t : tweets) {
			body.append(t.getText() + " ");
		}
		
		int end = 200;
		if(body.length() < 200) {
			end = body.length();
		}
		
		Story s = new Story(body.substring(0, end), body.toString());
		
		return s;
	}

}
