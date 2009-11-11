package com.bitgenre.orange.domain;

import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.List;

import com.bitgenre.orange.domain.Attribute;

/**
 * Converts a CommonNode into a typed attribute
 */
public class AttributeBuilder {

    /**
     * Parses an Attribute from top-level AST node.
     *
     * @param attributeNode
     * @param tokenNames
     * @return
     */
    public static Attribute
    parseAttributeFromAST(CommonTree attributeNode, String[] tokenNames)
    {
        //
        // Get datatype, it's the parent for all attribute nodes
        //
        String attrDatatype = attributeNode.getText();

        // Get name node, it is always the first child node by design of AST.
        String attrName = ((CommonTree)attributeNode.getChildren().get(0)).getText();



        // Next level of Parsing depends upon the datatype
         Attribute attr = createAttribute(attrName, attrDatatype, attributeNode);

        return attr;
    }
    

    protected static Attribute
    createAttribute(String attrName, String attrDataTypeName, CommonTree attributeNode)
    {

      AttrType datatype = AttrType.valueOf(attrDataTypeName);
      switch(datatype) {

      // Fill reference datatypes
      /*  NOT_USED replaced by Relationship.
      case Bag:
      case Set:
      case Ref:
          return(createReferenceType(attrName, datatype, attributeNode));
      */
  
      // Fill decimal datatypes
      case Decimal:
      case Money:
          return(createDecimalType(attrName, datatype, attributeNode));

      // Fill Simple datatypes.
      case Boolean:
      case Autoinc:
      case Autoinclong:
      case Date:
      case Datetime:
      case Float:
      case Double:
      case Guid:
      case Timestamp:
      case EntityId:
          return(createSimpleType(attrName, datatype, attributeNode));


      // Fill 'int' based datatypes.
      case Int:
      case Tinyint:
      case Long:
          return(createIntegerType(attrName, datatype, attributeNode));
   

      // Fill 'String' , 'Bit' and other Length based types.
      case String:
      case Char:
      case Clob:
      case Bit:
      case Blob:
          return(createLengthType(attrName, datatype, attributeNode));

      case Enum:
    	  return(createEnumType(attrName, datatype, attributeNode));
    	  
      default:
          throw(new IllegalArgumentException("Invalid datatype: "  + datatype));
      }

    }

    
    protected static AttributeEnum
    createEnumType(String attrName, AttrType datatype, CommonTree enumAttribute)
    {
    	String enumTypeName = attrName;
    	
    	// Child node 0 == EnumType name (required)
    	// Child node 1 == attribute name (required)
    	// Child node 2 == PROPERTIES
    	
    	CommonTree nameNode = (CommonTree)enumAttribute.getChild(1);
    	String name = nameNode.getText();
    	
    	AttributeEnum ae = new AttributeEnum(enumTypeName, name, datatype);
    	
    	// Fill PROPERTIES into com.bitgenre.orange.domain.Attribute
        if (enumAttribute.getChildCount() == 3)  {
            List<AttributeOption> options = createAttributeOptionsFromAST((CommonTree)enumAttribute.getChildren().get(2));
            ae.setOptions(options);        
        }
    	
    	return ae; 	
    }
    
    
    protected static AttributeDecimal
    createDecimalType(String attrName, AttrType datatype, CommonTree decimalAttribute)
    {
         AttributeDecimal attr = new AttributeDecimal(attrName, datatype);

        // Child node 0 == attribute name (required)
        // Child node 1 == PRECISION_SCALE node (required) contains precision and scale as child nodes.
        // Child node 2 == PROPERTIES. Node is required, but might not have any children.

        // Fill PROPERTIES into com.bitgenre.orange.domain.Attribute
        if (decimalAttribute.getChildCount() == 3)  {
            List<AttributeOption> options = createAttributeOptionsFromAST((CommonTree)decimalAttribute.getChildren().get(2));
            attr.setOptions(options);
            
        }

        // Get Precision and Scale Node
        CommonTree precisionScaleNode = (CommonTree)decimalAttribute.getChildren().get(1);

        // Get precision and scale values from child nodes.
        // Child Node 0 == Precision value as text
        // Child Node 1 == Scale value as text
        //
        CommonTree precisionNode = (CommonTree)precisionScaleNode.getChildren().get(0);
        CommonTree scaleNode = (CommonTree)precisionScaleNode.getChildren().get(1);

        Integer precision = Integer.parseInt(precisionNode.getText());
        Integer scale = Integer.parseInt(scaleNode.getText());

        attr.setPrecision(precision);
        attr.setScale(scale);

        return attr;
    }


