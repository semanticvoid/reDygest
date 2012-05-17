/**
 * 
 */
package com.redygest.piggybank.text;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * Piggybank function for generating MD5 hash
 * 
 * @author akishore
 * 
 */
public class MD5Hash extends EvalFunc<Tuple> {

	@Override
	public Tuple exec(Tuple input) throws IOException {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TupleFactory tFactory = TupleFactory.getInstance();
		Tuple oTuple = tFactory.newTuple();

		String txt = (String) input.get(0);

		if (md != null && txt != null) {
			md.update(txt.getBytes("UTF-8"));
			oTuple.append(new String(Hex.encodeHex(md.digest())));
		}

		return oTuple;
	}
}
