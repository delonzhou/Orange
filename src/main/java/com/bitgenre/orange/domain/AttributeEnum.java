package com.bitgenre.orange.domain;

public class AttributeEnum extends Attribute {
    protected final String enumTypeName;

    public AttributeEnum(String enumTypeName, String name, AttrType datatype) {
        super(name, datatype);
        this.enumTypeName = enumTypeName;
    }

    

    public String getEnumTypeName() {
		return enumTypeName;
	}



	@Override
    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append(super.toString());

        bldr.append(", EnumType=");
        bldr.append(this.enumTypeName);
        

        return bldr.toString();
    }
}
