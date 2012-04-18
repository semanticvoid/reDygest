/**
 * 
 */
package com.redygest.piggybank.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.util.hash.MurmurHash;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank function for generating MinHash signatures using 'n' number of hash
 * functions
 * 
 * @author akishore
 * 
 */
public class MinHashSketch extends EvalFunc<Tuple> {

	private static List<Integer> seeds = null;
	private static MurmurHash mHash = new MurmurHash();

	private static void initSeeds(int n) {
		seeds = new ArrayList<Integer>();
		for (int i = 1; i <= n; i++) {
			// generate a deterministic seed
			seeds.add(i * 100);
		}
	}

	@Override
	public Tuple exec(Tuple input) throws IOException {
		List<Integer> minHashSketch = new ArrayList<Integer>();
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();
		Tuple shingleTuple = (DefaultTuple) input.get(0);
		int n = (Integer) input.get(1);
		List<Object> shingles = shingleTuple.getAll();

		if (seeds == null) {
			initSeeds(n);
		}

		// init sketch
		for (int i = 0; i < n; i++) {
			minHashSketch.add(Integer.MAX_VALUE);
		}

		// generate sketch
		for (Object f : shingles) {
			String w = ((Integer) f).toString();
			for (int i = 0; i < n; i++) {
				int h = mHash.hash(w.getBytes(), seeds.get(i));
				if (h < minHashSketch.get(i)) {
					minHashSketch.set(i, h);
				}
			}
		}

		// populate output tuple
		for (Integer sig : minHashSketch) {
			oTuple.append(sig);
		}

		return oTuple;
	}

}
