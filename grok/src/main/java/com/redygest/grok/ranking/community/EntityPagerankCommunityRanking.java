package com.redygest.grok.ranking.community;

import java.util.HashMap;
import java.util.List;

import com.redygest.commons.data.Data;
import com.redygest.commons.data.DataType;

public class EntityPagerankCommunityRanking extends BaseCommunityRanking {
	HashMap<String, Double> pageranks;

	public EntityPagerankCommunityRanking(HashMap<String, Double> pageranks) {
		this.pageranks = pageranks;
	}

	@Override
	double score(List<String> members, List<Data> data) {
		double score = 0;
		int member_count = 0;
		for (Data d : data) {
			double d_score = 0;
			for (String member : members) {
				if (this.pageranks.containsKey(member)
						&& d.getValue(DataType.ORIGINAL_TEXT).toLowerCase()
								.contains(member.toLowerCase())) {
					d_score += this.pageranks.get(member);
					member_count++;
				}
			}
			score += d_score;
		}
		if (member_count != 0) {
			return (score / member_count);
		} else {
			return 0;
		}
	}

}
