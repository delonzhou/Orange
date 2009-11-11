package com.bitgenre.orange.domain;

import org.antlr.runtime.tree.CommonTree;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents 1 side of a Relationship.
 */
public class RelationshipSide {
    protected final String name;
    protected final RelationshipType type;
    protected final String toEntity;
    protected final List<String> properties;

    public RelationshipSide(String name, RelationshipType type, String toEntity, List<String> properties) {
        this.name = name;
        this.type = type;
        this.toEntity = toEntity;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public RelationshipType getType() {
        return type;
    }

    public String getToEntity() {
        return toEntity;
    }
    
    public List<String> getProperties() {
		return properties;
	}

    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();
        bldr.append("Side: name=");
        bldr.append(name);
        bldr.append(", type= ");
        bldr.append(type);
        bldr.append("<");
        bldr.append(toEntity);
        bldr.append(">");
        bldr.append(", options: ");

        bldr.append(properties);

        return bldr.toString();
    }

    public static RelationshipSide
    createFromAST(CommonTree node)
    {
        // First node contains the relationship type.
        RelationshipType sideType = RelationshipType.fromParser(node.getText());

        // Child 0 == relationship side name
        // Child 1 == entity name relationship refers to
        // Child 2 == Properties

        CommonTree sideNameNode = (CommonTree)node.getChildren().get(0);
        CommonTree toEntityNameNode = (CommonTree)node.getChildren().get(1);

        //
        // Fetch properties for this side, if any.
        //
        List<String> parsedProperties;

        if (node.getChildCount() == 3)  {
            CommonTree startNode = (CommonTree)node.getChildren().get(2);
            parsedProperties = fetchProperties(startNode);
        } else {
            parsedProperties = new ArrayList<String>(); // empty list of properties.
        }

        RelationshipSide side = new RelationshipSide(sideNameNode.getText(),
                sideType, toEntityNameNode.getText(), parsedProperties);

        return (side);

    }

    //
    // Fills the 'PROPERTIES' nodes into relationship side;
    //
    @SuppressWarnings("unchecked")
	protected static List<String>
    fetchProperties(CommonTree propertiesNode)
    {
        List<String> properties = new ArrayList<String>();

         // Get properties value nodes and add them to the toFill com.bitgenre.orange.domain.Attribute.
         //
         List<CommonTree> propertiesChildren = propertiesNode.getChildren();
         if (propertiesChildren != null) {
            for (CommonTree property : propertiesChildren) {
                properties.add(property.getText());
            }
        }

        return(properties);
    }

	

}
