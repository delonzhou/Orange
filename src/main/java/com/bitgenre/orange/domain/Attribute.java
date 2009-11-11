package com.bitgenre.orange.domain;

import java.util.List;
import java.util.ArrayList;

/**
 Parsed from input file
 */
public class Attribute {
    protected String name;
    protected AttrType datatype;
    protected List<AttributeOption> options = new ArrayList<AttributeOption>();

    public Attribute(String name, AttrType datatype) {
        this.name = name;
        this.datatype = datatype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public List<AttributeOption> getOptions() {
        return options;
    }

    public void setOptions(List<AttributeOption> options) {
        this.options = options;
    }

    public AttrType getDatatype() {
        return datatype;
    }

    public void setDatatype(AttrType datatype) {
        this.datatype = datatype;
    }

    public AttributeOption
    findOptionByName(String propertyName)
    {
        for (AttributeOption property : options) {
            if (propertyName.equals(property)) {
                return (property);
            }
        }

        return null; // not found
    }

    

    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append("Attribute: name=");
        bldr.append(this.name);
        bldr.append(", datatype=");
        bldr.append(datatype);
        bldr.append(", options=");
        bldr.append(this.options);
        
        return bldr.toString();
    }
}
