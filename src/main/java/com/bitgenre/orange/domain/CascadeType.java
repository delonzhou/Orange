package com.bitgenre.orange.domain;

/**
 * Represents the cascading property of a relationship.
 * 
 * @author brianmcginnis
 *
 */
public enum CascadeType {
	CascadeNone("cascadeNone"),
	CascadeSave("cascadeSave"),
	CascadeDelete("cascadeDelete"),
	CascadeAll("cascadeAll");
	
	protected String name;
	   
	private CascadeType(String name) { this.name = name; }
	
  /**
    * Converts String version  into Enum version.
    * Throws RuntimeException if no matches.
    * @param name
    * @return
    */
   public static CascadeType 
   fromString(String name)
   {
	   for (CascadeType ao : values()) {
		   if (ao.name.equals(name))
			   return ao;
	   }
	   
	   throw (new RuntimeException("CascadeType '" + name + "' not found."));
   }

}
