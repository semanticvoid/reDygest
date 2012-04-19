package com.redygest.piggybank.text;

import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank function for generating md5 hash key
 * 
 * @author akishore
 * 
 */
public class MD5HashKey extends EvalFunc<Tuple> {

	static MessageDigest md = null;
	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Tuple exec(Tuple input) throws IOException {
		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		try {
			if (md != null) {
				String id1 = (String) input.get(0);
				String id2 = (String) input.get(1);

				StringBuffer key = new StringBuffer();
				if (id1.compareToIgnoreCase(id2) > 0) {
					key.append(id1 + "-" + id2);
				} else {
					key.append(id2 + "-" + id1);
				}

				md.update(key.toString().getBytes());
				oTuple.append(new String(Hex.encodeHex(md.digest())));
				md.reset();
				return oTuple;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
