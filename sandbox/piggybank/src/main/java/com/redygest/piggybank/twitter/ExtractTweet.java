/**
 * 
 */
package com.redygest.piggybank.twitter;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank Tweet Class
 * 
 */
public class ExtractTweet extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {
			String jsonStr = null;
			if (input.get(0) instanceof DataByteArray) {
				jsonStr = ((DataByteArray) input.get(0)).toString();
			} else if (input.get(0) instanceof String) {
				jsonStr = (String) input.get(0);
			}

			Tweet t = new Tweet(jsonStr);
			String id = t.getId();
			String tweet = jsonStr;
			if (id != null && tweet != null) {
				oTuple.append(id);
				oTuple.append(tweet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return oTuple;
	}

}
