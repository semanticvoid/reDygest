package com.redygest.grok.ranking.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.util.Counter;
import com.redygest.commons.util.PriorityQueue;

public abstract class BaseCommunityRanking implements ICommunityRanking {

	public List<String> rankCommunities(
			HashMap<String, List<String>> memberships, List<Data> data) {
		Counter<String> communityRanks = new Counter<String>();
		for (String communityId : memberships.keySet()) {
			List<String> members = memberships.get(communityId);
			communityRanks.setCount(communityId, score(members, data));
		}
		PriorityQueue<String> q = communityRanks.asPriorityQueue();
		List<String> sorted = new ArrayList<String>();
		while (q.hasNext()) {
			sorted.add(q.next());
		}
		return sorted;
	}

	/**
	 * Abstract scoring function to be implemented by subclasses
	 * 
	 * @param members
	 *            
	 * @param data
	 *           
	 * @return score
	 */
	abstract double score(List<String> members, List<Data> data);

}
