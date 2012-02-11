package com.redygest.grok.knowledge.query.datatype;

public class Result {
	private Entity entity = null;
	
	public Result(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
}
