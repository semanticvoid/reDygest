package com.redygest.commons.data;

import junit.framework.TestCase;

public class TweetTest extends TestCase {

	public void testTweet() {
		Data data = new Tweet("\"We’ve always had some greenhouse gases in the atmosphere,\" Solomon says", "0");
		assertEquals("we’ve always had some greenhouse gases in the atmosphere solomon says", data.getValues(DataType.BODY).get(0));
	}
}