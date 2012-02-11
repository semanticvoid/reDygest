package com.redygest.grok.knowledge.query.datatype;

public class RelationEntity implements Entity {
	
	private String entityValue = null;
	
	public RelationEntity(String entityValue) {
		this.entityValue = entityValue;
	}
	
	public EntityType getType() {
		return EntityType.RELATION;
	}

	public String getValue() {
		return entityValue;
	}

}
