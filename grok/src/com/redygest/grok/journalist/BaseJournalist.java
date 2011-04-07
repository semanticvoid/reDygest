/**
 * 
 */
package com.redygest.grok.journalist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONSerializer;

import com.redygest.commons.data.Story;
import com.redygest.commons.data.Tweet;
import com.redygest.commons.store.MysqlStore;
import com.sun.corba.se.impl.resolver.FileResolverImpl;

/**
 * Class representing the base journalist template
 * 
 * @author semanticvoid
 * 
 */
abstract class BaseJournalist {

	protected List<Tweet> tweets;

	abstract Story process(List<Tweet> tweets);

	/**
	 * Read tweets
	 * 
	 * @param file
	 * @return
	 */
	protected final List<Tweet> read(String file) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		try {
			BufferedReader rdr = new BufferedReader(new FileReader(new File(
					file)));
			String line;

			while ((line = rdr.readLine()) != null) {
				try {
					Tweet t = new Tweet(line);
					if (t.getText() != null) {
						tweets.add(t);
					}
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
		tweets = read(file);
		Story s = process(tweets);
		write(s);
	}
}
