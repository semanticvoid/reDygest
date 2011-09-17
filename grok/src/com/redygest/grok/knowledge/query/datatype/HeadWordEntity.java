package com.redygest.grok.knowledge.query.datatype;

public class HeadWordEntity implements Entity {
	private String entityValue = null;
	
	public HeadWordEntity(String entityValue) {
		this.entityValue = entityValue;
	}
 	@Override
	public EntityType getType() {
		return EntityType.HEADWORD;
	}

	@Override
	public String getValue() {
		return entityValue;
	}

}
