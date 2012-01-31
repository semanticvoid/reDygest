package com.redygest.commons.db.synonym;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.redygest.commons.db.nosql.SimpleDB;

public class NounSynonymWikipediaRedirects implements ISynonymDb {

	private SimpleDB db = null;
	
	/**
	 * Constructor
	 */
	public NounSynonymWikipediaRedirects() {
		db = SimpleDB.getInstance();
	}
	
	@Override
	public String getSynonym(String w) {
		String root = null;
		
		if(w != null) {
			StringBuffer query = new StringBuffer(
					"select root from wikipediaredirects where variation = '");
			query.append(normalize(w));
			query.append("' limit 1");
			SelectResult result = db
					.select(new SelectRequest(query.toString()));
			if (result != null) {
				List<Item> items = result.getItems();
				if(items != null) {
					for(Item item : items) {
						List<Attribute> attributes = item.getAttributes();
						if(attributes != null) {
							for(Attribute attr : attributes) {
								root = attr.getValue();
								break;
							}
						}
					}
				}
			}
		}
		
		return root;
	}

	/**
	 * Normalize
	 */
	private String normalize(String w) {
		if(w == null) {
			return null;
		}
		
		w = w.toLowerCase().replaceAll(" ", "_");
		return w;
	}
}
