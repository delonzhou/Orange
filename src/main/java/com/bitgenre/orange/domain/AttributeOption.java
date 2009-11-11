package com.bitgenre.orange.domain;

/**
 * Represent's Oranges Attribute options. For example:
 * 
 * entity Person {
 *   String name[35] unique required;  // unique and required are attribute options.
 * }
 * 
 * @author brianmcginnis
 *
 */
public enum AttributeOption {
   Required("required"),
   Deprecated("deprecated"),
   Unique("unique"), 
   Unsigned("unsigned"),  // For Int based types only.
   PrimaryKey("primarykey");
   
   protected String name;
   
   private AttributeOption(String name) { this.name = name; }
   
   /**
    * Converts String version of attribute option into Enum version.
    * Throws RuntimeException if no matches.
    * @param name
    * @return
    */
   public static AttributeOption 
   fromString(String name)
   {
	   for (AttributeOption ao : values()) {
		   if (ao.name.equals(name))
			   return ao;
	   }
	   
	   throw (new RuntimeException("AttributeOption '" + name + "' not found."));
   }
}
