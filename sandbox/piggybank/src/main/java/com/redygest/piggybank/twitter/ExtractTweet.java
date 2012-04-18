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
			String jsonStr = ((DataByteArray) input.get(0)).toString();
			Tweet t = new Tweet(jsonStr);
			String id = t.getId();
			String txt = t.getText();
			if (id != null && txt != null) {
				oTuple.append(id);
				oTuple.append(txt);
			}
		} catch (Exception e) {
			// oTuple.append("");
		}

		return oTuple;
	}

}
