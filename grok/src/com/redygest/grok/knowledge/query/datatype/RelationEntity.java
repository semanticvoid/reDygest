package com.redygest.grok.knowledge.query.datatype;

public class RelationEntity implements Entity {
	
	private String entityValue = null;
	
	public RelationEntity(String entityValue) {
		this.entityValue = entityValue;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.RELATION;
	}

	@Override
	public String getValue() {
		return entityValue;
	}

}
