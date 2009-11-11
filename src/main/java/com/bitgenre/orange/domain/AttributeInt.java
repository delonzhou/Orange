package com.bitgenre.orange.domain;

import com.bitgenre.orange.domain.Attribute;

/**
 * Int based attribute
 */
public class AttributeInt extends Attribute {
    boolean isUnsigned = false;

    public AttributeInt(String name, AttrType datatype) {
        super(name, datatype);
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
    }

    @Override
    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append(super.toString());

        bldr.append(", isUnsigned=");
        bldr.append(this.isUnsigned);
        

        return bldr.toString();
    }
}
