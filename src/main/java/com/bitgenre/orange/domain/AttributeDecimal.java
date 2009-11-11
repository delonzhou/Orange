package com.bitgenre.orange.domain;

import com.bitgenre.orange.domain.Attribute;

/**
 *    A decimal based attribute.
 */
public class AttributeDecimal extends Attribute {
    protected Integer scale;
    protected Integer precision;
    
    public AttributeDecimal(String name, AttrType datatype) {
        super(name, datatype);
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    @Override
    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append(super.toString());

        bldr.append(", precision=");
        if (this.precision == null)
            bldr.append("null");
        else
            bldr.append(this.precision);

        bldr.append(", scale=");
        if (this.scale == null)
            bldr.append("null");
        else
            bldr.append(this.scale);

        return bldr.toString();
    }

}
