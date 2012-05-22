/**
 * 
 */
package com.redygest.piggybank.twitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank Tweet Class
 * 
 */
public class MergeFromBag extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();
		Set<String> ids = new HashSet<String>();
		Tweet t = null;

		try {
			DataBag bag = (DataBag) input.get(0);
			Iterator<Tuple> itr = bag.iterator();
			if (itr != null) {
				while (itr.hasNext()) {
					Tuple tuple = itr.next();
					for (int i = 1; i < tuple.size(); i = i + 2) {
						String jsonStr = (String) tuple.get(i);

						if (jsonStr != null) {
							if (t == null) {
								t = new Tweet(jsonStr);
								ids.add(t.getId());
							} else {
								Tweet t1 = new Tweet(jsonStr);
								if (!ids.contains(t1.getId())) {
									t.setRetweetCount(t.getRetweetCount()
											+ t1.getRetweetCount());
									ids.add(t1.getId());
									if (t1.getTimeArray() != null) {
										t.addTimeArr(t1.getTimeArray());
									} else {
										t.addTime(t1.getTime());
									}
								}
							}
						}
					}

					break;
				}

				if (t != null) {
					oTuple.append(t.toJSON());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return oTuple;
	}

}
