package com.bitgenre.orange.domain;

/**
 * The type of relationship, more specifically cardinality.
 *
 */
public enum RelationshipType {

    Reference("Ref"),
    Set("Set"),
    Bag("Bag");
    
    protected String name;

    private RelationshipType(String name) { this.name = name; }

    public static RelationshipType
    fromParser(String name)
    {
       for (RelationshipType e : values()) {
           if (e.name.equals(name))
               return e;
       }

       throw(new RuntimeException("Unknown RelationshipType: " + name));
    }
}
