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
public class OneFromBag extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		Tweet tweet = null;

		try {
			DataBag bag = (DataBag) input.get(0);
			Iterator<Tuple> itr = bag.iterator();
			if (itr != null) {
				while (itr.hasNext()) {
					Tuple t = itr.next();

					if (t != null && t.size() >= 2) {
						String jsonStr = (String) t.get(1);
						Tweet tw = new Tweet(jsonStr);

						if (tweet == null) {
							tweet = tw;
						} else {
							tweet.addTime(tw.getTime());
						}
					}
				}

				oTuple.append(tweet.toJSON());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return oTuple;
	}

}
