package com.redygest.commons.data;

public class NamedEntity {
	public String ner_class;
	public String text; 
	
	public NamedEntity(String text, String ner_class){
		this.ner_class = ner_class.trim();
		this.text = text.trim();
	}
	
	@Override
	public String toString(){
		return this.text+":"+this.ner_class;
	}

}
