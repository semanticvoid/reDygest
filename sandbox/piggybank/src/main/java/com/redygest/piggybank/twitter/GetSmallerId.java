package com.redygest.piggybank.twitter;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank function for getting the smaller id
 * 
 * @author akishore
 * 
 */
public class GetSmallerId extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {

			String id1 = (String) input.get(0);
			String id2 = (String) input.get(1);

			StringBuffer key = new StringBuffer();
			if (id1.compareToIgnoreCase(id2) > 0) {
				key.append(id1);
			} else {
				key.append(id2);
			}

			oTuple.append(key.toString());
			return oTuple;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
