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
public class GetText extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {
			String jsonStr = (String) input.get(0);
			Tweet t = new Tweet(jsonStr);
			String txt = t.getText();
			if (txt != null) {
				oTuple.append(txt);
			}
		} catch (Exception e) {
			// oTuple.append("");
		}

		return oTuple;
	}

}
