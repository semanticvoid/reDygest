/**
 * 
 */
package com.redygest.commons.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Community Class
 * 
 * @author semanticvoid
 * 
 */
public class Community {

	public static enum CommunityAttribute {
		PAGERANK;
	};

	// id
	String id;
	// members & attributes
	Map<String, Map<CommunityAttribute, Double>> members;

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
	public Set<String> getMembers() {
		Set<String> memberSet = null;

		if (this.members != null) {
			memberSet = this.members.keySet();
		}

		return memberSet;
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

}
