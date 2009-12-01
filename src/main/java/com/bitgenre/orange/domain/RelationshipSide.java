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
    protected final boolean lazyFetch;
    protected final CascadeType cascadeType;

    public RelationshipSide(String name, RelationshipType type, String toEntity, List<String> properties,
    		boolean lazyFetch, CascadeType cascadeType) {
        this.name = name;
        this.type = type;
        this.toEntity = toEntity;
        this.properties = properties;
        this.lazyFetch = lazyFetch;
        this.cascadeType = cascadeType;
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
    
    public boolean isLazyFetch() {
		return lazyFetch;
	}

	public CascadeType getCascadeType() {
		return cascadeType;
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
        
        if (lazyFetch == true) {
        	bldr.append(", fetch=lazy");
        } else {
        	bldr.append(", fetch=eager");
        }
        
        bldr.append(", cascade=");
        bldr.append(cascadeType);
        
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
        // Child 3 == FETCH propertices (eager, lazy or empty/none)
        // Child 4 == CASCADE properties (cascadeSave, cascadeDelete, cascadeAll, or empty/none)

        CommonTree sideNameNode = (CommonTree)node.getChildren().get(0);
        CommonTree toEntityNameNode = (CommonTree)node.getChildren().get(1);

        //
        // Fetch properties for this side, if any.
        //
        List<String> parsedProperties;

        if (node.getChildCount() >= 3)  {
            CommonTree startNode = (CommonTree)node.getChildren().get(2);
            parsedProperties = fetchProperties(startNode);
        } else {
            parsedProperties = new ArrayList<String>(); // empty list of properties.
        }
        
        boolean lazyFetch = deriveFetchMode((CommonTree)node.getChildren().get(3));
        
        CascadeType cascading = deriveCascading((CommonTree)node.getChildren().get(4));
        

        RelationshipSide side = new RelationshipSide(sideNameNode.getText(),
                sideType, toEntityNameNode.getText(), parsedProperties,
                lazyFetch, cascading);

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

	// 
    // Gets the 'FETCH' nodes in relationship side.
    // Returns the fetch value if any were found, otherwise null.
    // Returns TRUE if LAZY, False if EAGER.
    //
    @SuppressWarnings("unchecked")
	protected static boolean
    deriveFetchMode(CommonTree fetchParentNode)
    {
    	final boolean defaultValue = true;
    	
    	List<CommonTree> fetchChildren = fetchParentNode.getChildren();
    	if (fetchChildren != null) {
    		String value = fetchChildren.get(0).getText();
    		if ("eager".equals(value)) {
    			return (false);
    		} else if ("lazy".equals(value)) {
    			return (true);
    		} else {
    			throw (new RuntimeException("Error! expecting fetch value of 'lazy' or 'eager'. Got: " + value));
    		}
    	}
    	
    	return defaultValue;
    }
    
    /**
     * Figures out the CascadeType from parent commonTree node.
     * 
     * @param cascadeParentNode
     * @return 
     */
    @SuppressWarnings("unchecked")
	protected static CascadeType
    deriveCascading(CommonTree cascadeParentNode)
    {
    	final CascadeType defaultValue = CascadeType.CascadeNone;
    	
    	List<CommonTree> cascadeChildren = cascadeParentNode.getChildren();
    	if (cascadeChildren != null) {
    		String value = cascadeChildren.get(0).getText();
    		CascadeType cascade = CascadeType.fromString(value);
    		return (cascade);
    	}
    	
    	return defaultValue;   	
    }

}
