package com.redygest.grok.ranking.community;

import java.util.ArrayList;
import java.util.List;

import com.redygest.commons.data.Community;
import com.redygest.commons.data.Data;

public abstract class BaseCommunityRanking implements ICommunityRanking {

	public List<Community> rank(List<Community> communities, List<Data> data) {
		List<Community> rankedCommunities = new ArrayList<Community>();

		if (communities != null && data != null) {
			java.util.PriorityQueue<Community> pQueue = new java.util.PriorityQueue<Community>();

			for (Community c : communities) {
				List<String> members = c.getMembers();
				double score = score(members, data);
				c.setScore(score);
				pQueue.add(c);
			}

			Community c = null;
			while ((c = pQueue.poll()) != null) {
				rankedCommunities.add(c);
			}
		}

		return rankedCommunities;
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
