package com.bitgenre.orange.domain;

import java.util.List;
import java.util.Properties;

import org.antlr.runtime.tree.CommonTree;

/**
 * Represents a single decoration for an entity, attribute or relationship.
 */
public class Decoration {
	
	protected final DecoratedThing thingDecorated;
	
	/** Name of attribute or relationship side this decoration applies to, or NULL if applies to entity. */
	protected final String appliesToName;  
	
	protected final Properties properties = new Properties();

	
	public Decoration(String name, DecoratedThing itemDecorated) {
		super();
		this.thingDecorated = itemDecorated;
		this.appliesToName = name;		
	}

	public DecoratedThing getThingDecorated() {
		return thingDecorated;
	}

	public String getAppliesToName() {
		return appliesToName;
	}

	public void
	addProperty(String key, String value)
	{
		properties.put(key, value);
	}
	
	public Properties getProperties() {
		return properties;
	}

	
	@Override
	public String toString() {
		return "\nDecoration [Thing Decorated=" + thingDecorated
				+ ", appliesToName=" + appliesToName + ", properties="
				+ properties + "]";
	}

	public static Decoration
	createDecorationFromRelationshipAST(CommonTree parentNode, CommonTree decorationNode)
	{
		Decoration decoration = new Decoration(parentNode.getText(), DecoratedThing.Relationship);
		
		fillEntityOrRelationshipFromAST(decoration, decorationNode);
		
		return decoration;
	}
	
	public static Decoration
	createDecorationFromEntityAST(CommonTree parentNode, CommonTree decorationNode)
	{
		Decoration decoration = new Decoration(parentNode.getText(), DecoratedThing.Entity);
		
		fillEntityOrRelationshipFromAST(decoration, decorationNode);
      
        return decoration;
	}
	
	@SuppressWarnings("unchecked")
	protected static void 
	fillEntityOrRelationshipFromAST(Decoration decoration, CommonTree decorationNode)
	{
		// Child Node 0..N == Property values for the entity
		List<CommonTree> propertyNodes = decorationNode.getChildren();
        for (CommonTree property : propertyNodes) {
        	
        	String key = property.getText();
        	String value = property.getChild(0).getText();
        	
        	decoration.addProperty(key, value);
        }
		
	}
	
	@SuppressWarnings("unchecked")
	public static Decoration
	createDecorationFromAttributeAST(CommonTree decorationNode)
	{
		// Child 0 == Name of attribute the decoration applies to.
		String appliesToAttribute = decorationNode.getChild(0).getText();
		
		Decoration decoration = new Decoration(appliesToAttribute, DecoratedThing.Attribute);
		
		// Child Node 1..N == Property values for the entity
		List<CommonTree> propertyNodes = decorationNode.getChildren();
		for (int i = 1; i < propertyNodes.size(); i++) {
			
        	String key = propertyNodes.get(i).getText();
        	String value = propertyNodes.get(i).getChild(0).getText();
        	
        	decoration.addProperty(key, value);
        }
        
        return decoration;
	}
	

}
