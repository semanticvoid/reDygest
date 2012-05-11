package com.redygest.grok.ranking.community;

import java.util.List;

import com.redygest.commons.data.Community;
import com.redygest.commons.data.Data;

public interface ICommunityRanking {
	/**
	 * 
	 * @param communities
	 * @param data
	 * @return List of community ids
	 */
	List<Community> rank(List<Community> communities, List<Data> data);
}
