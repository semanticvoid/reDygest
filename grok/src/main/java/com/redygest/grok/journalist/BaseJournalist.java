/**
 * 
 */
package com.redygest.grok.journalist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;
import com.redygest.commons.data.Story;
import com.redygest.commons.data.Tweet;
import com.redygest.commons.preprocessor.twitter.ITweetPreprocessor;
import com.redygest.grok.prefilter.PrefilterRunner;
import com.redygest.grok.store.IStore;
import com.redygest.grok.store.StoreFactory;
import com.redygest.grok.store.StoreFactory.StoreType;

/**
 * Class representing the base journalist template
 * 
 * @author semanticvoid
 * 
 */
abstract class BaseJournalist {

	protected List<Data> tweets;
	protected ITweetPreprocessor preprocessor = null;
	protected PrefilterRunner prefilterRunner = null;

	abstract Story process(List<Data> tweets);

	/**
	 * Read tweets
	 * 
	 * @param file
	 * @return
	 */
	protected final List<Data> read(String file) {
		try {
			BufferedReader rdr = new BufferedReader(new FileReader(new File(
					file)));
			String line;
			long i = 0;
			while ((line = rdr.readLine()) != null) {
				try {
					boolean pass = true;
					Tweet t = new Tweet(line, String.valueOf(i), preprocessor);
					// prefilter code
					if (prefilterRunner != null) {
						pass = prefilterRunner.runFilters(t
								.getValue(DataType.ORIGINAL_TEXT));
					}

					if (pass && t.getValue(DataType.BODY) != null) {
						addTweet(t);
					}

					i++;
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tweets;
	}

	/**
	 * Placeholder for future selection/sampling strategies (if any)
	 * 
	 * @param t
	 * @return
	 */
	protected void addTweet(Data t) {
		tweets.add(t);
	}

	/**
	 * Write story to db
	 * 
	 * @param s
	 * @return
	 */
	protected boolean write(Story s) {
		IStore store = null;

		if (s != null) {
			store = StoreFactory.getInstance().produce(StoreType.MYSQL);

			// write to console if store not valid
			if (store == null) {
				System.out.println("---------- Story ----------");
				System.out.println(s.getBody());
			} else {
				return store.write(s.toJSON());
			}
		}

		return false;
	}

	/**
	 * base algorithm template
	 * 
	 * @param file
	 */
	public final void run(String file) {
		tweets = new ArrayList<Data>();
		tweets = read(file);
		Story s = process(tweets);
		write(s);
	}
}
