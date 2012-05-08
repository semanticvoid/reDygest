package com.redygest.grok.ranking.community;

import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;

public interface ICommunityRanking {
	/**
	 * 
	 * @param memberships
	 * @param data  
	 * @return List of community ids
	 */
	List<String> rankCommunities(HashMap<String, List<String>> memberships, List<Data> data);
}