    protected static AttributeLength
    createLengthType(String attrName, AttrType datatype, CommonTree lengthAttribute)
    {
         AttributeLength attr = new AttributeLength(attrName, datatype);

        // Child node 0 == attribute name (required)
        // Child node 1 == LENGTH node (required) contains length value as child nodes.
        // Child node 2 == PROPERTIES. Node is required, but might not have any children.

        // Fill PROPERTIES into com.bitgenre.orange.domain.Attribute
        if (lengthAttribute.getChildCount() == 3)  {
            List<AttributeOption> options = createAttributeOptionsFromAST((CommonTree)lengthAttribute.getChildren().get(2));
            attr.setOptions(options);
            
        }

        // Get LENGTH Node
        CommonTree lengthParentNode = (CommonTree)lengthAttribute.getChildren().get(1);

        // Get Length value from child nodes.
        // Child Node 0 == Length value as text
        //
        CommonTree lengthNode = (CommonTree)lengthParentNode.getChildren().get(0);

        Integer length = Integer.parseInt(lengthNode.getText());

        attr.setLength(length);

        return attr;
    }

    //
    // Fills com.bitgenre.orange.domain.Attribute for the basic datatypes (Boolean, Date, Datetime, Timestamp, etc.)
    //
    protected static Attribute
    createSimpleType(String attrName, AttrType datatype, CommonTree refAttribute)
    {
         Attribute attr = new Attribute(attrName, datatype);

        // Child node 0 == attribute name (required)
        // Child node 1 == PROPERTIES. Node is required, but might not have any children.

        // Fill PROPERTIES into com.bitgenre.orange.domain.Attribute
        if (refAttribute.getChildCount() == 2)  {
        	List<AttributeOption> options = createAttributeOptionsFromAST((CommonTree)refAttribute.getChildren().get(1));
            attr.setOptions(options);
        }

        return attr;
    }

    //
    // Fills com.bitgenre.orange.domain.Attribute data struct for Ref, Bag and Set attribute types.
    //
    /* NOT_USED replaced by Relationships.
    protected static Attribute
    createReferenceType(String attrName, AttrType datatype, CommonTree refAttribute)
    {
        Attribute attr = new Attribute(attrName, datatype);

        // Child node 0 == attribute name (required)
        // Child node 1 == entity type reference refers to 'refersTo' (required)

        // Get the 'refersTo' info
        Tree refersToNode = refAttribute.getChild(1);
        String refersTo = refersToNode.getText();

        // Child node 2 == PROPERTIES, but this is optional.
        if (refAttribute.getChildCount() == 3)  {
            fillProperties(attr, (CommonTree)refAttribute.getChildren().get(2));
        }

        return attr;
    }
    */

    protected static AttributeInt
    createIntegerType(String attrName, AttrType datatype, CommonTree intAttribute)
    {
        AttributeInt attr = new AttributeInt(attrName, datatype);

        // Child node 0 == attribute name (required)
        // Child node 1 == INT_PROPERTIES node (required)
        // Child node 2 == PROPERTIES node (required)

        if (intAttribute.getChildCount() == 3)  {

            // Fill INT_PROPERTIES into com.bitgenre.orange.domain.Attribute
            List<AttributeOption> options = createAttributeOptionsFromAST((CommonTree)intAttribute.getChildren().get(2));
            attr.setOptions(options);

            // Fill PROPERTIES into com.bitgenre.orange.domain.Attribute
            /* NOT USED
             * fillProperties(attr, (CommonTree)intAttribute.getChildren().get(2));
             */

        }

        // See if we got an 'unsigned' property setting, setup the Int style attribute with unsigned properly.
        for (AttributeOption option : attr.getOptions()) {
        	if (option.equals(AttributeOption.Unsigned))
        		attr.setUnsigned(true);
        }
       

        return attr;
    }

    //
    // Fills the 'attribute options' nodes into com.bitgenre.orange.domain.Attribute. (e.g. 'required' 
    // 'primarykey', etc.)
    //
    /*
    @SuppressWarnings("unchecked")
	protected static void
    fillPropertiesx(Attribute toFill, CommonTree propertiesNode)
    {

        if (propertiesNode != null) {


            // Get properties value nodes and add them to the toFill com.bitgenre.orange.domain.Attribute.
            //
            List<CommonTree> properties = propertiesNode.getChildren();
            if (properties != null) {
                for (CommonTree property : properties) {
                    toFill.addProperties(property.getText());
                }
            }
        }
    }
    */
    
    /**
     * Parses out the attribute options ('require', 'primarykey', etc.) from the AST node.
     * 
     */
    @SuppressWarnings("unchecked")
	protected static List<AttributeOption>
    createAttributeOptionsFromAST(CommonTree attrOptionsNode)
    {
    	List<AttributeOption> options = new ArrayList<AttributeOption>();
    	
    	if (attrOptionsNode != null) {
    		
    		List<CommonTree> optionNodes = attrOptionsNode.getChildren();
    		if (optionNodes != null) {
    			for (CommonTree option : optionNodes) {
    				AttributeOption value = AttributeOption.fromString(option.getText());
    				options.add(value);
    			}
    		}
    	}
    	
    	return options;
    	
    }

}
