/**
 * 
 */
package com.redygest.commons.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Community Class
 * 
 * @author semanticvoid
 * 
 */
public class Community implements Comparable<Community> {

	public static enum CommunityAttribute {
		PAGERANK;
	};

	// id
	String id;
	// members & attributes
	Map<String, Map<CommunityAttribute, Double>> members;
	// score
	double score = 0;

	/**
	 * Constructor
	 */
	public Community(String id) {
		this.id = id;
		this.members = new HashMap<String, Map<CommunityAttribute, Double>>();
	}

	/**
	 * Add member with attributes
	 * 
	 * @param member
	 * @param attrs
	 */
	public void add(String member, Map<CommunityAttribute, Double> attrs) {
		if (this.members != null) {
			if (!this.members.containsKey(member)) {
				this.members.put(member,
						new HashMap<CommunityAttribute, Double>());
			}
			this.members.get(member).putAll(attrs);
		}
	}

	/**
	 * Get id
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Function to get member set
	 * 
	 * @return
	 */
	public List<String> getMembers() {
		List<String> members = new ArrayList<String>();

		if (this.members != null) {
			members = new ArrayList<String>(this.members.keySet());
		}

		return members;
	}

	/**
	 * Get attribute value for member
	 * 
	 * @param member
	 * @param attr
	 * @return
	 */
	public double getMemberAttribute(String member, CommunityAttribute attr) {
		double value = -1;

		if (this.members != null && this.members.containsKey(member)
				&& this.members.get(member).containsKey(attr)) {
			value = this.members.get(member).get(attr);
		}

		return value;
	}

	/**
	 * Getter for score
	 * 
	 */
	public double getScore() {
		return this.score;
	}

	/**
	 * Setter for score
	 * 
	 * @param score
	 */
	public void setScore(double score) {
		this.score = score;
	}

	public int compareTo(Community o) {
		if (o != null) {
			if (this.getScore() > o.getScore()) {
				return 1;
			} else {
				return -1;
			}
		}

		return 0;
	}

}
