/**
 * 
 */
package com.redygest.piggybank.util;

import java.io.IOException;
import java.util.Iterator;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank Tweet Class
 * 
 */
public class OneFromBag extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {
			DataBag bag = (DataBag) input.get(0);
			Iterator<Tuple> itr = bag.iterator();
			if (itr != null) {
				while (itr.hasNext()) {
					oTuple = itr.next();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return oTuple;
	}

}
