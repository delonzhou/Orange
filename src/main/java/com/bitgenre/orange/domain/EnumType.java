package com.bitgenre.orange.domain;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class EnumType {
	
	protected final String typeName;
	protected List<String> values = new ArrayList<String>();
    protected Namespace namespace;
    
    
	public EnumType(String typeName, List<String> values,
			Namespace namespace) 
	{
		super();
		this.typeName = typeName;
		this.values = values;
		this.namespace = namespace;
	}
	
	public EnumType(Namespace namespace, String typeName)
	{
		super();
		this.typeName = typeName;
		this.namespace = namespace;
	}
	
	public List<String> getValues() {
		return values;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void
	addValue(String value)
	{
		this.values.add(value);
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}
	public String getTypeName() {
		return typeName;
	}
	
	@SuppressWarnings("unchecked")
	public static EnumType
    createEnumFromAST(CommonTree enumCreateNode, Namespace namespace)
    {
        //
        // First get the enum's name. Child 0 == Enum name
        //
        CommonTree enumNameNode = (CommonTree)enumCreateNode.getChildren().get(0);


        String enumName = enumNameNode.getText();

        EnumType enumType = new EnumType(namespace, enumName);


        //
        // Process Enum's values
        //
        List<CommonTree> valueNodes = enumNameNode.getChildren();

        if (valueNodes != null) {

            for (CommonTree enumValueNode : valueNodes) {
                String value = enumValueNode.getText();
                enumType.addValue(value);
            }
        }

       return enumType;
    }
    
	 public String
	    toString()
	    {
	        StringBuilder bldr = new StringBuilder();

	        bldr.append("Enum: namespace=");
	        bldr.append(this.namespace);
	        bldr.append(", name=");
	        bldr.append(this.typeName);
	        bldr.append(", values: ");
	        
	        boolean first = true;
	        for (String value : this.values) {
	        	if (!first) {
	        		bldr.append(", ");
	        		bldr.append(value.toString());
	        	} else {	
	        		bldr.append(value.toString());
	        		first = false;
	        	}
	        }

	        return bldr.toString();
	    }
	

}
