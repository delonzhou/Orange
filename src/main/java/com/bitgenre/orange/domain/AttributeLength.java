package com.bitgenre.orange.domain;

import com.bitgenre.orange.domain.Attribute;

/**
 * An attribute that has a 'length' parameter. Example: String, Char, Bit, etc.
 */
public class AttributeLength extends Attribute {
    protected Integer length;

    public AttributeLength(String name, AttrType datatype) {
        super(name, datatype);
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append(super.toString());

        bldr.append(", length=");
        if (this.length == null)
            bldr.append("null");
        else
            bldr.append(this.length);
        
        return bldr.toString();
    }
}
