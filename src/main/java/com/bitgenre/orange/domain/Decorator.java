package com.bitgenre.orange.domain;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

/**
 * Represents the 'decorate' object.
 */
public class Decorator {
	
	/** The type of decorator this is. Example : Persistence. */
    protected final String decoratorType;
    
    /** The entity type this decorator applies to. */
    protected final String forEntity;
    
    
    /** This is the main contents of the decorations for the decorator. Key of top-level map
     * is the attribute name. Key of second level map is metadata property.
     */
    protected List<Decoration> decoratorItems = new ArrayList<Decoration>();

    
	public Decorator(String decoratorType, String forEntity,
			List<Decoration> decoratorItems) {
		super();
		this.decoratorType = decoratorType;
		this.forEntity = forEntity;
		this.decoratorItems = decoratorItems;
	}

	public Decorator(String decoratorType, String forEntity) {
		super();
		this.decoratorType = decoratorType;
		this.forEntity = forEntity;
	}



	public String getDecoratorType() {
		return decoratorType;
	}

	

	public String getForEntity() {
		return forEntity;
	}

	public List<Decoration> getDecoratorItems() {
		return decoratorItems;
	}

	public void
	addDecoratorItem(Decoration decoration)
	{
		decoratorItems.add(decoration);
	}
	
	public String
	toString()
	{
		StringBuilder bldr = new StringBuilder();
		bldr.append("Decorator type: '");
		bldr.append(this.decoratorType);
		bldr.append("', applies to: '");
		bldr.append(this.forEntity);
		bldr.append("'");
		
		if ((decoratorItems != null) && (decoratorItems.size() > 0)) {
			bldr.append(", Properties: \n");	
			for (Decoration decoration : decoratorItems) {
				bldr.append(decoration.toString());
			}
		}
		
		
		return bldr.toString();
	}

	@SuppressWarnings("unchecked")
	public static Decorator
    createDecoratorFromAST(CommonTree decoratorNode)
    {

        // Child Node 0 == The entity the decorator applies to
        CommonTree forEntityNode = (CommonTree)decoratorNode.getChildren().get(0);
        String forEntity = forEntityNode.getText();
        
        // Child Node 0 == The 'class name' or 'type' of decorator 
        // Child Nodes 1..N == Decorations for Entity, Attribute or Relationship.
        //
        
        // Get type name from Child node 0.
        CommonTree typeNode = (CommonTree)forEntityNode.getChildren().get(0);
        String decoratorType = typeNode.getText();


        Decorator decorator = new Decorator(decoratorType, forEntity);
        
        // Get decoration data from Child Nodes1..N 
        List<CommonTree> decorations = forEntityNode.getChildren();
        for (CommonTree decorationNode : decorations) {
        	
        	if (decorationNode.getText() == "ENTITY_PROPERTIES") {
        		
        		Decoration entityLevelDecoration = Decoration.createDecorationFromEntityAST(forEntityNode, decorationNode);
        		decorator.addDecoratorItem(entityLevelDecoration);
        		
        	} else if (decorationNode.getText() == "RELATIONSHIP_PROPERTIES") {
            		
            		Decoration relationshipLevelDecoration = Decoration.createDecorationFromRelationshipAST(forEntityNode, decorationNode);
            		decorator.addDecoratorItem(relationshipLevelDecoration);
        		
        	} else if (decorationNode.getText() == "FIELD_PROPERTIES") {
        		
        		Decoration attrLevelDecoration = Decoration.createDecorationFromAttributeAST(decorationNode);
        		decorator.addDecoratorItem(attrLevelDecoration);
        		
        	} // else ignore it. 
        }
        

        return decorator;
    }
    
    
}
