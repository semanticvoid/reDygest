/**
 * 
 */
package com.redygest.piggybank.twitter;

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
public class OneFromBagWithMaxCount extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		Tweet t = null;

		try {
			DataBag bag = (DataBag) input.get(0);
			Iterator<Tuple> itr = bag.iterator();
			if (itr != null) {
				while (itr.hasNext()) {
					Tuple tup = itr.next();
					String jsonStr = (String) tup.get(1);
					Tweet t1 = new Tweet(jsonStr);

					if (t == null
							|| (t != null && t1.getRetweetCount() > t
									.getRetweetCount())) {
						t = t1;
					}
				}

				oTuple.append(t.toJSON());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return oTuple;
	}
}
