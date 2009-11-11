package com.bitgenre.orange.domain;

/**
 * The top-level statement parsed from input file.
 */
public enum StatementType {

    Entity("ENTITY"),
    Decorator("DECORATOR"),
    Import("IMPORT"),
    Namespace("NAMESPACE"),
    Relationship("RELATIONSHIP"),
    Enum("ENUM");

    protected String name;


    private StatementType(String name) {
        this.name = name;
    }

    public static StatementType
    fromString(String inName)
    {
        for (StatementType e : values()) {
           if (e.name.equals(inName))
               return e;
        }

        throw(new RuntimeException("Statement unknown: " + inName));
    }
}
