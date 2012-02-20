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
import com.redygest.commons.preprocessor.twitter.PreprocessorZ20120215;
import com.redygest.commons.store.MysqlStore;

/**
 * Class representing the base journalist template
 * 
 * @author semanticvoid
 * 
 */
abstract class BaseJournalist {

	protected List<Data> tweets;

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
			ITweetPreprocessor preprocessor = new PreprocessorZ20120215();
			while ((line = rdr.readLine()) != null) {
				try {
					Tweet t = new Tweet(line, String.valueOf(i), preprocessor);
					if (t.getValue(DataType.BODY) != null) {
						addTweet(t);
					}
					i++;
				} catch(Exception e) {
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
	 * @param t
	 * @return
	 */
	protected void addTweet(Data t) {
		tweets.add(t);
	}

	/**
	 * Write story to db
	 * @param s
	 * @return
	 */
	protected final boolean write(Story s) {
		try {
			MysqlStore store = new MysqlStore("localhost", "root", "", "dygest");
			return store.executeUpdate("INSERT INTO stories (title, body) values (\""
					+ s.getTitle() + "\",\"" + s.getBody() + "\")");
		} catch (Exception e) {
			e.printStackTrace();
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
