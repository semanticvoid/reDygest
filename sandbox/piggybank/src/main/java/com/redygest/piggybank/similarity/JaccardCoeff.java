package com.redygest.piggybank.similarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank function for computing jaccard coefficient
 * 
 * @author akishore
 * 
 */
public class JaccardCoeff extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();
		Tuple sketchTupleA = (DefaultTuple) input.get(0);
		Tuple sketchTupleB = (DefaultTuple) input.get(1);

		List<Object> fieldsA = sketchTupleA.getAll();
		List<Object> fieldsB = sketchTupleB.getAll();
		List<Integer> iFields1 = new ArrayList<Integer>();
		List<Integer> iFields2 = new ArrayList<Integer>();

		int count = fieldsA.size() * 2;
		int match = 0;

		for (int i = 0; i < fieldsA.size(); i++) {
			int a = (Integer) fieldsA.get(i);
			for (int j = 0; j < fieldsB.size(); j++) {
				int b = (Integer) fieldsB.get(j);
				if (a == b) {
					match += 2;
				}
			}
		}

		double sim = (double) match / (double) count;

		oTuple.append(sim);
		return oTuple;
	}

}
