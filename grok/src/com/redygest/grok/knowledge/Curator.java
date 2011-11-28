package com.redygest.grok.knowledge;

import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.redygest.grok.features.datatype.AttributeType;
import com.redygest.grok.features.datatype.Attributes;
import com.redygest.grok.features.datatype.FeatureVector;
import com.redygest.grok.features.datatype.Variable;
import com.redygest.grok.knowledge.graph.IRepresentation;
import com.redygest.grok.knowledge.graph.Node;
import com.redygest.grok.knowledge.graph.Relation;
import com.redygest.grok.knowledge.graph.RepresentationFactory;
import com.redygest.grok.knowledge.graph.Node.NodeType;
import com.redygest.grok.knowledge.graph.Relation.Relationship;
import com.redygest.grok.knowledge.graph.RepresentationFactory.RepresentationType;
import com.redygest.grok.repository.IFeaturesRepository;

/**
 * Class which organizes knowledge events/facts
 *
 */
public class Curator {

	// the internal representation
	IRepresentation kmodel;
	String name;
	
	static Logger logger = Logger.getLogger(Curator.class);
	
	/**
	 * Constructor
	 * @param name	the uniq curation name
	 */
	public Curator(String name) {
		kmodel = RepresentationFactory.getInstance().produceRepresentation(name, RepresentationType.NEO4J);
		this.name = name;
	}
	
	/**
	 * Constructor (auto generate name)
	 */
	public Curator() {
		name = String.valueOf(new Double(Math.random()).hashCode());
		kmodel = RepresentationFactory.getInstance().produceRepresentation(name, RepresentationType.NEO4J);
	}
	
	/**
	 * Add all events in a Repository
	 * @param repository
	 * @return true on sucess false otherwise
	 */
	public boolean addRepository(IFeaturesRepository repository) {
		if(repository != null && repository.size() > 0) {
			Set<Long> identifiers = repository.getIdentifiers();
			if(identifiers != null) {
				for(Long id : identifiers) {
					FeatureVector fVector = repository.getFeature(String.valueOf(id));
					if(fVector != null) {
						List<Variable> variables = fVector.getVariablesWithAttributeType(AttributeType.HAS_SRL);
						if(variables != null) {
							// create Sentence
							Node sentence = new Node(NodeType.SENTENCE);
							kmodel.addNode(sentence);
							
							for(Variable var : variables) {
								Attributes attrs = var.getVariableAttributes();
								if(attrs != null) {
									// create Event
									Node event = new Node(NodeType.EVENT);
									kmodel.addNode(event);
							        logger.info("Creating Event");
									
									// create Relation: Sentence -> Event
									
									for(AttributeType type : attrs.getAttributesMap().keySet()) {
										//AttributeType type = attrs.get(attr);
										for(String attrValue : attrs.getAttributeNames(type)) {
											logger.info("Attrvalue: "+attrValue+" type:"+type.toString());
											Relationship rType = attributeToRelationship(type);
											if(rType != null) {
												// create/fetch Entity
												Node entity = kmodel.getNodeWithName(attrValue);
												if(entity == null) {
													entity = new Node(NodeType.ENTITY, attrValue);
													System.out.println(attrValue);
													kmodel.addNode(entity);
												}
												// create Relation: Event -> Entity
												Relation rel = new Relation(rType, event, entity);
												kmodel.addRelation(rel);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get the knowledge model
	 * @return the IRepresentation
	 */
	public IRepresentation getModel() {
		return this.kmodel;
	}
	
	/**
	 * Get the knowledge model name
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Map SRL Attributes to Relationships
	 * @param type
	 * @return
	 */
	private Relationship attributeToRelationship(AttributeType type) {
		switch (type) {
			case SRL_A0:
				return Relationship.A0;
			case SRL_A1:
				return Relationship.A1;
			case SRL_A2:
				return Relationship.A2;
			case SRL_ACTION:
				return Relationship.ACTION;
			case SRL_LOC:
				return Relationship.LOC;
			case SRL_MNR:
				return Relationship.MNR;
			case SRL_PNC:
				return Relationship.PNC;
			case SRL_TMP:
				return Relationship.TMP;
			default:
				return null;
		}
	}
	
}
