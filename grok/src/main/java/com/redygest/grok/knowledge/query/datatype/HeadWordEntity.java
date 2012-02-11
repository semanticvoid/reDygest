package com.redygest.grok.knowledge.query.datatype;

public class HeadWordEntity implements Entity {
	private String entityValue = null;
	
	public HeadWordEntity(String entityValue) {
		this.entityValue = entityValue;
	}

	public EntityType getType() {
		return EntityType.HEADWORD;
	}

	public String getValue() {
		return entityValue;
	}

}
