/**
 * 
 */
package com.redygest.piggybank.twitter;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank Tweet Class
 * 
 */
public class AddCountToTweet extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {
			String jsonStr = (String) input.get(0);
			long count = (Long) input.get(1);

			Tweet t = new Tweet(jsonStr);
			t.setRetweetCount(t.getRetweetCount() + count);
			String tweet = t.toJSON();
			if (tweet != null) {
				oTuple.append(tweet);
			}
			// oTuple.append(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return oTuple;
	}

}
